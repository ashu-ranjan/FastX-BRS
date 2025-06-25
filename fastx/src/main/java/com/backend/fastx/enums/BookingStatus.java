package com.backend.fastx.enums;

import com.backend.fastx.exception.InvalidInputException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum BookingStatus {
    CONFIRMED,
    CANCELLED,
    PARTIALLY_CANCELLED,
    PENDING;

    @JsonCreator
    public BookingStatus parse(String value){
        for (BookingStatus status : BookingStatus.values()){
            if (status.name().equalsIgnoreCase(value))
                return status;
        }
        throw new InvalidInputException("Invalid Input");
    }

}
