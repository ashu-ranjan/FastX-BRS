package com.backend.fastx.service;

import com.backend.fastx.enums.Role;
import com.backend.fastx.model.BusOperator;
import com.backend.fastx.model.User;
import com.backend.fastx.repository.BusOperatorRepository;
import com.backend.fastx.utility.BusOperatorUtility;
import org.springframework.stereotype.Service;

@Service
public class BusOperatorService {

    private final BusOperatorRepository busOperatorRepository;
    private final BusOperatorUtility busOperatorUtility;
    private final UserService userService;

    public BusOperatorService(BusOperatorRepository busOperatorRepository, BusOperatorUtility busOperatorUtility, UserService userService) {
        this.busOperatorRepository = busOperatorRepository;
        this.busOperatorUtility = busOperatorUtility;
        this.userService = userService;
    }

    public BusOperator addBusOperator(BusOperator busOperator) {
        busOperatorUtility.validateBusOperator(busOperator);

        // Take user out of this customer object
        User user = busOperator.getUser();

        // Give role to the user
        user.setRole(Role.OPERATOR);

        // Save this User in the DB
        user = userService.register(user, busOperator.getEmail());

        // Attach this user back to busOperator
        busOperator.setUser(user);

        // Save busOperator in DB
        return busOperatorRepository.save(busOperator);
    }
}
