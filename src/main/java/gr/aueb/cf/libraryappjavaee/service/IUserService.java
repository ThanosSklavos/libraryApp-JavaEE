package gr.aueb.cf.libraryappjavaee.service;

import gr.aueb.cf.libraryappjavaee.dto.BookDTO;
import gr.aueb.cf.libraryappjavaee.dto.UserDTO;
import gr.aueb.cf.libraryappjavaee.model.Book;
import gr.aueb.cf.libraryappjavaee.model.User;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Set;

public interface IUserService {
    User insert(UserDTO dto) throws EntityAlreadyExistsException;
    User update(UserDTO dto) throws EntityNotFoundException;
    User delete(Long id) throws EntityNotFoundException;
    List<User> getUsersByUsername(String username) throws EntityNotFoundException;
    List<User> getAllUsers() throws EntityNotFoundException;
    User getUserById(Long id) throws EntityNotFoundException;
    boolean isUserValid(String username, String password) throws EntityNotFoundException;
    void addBook(User user, BookDTO bookDTO);
}
