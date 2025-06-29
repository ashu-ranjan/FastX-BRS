package com.backend.fastx.service;

import com.backend.fastx.enums.Role;

import com.backend.fastx.model.User;
import com.backend.fastx.repository.BusOperatorRepository;
import com.backend.fastx.repository.CustomerRepository;
import com.backend.fastx.repository.ExecutiveRepository;
import com.backend.fastx.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CustomerRepository customerRepository;
    private final BusOperatorRepository busOperatorRepository;
    private final ExecutiveRepository executiveRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, CustomerRepository customerRepository, BusOperatorRepository busOperatorRepository, ExecutiveRepository executiveRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.customerRepository = customerRepository;
        this.busOperatorRepository = busOperatorRepository;
        this.executiveRepository = executiveRepository;
    }

    /**
     * Registers a new user by setting the username as email and encoding the password.
     *
     * @param user  The User object containing user details.
     * @param email The email to be used as the username.
     * @return The saved User object with encoded password.
     */

    public User register(User user, String email) {

        // Set username as email for all users
        user.setUsername(email.toLowerCase().trim());

        // encrypt the password
        String plainPassword = user.getPassword(); // <-- this gives you plain password
        String encodedPassword = passwordEncoder.encode(plainPassword);
        user.setPassword(encodedPassword); //<-- Now, User has encoded password

        // save user to DB
        return userRepository.save(user);
    }

    /**
     * Retrieves user information based on the username.
     *
     * @param username The username of the user whose information is to be retrieved.
     * @return An object containing user information, or null if the user does not exist.
     */

    public Object getUserInfo(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();
        Role role = user.getRole();
        return switch (role) {
            case CUSTOMER -> customerRepository.getCustomerByUsername(username);
            case OPERATOR -> busOperatorRepository.getBusOperatorByUsername(username);
            case EXECUTIVE -> executiveRepository.getExecutiveByUsername(username);
            default -> null;
        };
    }
}
