package com.backend.fastx.model;

import com.backend.fastx.enums.SeatDeck;
import com.backend.fastx.enums.SeatType;
import jakarta.persistence.*;

@Entity
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "seat_number")
    private String seatNumber;

    @Enumerated(EnumType.STRING) // To store enum value as string (WINDOW, MIDDLE, AISLE, etc.)
    @Column(name = "seat_type")
    private SeatType seatType;

    @Enumerated(EnumType.STRING) // To store enum value as string (UPPER, LOWER)
    @Column(name = "seat_deck")
    private SeatDeck seatDeck;

    private double price; // Assuming you might want to add a price for the seat

    private boolean isActive;// later after booking it shows false without booking its shows true

    @ManyToOne
    private Bus bus;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public SeatDeck getSeatDeck() {
        return seatDeck;
    }

    public void setSeatDeck(SeatDeck seatDeck) {
        this.seatDeck = seatDeck;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Bus getBus() {
        return bus;
    }

    public void setBus(Bus bus) {
        this.bus = bus;
    }
    public double getPrice() {
        return price;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
