package com.backend.fastx.enums;

import com.backend.fastx.exception.RoleInvalidException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum Role {
    CUSTOMER,
    OPERATOR,
    EXECUTIVE,
    CEO;

    @JsonCreator
    public static Role parse(String value) {
        for (Role role : Role.values()) {
            if (role.name().equalsIgnoreCase(value)) {
                return role;
            }
        }
        throw new RoleInvalidException("Invalid Role Value: " + value + " !! [Allowed: CUSTOMER, OPERATOR, EXECUTIVE]");
    }
}
