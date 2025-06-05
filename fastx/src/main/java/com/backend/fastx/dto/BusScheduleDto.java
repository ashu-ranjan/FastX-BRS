package com.backend.fastx.dto;

import java.time.Duration;
import java.time.LocalTime;

public class BusScheduleDto {
    private int scheduleId;
    private String busName;
    private String busType;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Duration duration;
    private double fare;

    public BusScheduleDto(int scheduleId, String busName, String busType, LocalTime departureTime, LocalTime arrivalTime,Duration duration, double fare) {
        this.scheduleId = scheduleId;
        this.busName = busName;
        this.busType = busType;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
        this.duration = duration;
        this.fare = fare;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusType() {
        return busType;
    }

    public void setBusType(String busType) {
        this.busType = busType;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalTime departureTime) {
        this.departureTime = departureTime;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public int getScheduleId() {
        return scheduleId;
    }

    public void setScheduleId(int scheduleId) {
        this.scheduleId = scheduleId;
    }
}
