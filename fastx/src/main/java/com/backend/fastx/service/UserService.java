package com.backend.fastx.service;

import com.backend.fastx.model.User;
import com.backend.fastx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
}
