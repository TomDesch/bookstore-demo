package skilltest.bookstore.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import skilltest.bookstore.dto.AuthorDto;
import skilltest.bookstore.dto.BookDto;
import skilltest.bookstore.dto.FullNameDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/sql/BookControllerIntegrationTest.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String VALID_ISBN = "9780306406157";


    @Test
    @WithMockUser
    void getAllBooks_booksExists_returnsBooks() throws Exception {
        mockMvc.perform(get("/books"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$", hasSize(greaterThan(0))))
               .andExpect(jsonPath("$[0].isbn", is(VALID_ISBN)))
               .andExpect(jsonPath("$[0].author.id", is(10)))
               .andExpect(jsonPath("$[0].price", is(29.99D)))
               .andExpect(jsonPath("$[0].description", is("A SAMPLE BOOK")))
               .andExpect(jsonPath("$[0].stock", is(10)));
    }

    @Test
    @WithMockUser
    void getBook_withValidISBN_returnsBook() throws Exception {
        mockMvc.perform(get("/books/{isbn}", VALID_ISBN))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.isbn", is(VALID_ISBN)))
               .andExpect(jsonPath("$.price", is(29.99D)))
               .andExpect(jsonPath("$.description", is("A SAMPLE BOOK")))
               .andExpect(jsonPath("$.stock", is(10)));
    }

    @Test
    @WithMockUser
    void getBook_withInvalidISBN_badRequest() throws Exception {
        mockMvc.perform(get("/books/{isbn}", "anInvalidISBN"))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.[0]", is("Invalid ISBN")));
    }

    @Test
    @WithMockUser
    void createBook_withValidISBN_createsBook() throws Exception {
        FullNameDto newFullNameDto = FullNameDto.builder()
                                                .lastName("Doe")
                                                .firstName("Jane")
                                                .build();
        AuthorDto newAuthorDto = AuthorDto.builder()
                                          .fullName(newFullNameDto)
                                          .build();
        BookDto newBookDto = BookDto.builder()
                                    .isbn("0471958697")
                                    .author(newAuthorDto)
                                    .price(BigDecimal.valueOf(19.99))
                                    .description("Another sample book")
                                    .stock(20)
                                    .build();

        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(newBookDto)))
               .andDo(print())
               .andExpect(status().isCreated())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.isbn", is(newBookDto.getIsbn())))
               .andExpect(jsonPath("$.price", is(newBookDto.getPrice()
                                                           .doubleValue())))
               .andExpect(jsonPath("$.description", is(newBookDto.getDescription())))
               .andExpect(jsonPath("$.stock", is(newBookDto.getStock())));
    }

    @Test
    @WithMockUser
    void createBook_withInvalidISBN_badRequest() throws Exception {
        BookDto newBookDto = BookDto.builder()
                                    .isbn("invalidIsbn")
                                    .build();

        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(newBookDto)))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.[0]", is("Invalid ISBN")));
    }

    @Test
    @WithMockUser
    void createBook_withNoISBN_badRequest() throws Exception {
        BookDto newBookDto = BookDto.builder()
                                    .build();

        mockMvc.perform(post("/books").contentType(MediaType.APPLICATION_JSON)
                                      .content(objectMapper.writeValueAsString(newBookDto)))
               .andDo(print())
               .andExpect(status().isBadRequest())
               .andExpect(content().contentType(MediaType.APPLICATION_JSON))
               .andExpect(jsonPath("$.[0]", is("Invalid ISBN")));
    }
}