package skilltest.bookstore.controller;

import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import skilltest.bookstore.dto.CustomerDto;
import skilltest.bookstore.dto.FullNameDto;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Sql(value = {"/sql/CustomerControllerIntegrationTest.sql"}, executionPhase = ExecutionPhase.BEFORE_TEST_CLASS)
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private final CustomerDto customerDto = CustomerDto.builder()
                                                       .fullName(FullNameDto.builder()
                                                                            .firstName("a first name 1")
                                                                            .lastName("a last name 1")
                                                                            .build())
                                                       .email("test@example2.be")
                                                       .build();

    @Test
    @WithMockUser
    void getAllCustomers_withCustomer_returnsListOfCustomer() throws Exception {
        mockMvc.perform(get("/customers"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()", greaterThan(0)));
    }

    @Test
    @WithMockUser
    void getCustomerByEmail_withCustomer_returnsCustomer() throws Exception {
        mockMvc.perform(get("/customers/by-email").param("email", "TEST@EXAMPLE.BE"))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(10)))
               .andExpect(jsonPath("$.fullName.id", is(10)))
               .andExpect(jsonPath("$.fullName.firstName", is("A FIRST NAME")))
               .andExpect(jsonPath("$.fullName.lastName", is("A LAST NAME")))
               .andExpect(jsonPath("$.email", is("TEST@EXAMPLE.BE")));
    }

    @Test
    @WithMockUser
    void getCustomerById_withCustomer_returnsCustomer() throws Exception {
        long id = 10L;
        mockMvc.perform(get("/customers/{id}", id))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is((int) id)))
               .andExpect(jsonPath("$.email", is("TEST@EXAMPLE.BE")));
    }

    @Test
    @WithMockUser
    void createCustomer_withValidCustomer_returnsCreatedCustomer() throws Exception {
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(customerDto)))
               .andDo(print())
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is(1)))
               .andExpect(jsonPath("$.email", is(customerDto.getEmail())));
    }
}
