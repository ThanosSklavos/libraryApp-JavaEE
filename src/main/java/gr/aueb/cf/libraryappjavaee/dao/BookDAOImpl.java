package gr.aueb.cf.libraryappjavaee.dao;
import gr.aueb.cf.libraryappjavaee.model.Book;
import gr.aueb.cf.libraryappjavaee.service.util.JPAHelper;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.ext.Provider;
import java.util.List;

@Provider
@RequestScoped
public class BookDAOImpl implements IBookDAO{
    @Override
    public List<Book> getBooksByTitle(String title) {
        EntityManager em = JPAHelper.getEntityManager();
        TypedQuery<Book> query = em.createQuery(
                "SELECT b FROM Book b WHERE b.title LIKE :title", Book.class);
        query.setParameter("title", "%" + title + "%");
        return query.getResultList(); // will return empty list if operation finds nothing
    }

    @Override
    public Book getBookByTitle(String title) {
        EntityManager em = JPAHelper.getEntityManager();
        TypedQuery<Book> query = em.createQuery("SELECT b FROM Book b WHERE b.title = :title", Book.class);
        query.setParameter("title", title);

        try {
            //getSingleResult() does not return null if nothing is found instead throws NoResultException
            return query.getSingleResult();
        }catch (NoResultException e) {
            return null;

        }
    }
}
