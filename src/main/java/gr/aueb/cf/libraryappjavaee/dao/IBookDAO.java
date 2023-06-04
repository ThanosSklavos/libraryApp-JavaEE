package gr.aueb.cf.libraryappjavaee.dao;
import gr.aueb.cf.libraryappjavaee.model.Book;

import java.util.List;

public interface IBookDAO {
    List<Book> getBooksByTitle(String title);
    Book getBookByTitle(String title);
    Book getById(Long id);
    Book update(Book book);

    Book insert(Book book);

    Book delete(Long id);

    List<Book> getAllBooks();
}
