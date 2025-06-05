package com.backend.fastx.controller;

import com.backend.fastx.dto.PaymentRequestDTO;
import com.backend.fastx.dto.PaymentResponseDTO;
import com.backend.fastx.model.Payment;
import com.backend.fastx.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class PaymentController {

    @Autowired private PaymentService paymentService;

    @PostMapping("/fastx/api/payment/make-payment")
    public ResponseEntity<?> makePayment(@RequestParam int bookingId,
                                         @RequestBody PaymentRequestDTO requestDTO,
                                         Principal principal){
        String username = principal.getName();
        PaymentResponseDTO response = paymentService.makePayment(bookingId, requestDTO, username);
        return ResponseEntity.ok(response);
    }

}
