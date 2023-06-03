package gr.aueb.cf.libraryappjavaee.dao;

import gr.aueb.cf.libraryappjavaee.model.Book;
import gr.aueb.cf.libraryappjavaee.model.User;

import java.util.List;

public interface IBookDAO {
    List<Book> getBooksByTitle(String title);
    Book getBookByTitle(String title);
}
