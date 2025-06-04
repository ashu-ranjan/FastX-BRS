package com.backend.fastx.utility;

import com.backend.fastx.exception.InvalidInputException;
import com.backend.fastx.model.BusOperator;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

@Component
public class BusOperatorUtility {

    public boolean isValidEmail(String email){
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }

    public boolean isValidContact(String contact){
        return contact != null && Pattern.matches("\\d{10}", contact);
    }

    public void validateBusOperator(BusOperator busOperator){
        if (busOperator.getName() == null || busOperator.getName().trim().isEmpty()) {
            throw new InvalidInputException("Name is required.");
        }
        if (!isValidEmail(busOperator.getEmail())) {
            throw new InvalidInputException("Invalid email format.");
        }
        if (!isValidContact(busOperator.getContact())) {
            throw new InvalidInputException("Contact must be 10 digits.");
        }
        if (busOperator.getCompany() == null) {
            throw new InvalidInputException("Company is required.");
        }
    }
}
