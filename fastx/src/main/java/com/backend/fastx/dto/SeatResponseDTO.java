package com.backend.fastx.dto;

import com.backend.fastx.enums.SeatType;

public class SeatResponseDTO {
    private int seatId;
    private String seatNumber;
    private SeatType seatType;
    private boolean isActive;

    public SeatResponseDTO(int seatId, String seatNumber, SeatType seatType, boolean isActive) {
        this.seatId = seatId;
        this.seatNumber = seatNumber;
        this.seatType = seatType;
        this.isActive = isActive;
    }

    public int getSeatId() {
        return seatId;
    }

    public void setSeatId(int seatId) {
        this.seatId = seatId;
    }

    public String getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(String seatNumber) {
        this.seatNumber = seatNumber;
    }

    public SeatType getSeatType() {
        return seatType;
    }

    public void setSeatType(SeatType seatType) {
        this.seatType = seatType;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
