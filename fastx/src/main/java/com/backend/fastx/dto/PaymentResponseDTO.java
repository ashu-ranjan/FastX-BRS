package com.backend.fastx.dto;

import com.backend.fastx.enums.PaymentStatus;

public class PaymentResponseDTO {

    private int paymentId;
    private PaymentStatus paymentStatus;
    private double amount;

    public PaymentResponseDTO(int paymentId, PaymentStatus paymentStatus, double amount) {
        this.paymentId = paymentId;
        this.paymentStatus = paymentStatus;
        this.amount = amount;
    }

    public int getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(int paymentId) {
        this.paymentId = paymentId;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
