package gr.aueb.cf.libraryappjavaee.service;
import gr.aueb.cf.libraryappjavaee.dto.UserDTO;
import gr.aueb.cf.libraryappjavaee.model.User;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryappjavaee.service.exceptions.OutOfStockException;
import java.util.List;


public interface IUserService {
    User insert(UserDTO dto) throws EntityAlreadyExistsException;
    User update(UserDTO dto) throws EntityNotFoundException;
    User delete(Long id) throws EntityNotFoundException;
    List<User> getUsersByUsername(String username) throws EntityNotFoundException;
    List<User> getAllUsers() throws EntityNotFoundException;
    User getUserById(Long id) throws EntityNotFoundException;
    boolean isUserValid(String username, String password) throws EntityNotFoundException;
    void addBook(Long userID, Long bookID) throws EntityAlreadyExistsException, EntityNotFoundException, OutOfStockException;
    void removeBook(Long userID, Long bookID) throws EntityNotFoundException;
}
