package skilltest.bookstore.controller;

import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import skilltest.bookstore.dto.BookDto;
import skilltest.bookstore.service.BookService;
import skilltest.bookstore.validator.ValidEmail;
import skilltest.bookstore.validator.ValidIsbn;

@RestController
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{isbn}")
    @PreAuthorize("hasRole('USER')")
    public BookDto getBook(@PathVariable @ValidIsbn String isbn) {
        return bookService.getBook(isbn);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public BookDto createBook(@RequestBody @Valid BookDto bookDto) {
        return bookService.createBook(bookDto);
    }
}