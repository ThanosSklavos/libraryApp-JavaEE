package gr.aueb.cf.libraryappjavaee.service;

import gr.aueb.cf.libraryappjavaee.model.Book;
import gr.aueb.cf.libraryappjavaee.model.User;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityNotFoundException;

import java.util.List;

public interface IBookService {
    List<Book> getBooksByTitle(String title) throws EntityNotFoundException;
    Book getBookByTitle(String title) throws EntityNotFoundException;
}
