package com.backend.fastx.utility;

import com.backend.fastx.exception.GenderInvalidException;
import com.backend.fastx.exception.InvalidInputException;
import com.backend.fastx.model.Customer;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component // <-- Register it as a Spring Bean
public class CustomerUtility {

    public boolean isValidEmail(String email){
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }

    public boolean isValidContact(String contact){
        return contact != null && Pattern.matches("\\d{10}", contact);
    }

    public void validateCustomer(Customer customer){
        if (customer.getName() == null || customer.getName().trim().isEmpty()) {
            throw new InvalidInputException("Name is required.");
        }
        if (!isValidEmail(customer.getEmail())) {
            throw new InvalidInputException("Invalid email format.");
        }
        if (!isValidContact(customer.getContact())) {
            throw new InvalidInputException("Contact must be 10 digits.");
        }
        if (customer.getGender() == null) {
            throw new GenderInvalidException("Gender must be provided.");
        }
    }

}
