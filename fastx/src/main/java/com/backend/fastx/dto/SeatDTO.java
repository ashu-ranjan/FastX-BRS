package com.backend.fastx.dto;

import com.backend.fastx.model.Seat;

public class SeatDTO {
    private boolean isActive;
    private double price;

    public SeatDTO() {}
    public SeatDTO(Seat seat) {
        this.price = seat.getPrice();
        this.isActive = seat.isActive();
    }

    public boolean isActive() {
        return isActive;
    }

    public double getPrice() {
        return price;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
