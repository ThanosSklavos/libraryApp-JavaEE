package gr.aueb.cf.libraryappjavaee.service;
import gr.aueb.cf.libraryappjavaee.dto.BookDTO;
import gr.aueb.cf.libraryappjavaee.model.Book;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryappjavaee.service.exceptions.OutOfStockException;

import java.util.List;

public interface IBookService {
    List<Book> getBooksByTitle(String title) throws EntityNotFoundException;
    Book getBookByTitle(String title) throws EntityNotFoundException;

    Book getBookByID(Long id) throws EntityNotFoundException;

    Book update(BookDTO bookDTO) throws EntityNotFoundException;

    Book decreaseNumberOfCopies(Book book) throws OutOfStockException;

    Book increaseNumberOfCopies(Book book);
}
