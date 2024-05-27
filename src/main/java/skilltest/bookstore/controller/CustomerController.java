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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import skilltest.bookstore.dto.CustomerDto;
import skilltest.bookstore.service.CustomerService;
import skilltest.bookstore.validator.ValidEmail;


@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    @PreAuthorize("hasRole('USER')")
    public List<CustomerDto> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @GetMapping("/by-email")
    @PreAuthorize("hasRole('USER')")
    public CustomerDto getCustomerByEmail(@RequestParam @ValidEmail String email) {
        return customerService.getCustomer(email);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public CustomerDto getCustomerById(@PathVariable long id) {
        return customerService.getCustomer(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('USER')")
    public CustomerDto createCustomer(@RequestBody @Valid CustomerDto customerDto) {
        return customerService.createCustomer(customerDto);
    }
}