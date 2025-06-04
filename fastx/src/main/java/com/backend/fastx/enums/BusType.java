package com.backend.fastx.enums;

import com.backend.fastx.exception.InvalidBusTypeException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum BusType {
    AC_SLEEPER,
    NON_AC_SLEEPER,
    AC_SEMI_SLEEPER,
    NON_AC_SEMI_SLEEPER,
    AC_SEATER,
    NON_AC_SEATER;

    @JsonCreator
    public static BusType parse(String value){
        for (BusType busType : BusType.values()){
            if(busType.name().equalsIgnoreCase(value))
                return busType;
        }
        throw new InvalidBusTypeException("Invalid Bus Type Value : " + value + " !! [Allowed: AC_SLEEPER,\n" +
                                                                                        "    NON_AC_SLEEPER,\n" +
                                                                                        "    AC_SEMI_SLEEPER,\n" +
                                                                                        "    NON_AC_SEMI_SLEEPER,\n" +
                                                                                        "    AC_SEATER,\n" +
                                                                                        "    NON_AC_SEATER]");
    }
}
