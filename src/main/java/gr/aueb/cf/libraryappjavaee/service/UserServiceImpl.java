package gr.aueb.cf.libraryappjavaee.service;
import gr.aueb.cf.libraryappjavaee.dao.IUserDAO;
import gr.aueb.cf.libraryappjavaee.dto.UserDTO;
import gr.aueb.cf.libraryappjavaee.model.User;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryappjavaee.service.util.JPAHelper;
import gr.aueb.cf.libraryappjavaee.service.util.LoggerUtil;
import jakarta.persistence.NoResultException;
import org.mindrot.jbcrypt.BCrypt;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
@RequestScoped
public class UserServiceImpl implements IUserService{

    @Inject
    private IUserDAO dao;

    @Override
    public User insert(UserDTO dto) throws EntityAlreadyExistsException {
        User user;

        try {
            JPAHelper.beginTransaction();
            user = map(dto);
            if (user.getId() != null) throw new EntityAlreadyExistsException(User.class);   // dto here, should never have id, it will be auto generated

            if (dao.getUsersByUsername(dto.getUsername()).isEmpty()) {  // check if entity with the same username exists, if not insert.
                user = dao.insert(user);
            } else {
                throw new BadRequestException();
            }
            JPAHelper.commitTransaction();

        } catch (BadRequestException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning("Insert user error - id should be empty - rollback");
            throw e;
        }catch (EntityAlreadyExistsException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning(
                    "Insert user error - entity with username " + dto.getUsername() + " already exists");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return user;
    }

    @Override
    public User update(UserDTO dto) throws EntityNotFoundException {   // here we always need a dto with the id of the entity we want to update
        User userToUpdate;
        try {
            JPAHelper.beginTransaction();
            userToUpdate = map(dto);
            if (dao.getById(userToUpdate.getId()) == null) {    // check by id if the entity we want to update exists
                throw new EntityNotFoundException(User.class, userToUpdate.getId());
            }
            dao.update(userToUpdate);
            JPAHelper.commitTransaction();

        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning("Update user rollback - Entity not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return userToUpdate;
    }

    @Override
    public User delete(Long id) throws EntityNotFoundException {
        User userToDelete;
        try {
            JPAHelper.beginTransaction();
            if (dao.getById(id) == null) {
                throw new EntityNotFoundException(User.class, id);
            }
            userToDelete = dao.delete(id);
            JPAHelper.commitTransaction();

        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning("Delete user rollback - Entity not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return userToDelete;
    }

    @Override
    public List<User> getUsersByUsername(String username) throws EntityNotFoundException {
        List<User> users;
        try {
            JPAHelper.beginTransaction();
            users = dao.getUsersByUsername(username);
            if (users.isEmpty()) {
                throw new EntityNotFoundException(List.class, 0L);
            }
            JPAHelper.commitTransaction();

        }catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning(
                    "Get user by username rollback, username " + username + " not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return users;
    }

    @Override
    public User getUserById(Long id) throws EntityNotFoundException {
        User user;
        try {
            JPAHelper.beginTransaction();
            user = dao.getById(id);
            if (user == null) {
                throw new EntityNotFoundException(User.class, id);
            }
            JPAHelper.commitTransaction();

        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning(
                    "Get user by id rollback, id " + id + " not found");
            throw e;
        } finally {
        JPAHelper.closeEntityManager();
    }
        return user;
    }

    @Override
    public boolean isUserValid(String username, String password) throws EntityNotFoundException {
        User user;
        String hashedPassword;

        try {
            user = dao.getByUsername(username);
            if (user == null) {
                throw new EntityNotFoundException(User.class, 0L);
            }
            hashedPassword = user.getPassword();
            return (BCrypt.checkpw(password, hashedPassword));

        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning("Error in validation - User with username "+ username + " not found");
            throw e;
        }
    }

    private User map(UserDTO dto) {
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setLastname(dto.getLastname());
        user.setFirstname(dto.getFirstname());
        user.setPassword(hashPassword(dto.getPassword()));
        return user;
    }

    private String hashPassword(String password) {
        int workload = 12;
        String salt = BCrypt.gensalt(workload);
        return BCrypt.hashpw(password, salt);
    }


    /* Test Features*/
    @Override
    public List<User> getAllUsers() throws EntityNotFoundException {
        List<User> users;
        try {
            JPAHelper.beginTransaction();
            users = dao.getAllUsers();
            if (users.isEmpty()) {
                throw new EntityNotFoundException(List.class, 0L);
            }
            JPAHelper.commitTransaction();

        }catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning(
                    "Get all users rollback - No users found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return users;
    }

}
