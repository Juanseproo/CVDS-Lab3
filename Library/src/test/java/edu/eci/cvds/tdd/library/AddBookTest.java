package edu.eci.cvds.tdd.library;

import edu.eci.cvds.tdd.library.Library;
import edu.eci.cvds.tdd.library.book.Book;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.HashMap;


public class AddBookTest {

    @Test
    public void testApp() {
        assertTrue(true);
    }
    
    /* Añadir un libro nuevo
    *  Verificar que la cantidad de libros sea 1.
    *  Verificar que aparezca en la lista de libros.
    *  Verificar que la cantidad de ese libro sea 1.
    */
    @Test
    public void testAddNewBook() {
        Library library = new Library();
        Book book = new Book("Effective Java", "Juan Buitrago", "132456789");

        library.addBook(book);

        Map<Book, Integer> books = library.getBooks();

        assertEquals(1, books.size());
        assertTrue(books.containsKey(book));
        assertEquals(Integer.valueOf(1), books.get(book));
    }

    @Test
    public void testAddBookWithNullTitle() {
        Library library = new Library();

        assertThrows(IllegalArgumentException.class, () -> {
            library.addBook(null);
        });
    }
}
