package com.backend.fastx.model;

import com.backend.fastx.enums.BusType;
import jakarta.persistence.*;

@Entity
@Table(name = "bus")
public class Bus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "bus_name")
    private String busName;

    @Column(name = "bus_number")
    private String busNumber;

    @Enumerated(EnumType.STRING) // To store enum value as string (AC_SLEEPER, NON_AC_SLEEPER, etc.)
    @Column(name = "bus_type")
    private BusType busType;

    private int capacity;

    @Column(length = 1000)
    private String amenities;

    private String imageUrl;

    @ManyToOne
    private BusOperator busOperator;

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusName() {
        return busName;
    }

    public void setBusName(String busName) {
        this.busName = busName;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public BusType getBusType() {
        return busType;
    }

    public void setBusType(BusType busType) {
        this.busType = busType;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getAmenities() {
        return amenities;
    }

    public void setAmenities(String amenities) {
        this.amenities = amenities;
    }

    public BusOperator getBusOperator() {
        return busOperator;
    }

    public void setBusOperator(BusOperator busOperator) {
        this.busOperator = busOperator;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
