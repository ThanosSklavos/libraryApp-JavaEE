package gr.aueb.cf.libraryappjavaee.rest;
import gr.aueb.cf.libraryappjavaee.dto.BookDTO;
import gr.aueb.cf.libraryappjavaee.model.Book;
import gr.aueb.cf.libraryappjavaee.service.IBookService;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/books")
public class BookRestController {

    @Inject
    private IBookService service;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookByTitle(@QueryParam("title") String title) {
        Book book;
        try {
            book = service.getBookByTitle(title);
            BookDTO bookDTO = map(book);
            return Response.status(Response.Status.OK).entity(bookDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Book with title " + title + " was not found").build();
        }
    }


    private BookDTO map(Book book) {
        BookDTO bookDTO = new BookDTO();
        bookDTO.setId(book.getId());
        bookDTO.setTitle(book.getTitle());
        bookDTO.setAuthor(book.getAuthor());
        bookDTO.setNumberOfCopies(book.getNumberOfCopies());
        return bookDTO;
    }
}
