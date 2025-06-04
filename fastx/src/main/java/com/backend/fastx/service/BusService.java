package com.backend.fastx.service;

import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Bus;
import com.backend.fastx.model.BusOperator;
import com.backend.fastx.repository.BusOperatorRepository;
import com.backend.fastx.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BusService {

    private final BusRepository busRepository;
    private final BusOperatorRepository busOperatorRepository;

    @Autowired
    public BusService(BusRepository busRepository, BusOperatorRepository busOperatorRepository) {
        this.busRepository = busRepository;
        this.busOperatorRepository = busOperatorRepository;
    }
    public Bus addBus(Bus bus, String username) {

        // Fetch operator by username (having one-to-one with User)
        BusOperator operator = busOperatorRepository.findByUserUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("No BusOperator found for user: " + username));

        // Set the operator to this bus
        bus.setBusOperator(operator);

        // Save and return
        return busRepository.save(bus);
    }
}
