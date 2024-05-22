package skilltest.bookstore.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Collections;
import java.util.List;
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
import skilltest.bookstore.dto.CustomerDto;
import skilltest.bookstore.dto.FullNameDto;
import skilltest.bookstore.model.Customer;
import skilltest.bookstore.model.FullName;
import skilltest.bookstore.repository.CustomerRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerDto customerDto;

    private Customer customer;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                           .id(1L)
                           .fullName(FullName.builder()
                                             .id(1L)
                                             .firstName("a first name")
                                             .lastName("a last name")
                                             .build())
                           .email("test@example.be")
                           .build();
        customerDto = CustomerDto.builder()
                                 .id(1L)
                                 .fullName(FullNameDto.builder()
                                                      .firstName("a first name")
                                                      .lastName("a last name")
                                                      .build())
                                 .email("test@example.be")
                                 .build();
    }

    @Test
    @WithMockUser
    void getAllCustomers_withCustomer_returnsListOfCustomer() throws Exception {
        customerRepository.save(customer);
        List<CustomerDto> customerList = Collections.singletonList(customerDto);

        mockMvc.perform(get("/customers"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.size()", is(customerList.size())))
               .andExpect(jsonPath("$[0].email", is(customerDto.getEmail())));
    }

    @Test
    @WithMockUser
    void getCustomerByEmail_withCustomer_returnsCustomer() throws Exception {
        customerRepository.save(customer);

        mockMvc.perform(get("/customers/by-email").param("email", "test@example.be"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.email", is(customerDto.getEmail())));
    }

    @Test
    @WithMockUser
    void getCustomerById_withCustomer_returnsCustomer() throws Exception {
        customerRepository.save(customer);

        mockMvc.perform(get("/customers/{id}", 1L))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is((int) customerDto.getId())))
               .andExpect(jsonPath("$.email", is(customerDto.getEmail())));
    }

    @Test
    @WithMockUser
    void createCustomer_withValidCustomer_returnsCreatedCustomer() throws Exception {
        mockMvc.perform(post("/customers").contentType(MediaType.APPLICATION_JSON)
                                          .content(objectMapper.writeValueAsString(customerDto)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id", is((int) customerDto.getId())))
               .andExpect(jsonPath("$.email", is(customerDto.getEmail())));
    }
}
