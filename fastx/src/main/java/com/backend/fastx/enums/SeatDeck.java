package com.backend.fastx.enums;

import com.backend.fastx.exception.InvalidSeatTypeException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum SeatDeck {
    UPPER,
    LOWER;

    @JsonCreator
    public static SeatDeck parse(String value){
        for (SeatDeck seatDeck : SeatDeck.values()){
            if(seatDeck.name().equalsIgnoreCase(value))
                return seatDeck;
        }
        throw new InvalidSeatTypeException("Invalid Seat Type Value : " + value + " !! [Allowed: UPPER" + "    LOWER");
    }
}
