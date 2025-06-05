package com.backend.fastx.enums;

import com.backend.fastx.exception.InvalidInputException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum ScheduleDays {

    MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY, DAILY;

    @JsonCreator
    public static ScheduleDays parse(String value){
        for (ScheduleDays day : ScheduleDays.values()){
            if (day.name().equalsIgnoreCase(value))
                return day;
        }
        throw new InvalidInputException("Invalid Allowed ScheduleDays Value !!");
    }
}
