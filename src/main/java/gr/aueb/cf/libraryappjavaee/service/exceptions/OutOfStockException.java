package gr.aueb.cf.libraryappjavaee.service.exceptions;


import gr.aueb.cf.libraryappjavaee.model.Book;

public class OutOfStockException extends Exception{
    private static final long serialVersionUID = 1L;

    public OutOfStockException(Book book) {
        super("Title " + book.getTitle() + " with id " + book.getId() + " is out of stock, please call us for more information");
    }
}
