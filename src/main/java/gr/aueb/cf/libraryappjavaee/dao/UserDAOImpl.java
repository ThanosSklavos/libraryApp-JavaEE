package gr.aueb.cf.libraryappjavaee.dao;

import gr.aueb.cf.libraryappjavaee.model.User;
import gr.aueb.cf.libraryappjavaee.service.util.JPAHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
@RequestScoped
public class UserDAOImpl implements IUserDAO{
    @Override
    public User insert(User user) {
        EntityManager em = JPAHelper.getEntityManager();
        em.persist(user);
        return em.find(User.class, user.getId());  // will return null user if operation fails
    }

    @Override
    public User update(User user) {
        EntityManager em = JPAHelper.getEntityManager();
        em.merge(user);
        return em.find(User.class, user.getId());  // will return user before update if operation fails
    }

    @Override
    public User delete(Long id) {
        EntityManager em = JPAHelper.getEntityManager();
        User userToDelete = em.find(User.class, id);
        em.remove(userToDelete);
        return userToDelete; // will return null user if operation fails
    }

    @Override
    public List<User> getUsersByUsername(String username) {
        EntityManager em = JPAHelper.getEntityManager();
        TypedQuery<User> query = em.createQuery(
                "SELECT u FROM User u WHERE u.username LIKE :username", User.class);
        query.setParameter("username", "%" + username + "%");
        return query.getResultList(); // will return empty list if operation finds nothing
    }

    @Override
    public User getByUsername(String username) {
        EntityManager em = JPAHelper.getEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class);
        query.setParameter("username", username);

        User user = null;
        try {
            //getSingleResult() does not return null if nothing is found instead throws NoResultException
            return user = query.getSingleResult();
        }catch (NoResultException e) {
            return user;
        }
    }

    @Override
    public User getById(long id) {
        EntityManager em = JPAHelper.getEntityManager();
        return em.find(User.class, id); // will return null user if operation fails
    }

    public List<User> getAllUsers() {
        EntityManager em = JPAHelper.getEntityManager();
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u", User.class);
        return query.getResultList(); // will return empty list if operation finds nothing
    }
}
