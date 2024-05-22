package skilltest.bookstore.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import skilltest.bookstore.dto.CustomerDto;
import skilltest.bookstore.model.Customer;
import skilltest.bookstore.repository.CustomerRepository;
import skilltest.bookstore.validator.ValidEmail;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private static final String CUSTOMER_NOT_FOUND = "Customer with %s %s not found";
    private final CustomerRepository customerRepository;
    private final ModelMapper modelMapper;

    public List<CustomerDto> getAllCustomers() {
        return customerRepository.findAll()
                                 .stream()
                                 .map(this::toDto)
                                 .collect(Collectors.toList());
    }

    public CustomerDto getCustomer(long id) {
        return customerRepository.findById(id)
                                 .map(this::toDto)
                                 .orElseThrow(() -> new EntityNotFoundException(CUSTOMER_NOT_FOUND.formatted("id", id)));
    }

    public CustomerDto getCustomer(@ValidEmail String email) {
        return customerRepository.findCustomerByEmail(email)
                                 .map(this::toDto)
                                 .orElseThrow(() -> new EntityNotFoundException(CUSTOMER_NOT_FOUND.formatted("email", email)));
    }


    public CustomerDto createCustomer(CustomerDto customerDto) {
        Customer customer = fromDto(customerDto);
        Customer savedCustomer = customerRepository.save(customer);
        return toDto(savedCustomer);
    }

    private CustomerDto toDto(Customer customer) {
        return modelMapper.map(customer, CustomerDto.class);
    }

    private Customer fromDto(CustomerDto customerDto) {
        return modelMapper.map(customerDto, Customer.class);
    }
}