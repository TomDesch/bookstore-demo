package skilltest.bookstore.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import skilltest.bookstore.dto.AuthorDto;
import skilltest.bookstore.dto.BookDto;
import skilltest.bookstore.dto.FullNameDto;
import skilltest.bookstore.model.Author;
import skilltest.bookstore.model.Book;
import skilltest.bookstore.model.FullName;
import skilltest.bookstore.repository.AuthorRepository;
import skilltest.bookstore.repository.BookRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Book book;
    private BookDto bookDto;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        authorRepository.deleteAll();

        FullName fullName = FullName.builder()
                                    .firstName("John")
                                    .lastName("Doe")
                                    .build();
        Author author = Author.builder()
                              .fullName(fullName)
                              .build();
        book = new Book("1234567890123", author, BigDecimal.valueOf(29.99), "A sample book", 10);
        bookRepository.save(book);

        FullNameDto fullNameDto = FullNameDto.builder()
                                             .id(1L)
                                             .firstName("Joshua")
                                             .lastName("Blosh")
                                             .build();
        AuthorDto authorDto = AuthorDto.builder()
                                       .id(1L)
                                       .fullName(fullNameDto)
                                       .build();
        bookDto = BookDto.builder()
                         .isbn("1234567890123")
                         .author(authorDto)
                         .price(BigDecimal.valueOf(29.99))
                         .description("A sample book")
                         .stock(10)
                         .build();
    }

    @Test
    @WithMockUser
    void getAllBooks() throws Exception {
        mockMvc.perform(get("/books"))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(1)))
               .andExpect(jsonPath("$[0].isbn", is(bookDto.getIsbn())))
               .andExpect(jsonPath("$[0].author.id", is(bookDto.getAuthor()
                                                               .getId()
                                                               .intValue())))
               .andExpect(jsonPath("$[0].price", is(bookDto.getPrice()
                                                           .doubleValue())))
               .andExpect(jsonPath("$[0].description", is(bookDto.getDescription())))
               .andExpect(jsonPath("$[0].stock", is(bookDto.getStock())));
    }

    @Test
    @WithMockUser
    void getBook() throws Exception {
        mockMvc.perform(get("/books/{isbn}", book.getIsbn()))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.isbn", is(bookDto.getIsbn())))
               .andExpect(jsonPath("$.price", is(bookDto.getPrice()
                                                        .doubleValue())))
               .andExpect(jsonPath("$.description", is(bookDto.getDescription())))
               .andExpect(jsonPath("$.stock", is(bookDto.getStock())));
    }

    @Test
    @WithMockUser
    void createBook() throws Exception {
        FullNameDto newFullNameDto = FullNameDto.builder()
                                                .lastName("Doe")
                                                .firstName("Jane")
                                                .build();
        AuthorDto newAuthorDto = AuthorDto.builder()
                                          .fullName(newFullNameDto)
                                          .build();
        BookDto newBookDto = BookDto.builder()
                                    .isbn("9876543210987")
                                    .author(newAuthorDto)
                                    .price(BigDecimal.valueOf(19.99))
                                    .description("Another sample book")
                                    .stock(20)
                                    .build();

        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(newBookDto)))
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.isbn", is(newBookDto.getIsbn())))
               .andExpect(jsonPath("$.price", is(newBookDto.getPrice()
                                                           .doubleValue())))
               .andExpect(jsonPath("$.description", is(newBookDto.getDescription())))
               .andExpect(jsonPath("$.stock", is(newBookDto.getStock())));
    }
}