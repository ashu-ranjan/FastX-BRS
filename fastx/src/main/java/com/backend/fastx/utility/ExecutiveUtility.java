package com.backend.fastx.utility;

import com.backend.fastx.exception.InvalidInputException;
import com.backend.fastx.model.Executive;
import org.springframework.stereotype.Component;

@Component
public class ExecutiveUtility {

    public boolean isValidEmail(String email){
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$");
    }

    public void validateExecutive(Executive executive){
        if (executive.getName() == null || executive.getName().trim().isEmpty()) {
            throw new InvalidInputException("Name is required.");
        }
        if (!isValidEmail(executive.getEmail())) {
            throw new InvalidInputException("Invalid email format.");
        }
        if (executive.getDesignation() == null || executive.getDesignation().trim().isEmpty()) {
            throw new InvalidInputException("Designation is required.");
        }
    }
}
