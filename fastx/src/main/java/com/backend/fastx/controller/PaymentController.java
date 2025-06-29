package com.backend.fastx.controller;

import com.backend.fastx.dto.PaymentRequestDTO;
import com.backend.fastx.dto.PaymentResponseDTO;
import com.backend.fastx.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
public class PaymentController {

    @Autowired private PaymentService paymentService;

    /**
     * @aim Processes a payment for a booking.
     * @description This method allows a customer to make a payment for their booking.
     * @path /fastx/api/payment/make-payment
     * @method POST
     * @param bookingId The ID of the booking for which the payment is being made.
     * @param requestDTO The payment request details including payment method.  
     * @param principal The principal object representing the authenticated user.
     * @return ResponseEntity containing the payment response details.
     */

    @PostMapping("/fastx/api/payment/make-payment")
    public ResponseEntity<?> makePayment(@RequestParam int bookingId,
                                         @RequestBody PaymentRequestDTO requestDTO,
                                         Principal principal){
        String username = principal.getName();
        PaymentResponseDTO response = paymentService.makePayment(bookingId, requestDTO, username);
        return ResponseEntity.ok(response);
    }

}
