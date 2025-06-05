package com.backend.fastx.service;

import com.backend.fastx.enums.PaymentMethod;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentGatewayService {

    /* This a dummy gateway service to get payment status */
    public boolean processPayment(PaymentMethod method, double amount){
        if (amount <= 0) return false;
        return new Random().nextInt(100) >= 10; // 90% success chance using this
    }
}
