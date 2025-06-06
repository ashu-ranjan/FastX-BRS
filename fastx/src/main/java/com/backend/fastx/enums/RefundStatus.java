package com.backend.fastx.enums;

import com.backend.fastx.exception.InvalidInputException;
import com.fasterxml.jackson.annotation.JsonCreator;

public enum RefundStatus {
    REQUESTED, APPROVED, REJECTED;

    @JsonCreator
    public static RefundStatus parse(String value) {
        for (RefundStatus refundStatus : RefundStatus.values()) {
            if (refundStatus.name().equalsIgnoreCase(value)) {
                return refundStatus;
            }
        }
        throw new InvalidInputException("Invalid refund status Value: " + value + " !! [Allowed: REQUESTED, APPROVED, REJECTED]");
    }
}
