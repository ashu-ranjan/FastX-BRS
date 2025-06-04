package com.backend.fastx.enums;

import com.backend.fastx.exception.InvalidInputException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Day {

    EVERYDAY,
    MON_WED_FRI,
    SAT_SUN,
    TUE_THU_SAT,
    SUNDAY;

    @JsonCreator
    public static Day parse(String value){
        for (Day day : Day.values()){
            if (day.name().equalsIgnoreCase(value))
                return day;
        }
        throw new InvalidInputException("Invalid Allowed Day Value !!");
    }
}
