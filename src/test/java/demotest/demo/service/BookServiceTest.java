package demotest.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import demotest.demo.dto.AuthorDto;
import demotest.demo.dto.BookDto;
import demotest.demo.dto.FullNameDto;
import demotest.demo.model.Author;
import demotest.demo.model.Book;
import demotest.demo.model.FullName;
import demotest.demo.repository.BookRepository;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        FullName fullName = new FullName(2L, "Joshua", "Blosh");

        Author author = new Author(1L, fullName);
        book = new Book("1234567890123", author, BigDecimal.valueOf(29.99), "A sample book", 10);

        FullNameDto fullNameDto = new FullNameDto(2L, "Joshua", "Blosh");
        AuthorDto authorDto = new AuthorDto(1L, fullNameDto);
        bookDto = new BookDto("1234567890123", authorDto, BigDecimal.valueOf(29.99), "A sample book", 10);
    }

    @Test
    void getAllBooks_dbContainsNoBooks_returnsEmptyList() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        List<BookDto> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(0, books.size());
        assertTrue(books.isEmpty());
        verify(bookRepository).findAll();
    }

    @Test
    void getAllBooks_dbContainsBooks_returnsAllBooks() {
        when(bookRepository.findAll()).thenReturn(Collections.singletonList(book));
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        List<BookDto> books = bookService.getAllBooks();

        assertNotNull(books);
        assertEquals(1, books.size());
        assertEquals(bookDto, books.get(0));
        verify(bookRepository).findAll();
    }

    @Test
    void getBook_noBook_throwsException() {
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.empty());
        assertThrows(EntityNotFoundException.class, () -> bookService.getBook(book.getIsbn()), "Book with ISBN %s not found".formatted(book.getIsbn()));
        verify(bookRepository).findBookByIsbn(book.getIsbn());
    }

    @Test
    void getBook_bookExists_returnsBook() {
        when(bookRepository.findBookByIsbn(book.getIsbn())).thenReturn(Optional.of(book));
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        BookDto foundBook = bookService.getBook(book.getIsbn());

        assertNotNull(foundBook);
        assertEquals(bookDto, foundBook);
        verify(bookRepository).findBookByIsbn(book.getIsbn());
    }

    @Test
    void createBook_validDto_ok() {
        when(modelMapper.map(bookDto, Book.class)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(modelMapper.map(book, BookDto.class)).thenReturn(bookDto);

        BookDto createdBook = bookService.createBook(bookDto);

        assertNotNull(createdBook);
        assertEquals(bookDto, createdBook);
        verify(bookRepository).save(book);
    }
}