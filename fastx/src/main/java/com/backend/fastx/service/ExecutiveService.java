package com.backend.fastx.service;

import com.backend.fastx.enums.Role;
import com.backend.fastx.model.Executive;
import com.backend.fastx.model.User;
import com.backend.fastx.repository.ExecutiveRepository;
import com.backend.fastx.utility.ExecutiveUtility;
import org.springframework.stereotype.Service;

@Service
public class ExecutiveService {

    private final ExecutiveRepository executiveRepository;
    private final ExecutiveUtility executiveUtility;
    private final UserService userService;

    public ExecutiveService(ExecutiveRepository executiveRepository, ExecutiveUtility executiveUtility, UserService userService) {
        this.executiveRepository = executiveRepository;
        this.executiveUtility = executiveUtility;
        this.userService = userService;
    }

    public Executive addExecutive(Executive executive) {

        executiveUtility.validateExecutive(executive);

        // Take user out of this Executive object
        User user = executive.getUser();

        // Give role to the user
        user.setRole(Role.EXECUTIVE);

        // Save this user in db
        user = userService.register(user, executive.getEmail());

        // Attach this user back to Executive
        executive.setUser(user);

        // Save executive in DB
        return executiveRepository.save(executive);
    }
}
