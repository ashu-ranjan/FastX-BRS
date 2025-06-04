package com.backend.fastx.enums;

import com.backend.fastx.exception.GenderInvalidException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Gender {
    MALE,
    FEMALE,
    OTHER;

    @JsonCreator
    public static Gender parse(String value){
        for (Gender gender : Gender.values()){
            if(gender.name().equalsIgnoreCase(value))
                return gender;
        }
        throw new GenderInvalidException("Invalid Gender Value : " + value + " !! [Allowed: MALE, FEMALE, OTHER]");
    }
}
