package com.backend.fastx.service;

import com.backend.fastx.enums.PaymentMethod;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentGatewayService {

    /**
     * Simulates a payment processing service.
     * In a real-world application, this would interact with a payment gateway API.
     *
     * @param method  The payment method to use (e.g., CREDIT_CARD, PAYPAL).
     * @param amount  The amount to be charged.
     * @return true if the payment was successful, false otherwise.
     */
    /* This a dummy gateway service to get payment status */
    public boolean processPayment(PaymentMethod method, double amount){
        if (amount <= 0) return false;
        return new Random().nextInt(100) >= 10; // 90% success chance using this
    }
}
