package skilltest.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import jakarta.persistence.EntityNotFoundException;
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
import skilltest.bookstore.dto.CustomerDto;
import skilltest.bookstore.dto.FullNameDto;
import skilltest.bookstore.model.Customer;
import skilltest.bookstore.model.FullName;
import skilltest.bookstore.repository.CustomerRepository;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CustomerService customerService;

    private Customer customer;
    private CustomerDto customerDto;

    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                           .id(1L)
                           .fullName(FullName.builder()
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
    void getAllCustomers_customer_returnsCustomers() {
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);

        List<CustomerDto> customers = customerService.getAllCustomers();

        assertNotNull(customers);
        assertEquals(1, customers.size());
        assertEquals(customerDto, customers.get(0));
        verify(customerRepository).findAll();
    }

    @Test
    void getCustomerById_exists_returnsCustomer() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);

        CustomerDto foundCustomer = customerService.getCustomer(1L);

        assertNotNull(foundCustomer);
        assertEquals(customerDto, foundCustomer);
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerById_doesntExist_throwsNotFound() {
        when(customerRepository.findById(anyLong())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> customerService.getCustomer(1L));

        assertEquals("Customer with id 1 not found", thrown.getMessage());
        verify(customerRepository).findById(1L);
    }

    @Test
    void getCustomerByEmail_exists_returnsCustomer() {
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(Optional.of(customer));
        when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);

        CustomerDto foundCustomer = customerService.getCustomer("test@example.com");

        assertNotNull(foundCustomer);
        assertEquals(customerDto, foundCustomer);
        verify(customerRepository).findCustomerByEmail("test@example.com");
    }

    @Test
    void getCustomerByEmail_doesntExist_throwsNotFound() {
        when(customerRepository.findCustomerByEmail(anyString())).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> customerService.getCustomer("test@example.com"));

        assertEquals("Customer with email test@example.com not found", thrown.getMessage());
        verify(customerRepository).findCustomerByEmail("test@example.com");
    }

    @Test
    void createCustomer() {
        when(modelMapper.map(customerDto, Customer.class)).thenReturn(customer);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);
        when(modelMapper.map(customer, CustomerDto.class)).thenReturn(customerDto);

        CustomerDto createdCustomer = customerService.createCustomer(customerDto);

        assertNotNull(createdCustomer);
        assertEquals(customerDto, createdCustomer);
        verify(customerRepository).save(customer);
    }
}
