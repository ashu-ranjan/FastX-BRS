package com.backend.fastx.model;

import com.backend.fastx.enums.RefundStatus;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "cancellation")
public class Cancellation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "cancel_date")
    private LocalDate cancelDate;

    @Column(name = "refund_amount")
    private double refundAmount;

    @Enumerated(EnumType.STRING) // To store enum value as string (PROCESSED, PROCESSING, etc.)
    @Column(name = "refund_status")
    private RefundStatus refundStatus;

    private String reason;

    @OneToOne
    private BookingDetails bookingDetails;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(LocalDate cancelDate) {
        this.cancelDate = cancelDate;
    }

    public double getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(double refundAmount) {
        this.refundAmount = refundAmount;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public BookingDetails getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(BookingDetails bookingDetails) {
        this.bookingDetails = bookingDetails;
    }
}
