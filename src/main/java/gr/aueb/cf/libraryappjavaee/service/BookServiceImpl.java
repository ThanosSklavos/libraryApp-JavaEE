package gr.aueb.cf.libraryappjavaee.service;
import gr.aueb.cf.libraryappjavaee.dao.IBookDAO;
import gr.aueb.cf.libraryappjavaee.dto.BookDTO;
import gr.aueb.cf.libraryappjavaee.model.Book;
import gr.aueb.cf.libraryappjavaee.model.User;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryappjavaee.service.exceptions.OutOfStockException;
import gr.aueb.cf.libraryappjavaee.service.util.JPAHelper;
import gr.aueb.cf.libraryappjavaee.service.util.LoggerUtil;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
@RequestScoped
public class BookServiceImpl implements IBookService {

    @Inject
    private IBookDAO dao;
    @Override
    public List<Book> getBooksByTitle(String title) throws EntityNotFoundException {
        List<Book> books;
        try {
            JPAHelper.beginTransaction();
            books = dao.getBooksByTitle(title);
            if (books.isEmpty()) {
                throw new EntityNotFoundException(List.class, 0L);
            }
            JPAHelper.commitTransaction();

        }catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning(
                    "Get book by title rollback, title " + title + " not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return books;
    }

    @Override
    public Book getBookByTitle(String title) throws EntityNotFoundException {
        Book book;
        try {
            JPAHelper.beginTransaction();
            book = dao.getBookByTitle(title);
            if (book == null) {
                throw new EntityNotFoundException(Book.class, 0L);
            }
            JPAHelper.commitTransaction();

        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning(
                    "Get book by title rollback, title " + title + " not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return book;
    }

    @Override
    public Book getBookByID(Long id) throws EntityNotFoundException {
        Book book;
        try {
            JPAHelper.beginTransaction();
            book = dao.getById(id);
            if (book == null) {
                throw new EntityNotFoundException(Book.class, id);
            }
            JPAHelper.commitTransaction();

        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning(
                    "Get book by id rollback, id " + id + " not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return book;
    }

    @Override
    public Book update(BookDTO bookDTO) throws EntityNotFoundException {
        Book bookToUpdate;
        try {
            JPAHelper.beginTransaction();
            bookToUpdate = map(bookDTO);
            if (dao.getById(bookToUpdate.getId()) == null) {    // check by id if the entity we want to update exists
                throw new EntityNotFoundException(User.class, bookToUpdate.getId());
            }
            dao.update(bookToUpdate);
            JPAHelper.commitTransaction();

        } catch (EntityNotFoundException e) {
            JPAHelper.rollbackTransaction();
            LoggerUtil.getCurrentLogger().warning("Update book rollback - Entity not found");
            throw e;
        } finally {
            JPAHelper.closeEntityManager();
        }
        return bookToUpdate;
    }

    @Override
    public Book decreaseNumberOfCopies(Book book) throws OutOfStockException {
        int numberOfCopies = book.getNumberOfCopies();

        if (numberOfCopies > 0) {
            book.setNumberOfCopies(--numberOfCopies);
        } else {
            throw new OutOfStockException(book);
        }
        return book;
    }

    @Override
    public Book increaseNumberOfCopies(Book book) {
        int numberOfCopies = book.getNumberOfCopies();

        book.setNumberOfCopies(++numberOfCopies);
        return book;
    }

    private Book map(BookDTO dto) {
        Book book = new Book();
        book.setId(dto.getId());
        book.setTitle(dto.getTitle());
        book.setAuthor(dto.getAuthor());
        book.setNumberOfCopies(dto.getNumberOfCopies());
        return book;
    }
}
