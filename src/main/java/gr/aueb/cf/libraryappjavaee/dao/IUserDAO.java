package gr.aueb.cf.libraryappjavaee.dao;

import gr.aueb.cf.libraryappjavaee.model.User;

import java.util.List;

public interface IUserDAO {
    User insert(User user);
    User update(User user);
    User delete(Long id);
    List<User> getUsersByUsername(String username);
    List<User> getAllUsers();
    User getByUsername(String username);
    User getById(long id);

}
