package com.backend.fastx.enums;

import com.backend.fastx.exception.InvalidSeatTypeException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum SeatType {
    WINDOW,
    MIDDLE,
    AISLE;


    @JsonCreator
    public static SeatType parse(String value){
        for (SeatType seatType : SeatType.values()){
            if(seatType.name().equalsIgnoreCase(value))
                return seatType;
        }
        throw new InvalidSeatTypeException("Invalid Seat Type Value : " + value + " !! [Allowed: WINDOW" +
                                                                                            "    AISLE" +
                                                                                            "    MIDDLE]");
    }
}
