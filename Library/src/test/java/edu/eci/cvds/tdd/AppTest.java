package edu.eci.cvds.tdd;

import edu.eci.cvds.tdd.library.Library;
import edu.eci.cvds.tdd.library.book.Book;
import edu.eci.cvds.tdd.library.user.User;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Unit test for App.
 */
public class AppTest {
    private Library library;
    private User testUser;

    @Before
    public void setUp() {
        library = new Library();
        testUser = new User();
        testUser.setId("test123");
        testUser.setName("Test User");
        library.addUser(testUser);
    }

    /*
     * Añade libro null
     */
    @Test
    public void testAddNullBook() {
        assertFalse(library.addBook(null));
    }

    /*
     * Verifica que el método retorne true cuando se agrega un libro que no existe en la biblioteca.
     */
    @Test
    public void testAddNewBook() {
        Book newBook = new Book("New Book", "Author", "1234567890");
        assertTrue(library.addBook(newBook));
    }

    /*
     * Verifica que el método retorne true cuando se agrega un libro que ya existe en la biblioteca.
     */
    @Test
    public void testAddExistingBook() {
        Book book = new Book("Existing Book", "Author", "0987654321");
        assertTrue(library.addBook(book));
        assertTrue(library.addBook(book));
    }

    /*
     * Verifica que el método retorne true cuando se agrega dos libro que no existen en la biblioteca.
     */
    @Test
    public void testAddMultipleBooks() {
        Book book1 = new Book("Book 1", "Author 1", "1111111111");
        Book book2 = new Book("Book 2", "Author 2", "2222222222");
        
        assertTrue(library.addBook(book1));
        assertTrue(library.addBook(book2));
        assertTrue(library.addBook(book1));
    }

    /*
     * Verifica que el método trate estos libros como el mismo libro y solo incremente la cantidad.
     */
    @Test
    public void testAddBooksWithSameISBN() {
        // Crear un libro
        Book existingBook = new Book("Existing Book", "Existing Author", "2222222222");

        // Agregar el libro por primera vez
        assertTrue(library.addBook(existingBook));

        // Verificar que la cantidad sea 1
        assertEquals(Integer.valueOf(1), library.getBooks().get(existingBook));

        // Agregar el mismo libro nuevamente
        assertTrue(library.addBook(existingBook));

        // Verificar que la cantidad se haya incrementado a 2
        assertEquals(Integer.valueOf(2), library.getBooks().get(existingBook));
    }

}