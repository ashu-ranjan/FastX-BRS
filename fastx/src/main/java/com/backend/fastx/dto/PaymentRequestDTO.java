package com.backend.fastx.dto;

import com.backend.fastx.enums.PaymentMethod;
import com.backend.fastx.enums.PaymentStatus;

public class PaymentRequestDTO {
    private PaymentMethod paymentMethod; // UPI, CARD, etc.
    private PaymentStatus paymentStatus; // PROCESSING or SUCCESS

    public PaymentRequestDTO() {
        // Default constructor
    }

    public PaymentRequestDTO(PaymentMethod paymentMethod, PaymentStatus paymentStatus) {
        this.paymentMethod = paymentMethod;
        this.paymentStatus = paymentStatus;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public PaymentStatus getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(PaymentStatus paymentStatus) {
        this.paymentStatus = paymentStatus;
    }
}
