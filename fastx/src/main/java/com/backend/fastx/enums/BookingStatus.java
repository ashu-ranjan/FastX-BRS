package com.backend.fastx.enums;

import com.backend.fastx.exception.InvalidInputException;
import com.fasterxml.jackson.annotation.JsonCreator;
import org.hibernate.sql.results.graph.collection.internal.BagInitializer;

import java.awt.print.Book;

public enum BookingStatus {
    CONFIRMED,
    CANCELLED,
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
