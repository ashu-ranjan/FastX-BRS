package com.backend.fastx.dto;


import org.springframework.stereotype.Component;

import java.util.List;

public class BookingRequestDTO {
    private int customerId;
    private int scheduleId;
    private List<PassengerDTO> passengers;

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }

    public List<PassengerDTO> getPassengers() {
        return passengers;
    }

    public void setPassengers(List<PassengerDTO> passengers) {
        this.passengers = passengers;
    }

}
