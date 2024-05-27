package skilltest.bookstore.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import skilltest.bookstore.dto.BookDto;
import skilltest.bookstore.model.Book;
import skilltest.bookstore.repository.BookRepository;
import skilltest.bookstore.validator.ValidIsbn;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;

    private final ModelMapper modelMapper;
    private static final String BOOK_NOT_FOUND = "Book with ISBN %s not found";

    public List<BookDto> getAllBooks() {
        return bookRepository.findAll()
                             .stream()
                             .map(this::toDto)
                             .collect(Collectors.toList());
    }

    public BookDto getBook(@ValidIsbn String isbn) {
        return bookRepository.findBookByIsbn(isbn)
                             .map(this::toDto)
                             .orElseThrow(() -> new EntityNotFoundException(BOOK_NOT_FOUND.formatted(isbn)));
    }

    public BookDto createBook(BookDto bookDTO) {
        Book book = fromDto(bookDTO);
        Book savedBook = bookRepository.save(book);
        return toDto(savedBook);
    }

    private BookDto toDto(Book book) {
        return modelMapper.map(book, BookDto.class);
    }

    private Book fromDto(BookDto bookDto) {
        return modelMapper.map(bookDto, Book.class);
    }
}