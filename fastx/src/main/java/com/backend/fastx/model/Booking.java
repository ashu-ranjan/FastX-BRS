package com.backend.fastx.model;

import com.backend.fastx.enums.BookingStatus;
import jakarta.persistence.*;

import java.sql.Timestamp;
import java.time.LocalDate;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "booked_on")
    private Timestamp bookedOn;

    @Column(name = "journey_date")
    private LocalDate journeyDate;

    @Column(name = "total_seat")
    private int totalSeat;

    @Column(name = "total_fare")
    private double totalFare;

    @Enumerated(EnumType.STRING) // To store enum value as string (CONFIRMED, CANCELLED, PENDING)
    private BookingStatus status;


    @ManyToOne
    private Customer customer; // One customer can have many bookings
    @ManyToOne
    private Schedule schedule; // one schedule can have many bookings

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getBookedOn() {
        return bookedOn;
    }

    public void setBookedOn(Timestamp bookedOn) {
        this.bookedOn = bookedOn;
    }

    public int getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(int totalSeat) {
        this.totalSeat = totalSeat;
    }

    public double getTotalFare() {
        return totalFare;
    }

    public void setTotalFare(double totalFare) {
        this.totalFare = totalFare;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public void setSchedule(Schedule schedule) {
        this.schedule = schedule;
    }

    public LocalDate getJourneyDate() {
        return journeyDate;
    }

    public void setJourneyDate(LocalDate journeyDate) {
        this.journeyDate = journeyDate;
    }

}
