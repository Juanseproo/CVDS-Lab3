package edu.eci.cvds.tdd;

import edu.eci.cvds.tdd.library.Library;
import edu.eci.cvds.tdd.library.book.Book;
import edu.eci.cvds.tdd.library.loan.Loan;
import edu.eci.cvds.tdd.library.loan.LoanStatus;
import edu.eci.cvds.tdd.library.user.User;

import org.junit.*;
import static org.junit.Assert.*;

/*
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

    /*
     * Probar crear un préstamo exitoso.
     * Verificar que el préstamo se ha creado, el estado es ACTIVE y el libro está en préstamo.
     * Verificar que después del prestamo, no queda el libro disponible.
     */
    @Test
    public void testLoanABookSuccessfully() {
        //Crear un libro y añadirlo a la Libreria
        Book book = new Book("Effective Java", "Juan Buitrago", "9780134685991");
        library.addBook(book);

        //Pedir un libro
        Loan loan = library.loanABook("test123", "9780134685991");

        assertNotNull(loan);
        assertEquals(loan.getStatus(), LoanStatus.ACTIVE);
        assertEquals(library.getBooks().get(book).intValue(), 0);
    }

    /*
     * Probar no permitir préstamo si el usuario no existe.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoanABookUserDoesNotExist() {
        //Crear un libro y añadirlo a la Libreria
        Book book = new Book("Effective Java", "Juan Buitrago", "9780134685991");
        library.addBook(book);

        //Pedir un libro
        library.loanABook("nonexistentUser", "9780134685991");

    }

    /*
     * Probar no permitir préstamo si el libro no está disponible.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoanABookNotAvailable() {
        //Pedir un libro
        library.loanABook("test123", "invalidISBN");
    }

    /*
     * Prueba no permitir que se preste un libro si no hay existencias
     */
    @Test(expected = IllegalArgumentException.class)
    public void testLoanABookWithNoMoreCopies() {
        // Crear otro usuario para la prueba
        User testUser = new User();
        testUser.setId("test1234");
        testUser.setName("Test User2");
        library.addUser(testUser);

        // Crear un libro y añadirlo a la Libreria
        Book book = new Book("Effective Java", "Juan Buitrago", "9780134685991");
        library.addBook(book);

        library.loanABook("test123", "9780134685991");
        library.loanABook("test1234", "9780134685991");
    }

    /*
     * Prueba no permitir que un usuario tenga más de un préstamo activo para el mismo libro.
     */
    @Test(expected = IllegalStateException.class)
    public void testLoanABookAlreadyLoaned() {
        // Crear un libro y añadirlo a la Libreria
        Book book = new Book("Effective Java", "Juan Buitrago", "9780134685991");
        library.addBook(book);
        library.addBook(book);

        library.loanABook("test123", "9780134685991");
        library.loanABook("test123", "9780134685991");
    }

    /*
     * Probar devolver un préstamo exitoso.
     * Verificar que el préstamo se ha modificado, el estado es RETURNED y el libro ya no está en préstamo.
     */
    @Test
    public void testReturnLoanSuccessfully() {
        Book book = new Book("Effective Java", "Juan Medina", "9780134685991");
        library.addBook(book);

        Loan loan = library.loanABook("test123", "9780134685991");
        assertNotNull(loan);

        Loan returnedLoan = library.returnLoan(loan);

        assertNotNull(returnedLoan);
        assertEquals(LoanStatus.RETURNED, returnedLoan.getStatus());
        assertEquals(1, (int) library.getBooks().get(book));
        assertNotNull(returnedLoan.getReturnDate());
    }

    /*
     * Probar devolver un préstamo inexistente.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReturnNonexistentLoan() {
        Book book = new Book("Effective Java", "Juan Medina", "9780134685991");
        library.addBook(book);
        Loan nonExistentLoan = new Loan();
        library.returnLoan(nonExistentLoan);
    }

    /*
     * Probar devolver un préstamo con estado "RETURNED".
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReturnLoanTwice() {
        Book book = new Book("Effective Java", "Juan Medina", "9780134685991");
        library.addBook(book);

        Loan loan = library.loanABook("test123", "9780134685991");
        assertNotNull(loan);

        library.returnLoan(loan);
        library.returnLoan(loan);
    }

    /*
     * Probar devolver un préstamo para un libro que no ha sido prestado.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReturnLoanForBookNotOnLoan() {
        Book book = new Book("Effective Java", "Juan Medina", "9780134685991");
        library.addBook(book);

        Loan loan = new Loan();
        loan.setBook(book);
        loan.setUser(testUser);
        loan.setStatus(LoanStatus.ACTIVE);
        library.returnLoan(loan);
    }
}