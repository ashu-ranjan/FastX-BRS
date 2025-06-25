package com.backend.fastx.dto;

public class CancelRequestDTO {
    private int id;
    private String reason;
    private String status;
    private int bookingId;
    private int bookingDetailsId;
    private String passengerName;
    private String customerName;
    private String customerEmail;

    public CancelRequestDTO(int id,
                            String reason,
                            String status,
                            int bookingId,
                            int bookingDetailsId,
                            String passengerName,
                            String customerName,
                            String customerEmail) {
        this.id = id;
        this.reason = reason;
        this.status = status;
        this.bookingId = bookingId;
        this.bookingDetailsId = bookingDetailsId;
        this.passengerName = passengerName;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public int getBookingDetailsId() {
        return bookingDetailsId;
    }

    public void setBookingDetailsId(int bookingDetailsId) {
        this.bookingDetailsId = bookingDetailsId;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }
}
