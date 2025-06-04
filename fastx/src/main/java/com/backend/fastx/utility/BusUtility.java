package com.backend.fastx.utility;

import com.backend.fastx.exception.InvalidInputException;
import com.backend.fastx.model.Bus;
import org.springframework.stereotype.Component;

@Component
public class BusUtility {

    public void validateBus(Bus bus){
        if (bus.getBusName() == null || bus.getBusName().trim().isEmpty()) {
            throw new InvalidInputException("Bus name is required.");
        }
        if (bus.getBusNumber() == null || bus.getBusNumber().trim().isEmpty()) {
            throw new InvalidInputException("Bus number is required.");
        }
        if (bus.getBusType() == null) {
            throw new InvalidInputException("Bus type is required.");
        }
        if (bus.getCapacity() == 0) {
            throw new InvalidInputException("Bus capacity can't zero");
        }
    }
}
