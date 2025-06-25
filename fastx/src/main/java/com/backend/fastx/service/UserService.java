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
