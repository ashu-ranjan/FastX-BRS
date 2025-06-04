package com.backend.fastx.utility;

import com.backend.fastx.exception.InvalidInputException;
import com.backend.fastx.exception.UsernameAlreadyExistsException;
import com.backend.fastx.model.User;
import com.backend.fastx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserUtility {

    @Autowired
    private UserRepository userRepository;

    public boolean isValidUsername(String username) {
        return username != null && username.trim().length() >= 3;
    }

    public boolean isUsernameUnique(String username) {
        return username != null && !userRepository.existsByUsername(username);
    }

    public boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 8 &&
                password.matches(".*[A-Z].*") &&
                password.matches(".*\\d.*");
    }

    public void validateUser(User user) {
        if (!isValidUsername(user.getUsername())) {
            throw new InvalidInputException("Username must be at least 3 characters long.");
        }

        if (!isUsernameUnique(user.getUsername())) {
            throw new UsernameAlreadyExistsException("Username '" + user.getUsername() + "' already exists.");
        }

        if (!isValidPassword(user.getPassword())) {
            throw new InvalidInputException("Password must be at least 8 characters long, contain one uppercase letter and one number.");
        }

        if (user.getRole() == null) {
            throw new InvalidInputException("No role assigned to user.");
        }
    }

}
