package com.backend.fastx.service;

import com.backend.fastx.enums.Role;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Customer;
import com.backend.fastx.model.User;
import com.backend.fastx.repository.CustomerRepository;
import com.backend.fastx.utility.CustomerUtility;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerUtility customerUtility;
    private final UserService userService;

    public CustomerService(CustomerRepository customerRepository, CustomerUtility customerUtility, UserService userService) {
        this.customerRepository = customerRepository;
        this.customerUtility = customerUtility;
        this.userService = userService;
    }

    public Customer addCustomer(Customer customer) {
        customerUtility.validateCustomer(customer);

        // Take user out of this customer object
        User user = customer.getUser();

        // Give role to the user
        user.setRole(Role.CUSTOMER);

        // Save this User in the DB
        user = userService.register(user, customer.getEmail());

        // Attach this user back to customer
        customer.setUser(user);

        // Save Customer in DB
        return customerRepository.save(customer);
    }

    public int getCustomerIdFromEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
        return customer.getId();
    }
}
