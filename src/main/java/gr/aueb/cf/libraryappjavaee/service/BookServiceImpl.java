package gr.aueb.cf.libraryappjavaee.service;
import gr.aueb.cf.libraryappjavaee.dao.IBookDAO;
import gr.aueb.cf.libraryappjavaee.model.Book;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityNotFoundException;
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
}
