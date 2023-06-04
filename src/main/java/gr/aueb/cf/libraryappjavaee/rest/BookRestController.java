package gr.aueb.cf.libraryappjavaee.rest;
import gr.aueb.cf.libraryappjavaee.dto.BookDTO;
import gr.aueb.cf.libraryappjavaee.dto.UserDTO;
import gr.aueb.cf.libraryappjavaee.model.Book;
import gr.aueb.cf.libraryappjavaee.model.User;
import gr.aueb.cf.libraryappjavaee.service.IBookService;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityNotFoundException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;

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

    @Path("/getall")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllBooks() {
        List<Book> books;

        try {
            books = service.getAllBooks();
            List<BookDTO> bookDTOs = new ArrayList<>();
            for (Book book : books) {
                bookDTOs.add(map(book));
            }
            return Response.status(Response.Status.OK).entity(bookDTOs).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("No books found").build();
        }
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getBookById(@PathParam("id") Long id) {
        Book book;
        try {
            book = service.getBookByID(id);
            BookDTO bookDTO = map(book);
            return Response.status(Response.Status.OK).entity(bookDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Book with id " + id + " was not found").build();
        }
    }

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addBook(BookDTO dto, @Context UriInfo uriInfo) {
        try {
            Book book = service.insert(dto);
            BookDTO bookDTO = map(book);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            return Response.created(uriBuilder.path(Long.toString(bookDTO.getId())).build())
                    .entity(bookDTO).build();
        } catch (EntityAlreadyExistsException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("User already exists")
                    .build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteBook(@PathParam("id") Long id) {
        try {
            Book book = service.getBookByID(id);
            service.delete(id);
            BookDTO bookDTO = map(book);
            return Response.status(Response.Status.OK).entity(bookDTO).build();
        } catch (EntityNotFoundException e1) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("Book with id " + id + " was not found")
                    .build();
        }
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateBook(@PathParam("id") Long id, BookDTO dto) {
        try {
            dto.setId(id);
            Book book = service.update(dto);
            BookDTO dtoToReturn = map(book);
            return Response.status(Response.Status.OK).entity(dtoToReturn).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("Book with id " + id + " was not found").build();
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
