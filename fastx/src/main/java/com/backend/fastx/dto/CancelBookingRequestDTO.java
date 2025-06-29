package com.backend.fastx.dto;

public class CancelBookingRequestDTO {
    private String reason;

    public CancelBookingRequestDTO() {
        // Default constructor
    }

    public CancelBookingRequestDTO(String reason) {
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
