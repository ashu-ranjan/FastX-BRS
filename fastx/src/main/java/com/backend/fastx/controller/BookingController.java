package com.backend.fastx.controller;

import com.backend.fastx.dto.BookingRequestDTO;
import com.backend.fastx.service.BookingService;
import com.backend.fastx.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/fastx/api")
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private CustomerService customerService;

    @PostMapping("/book")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO,
                                           Principal principal){
        int customerId = customerService.getCustomerIdFromEmail(principal.getName());
        bookingRequestDTO.setCustomerId(customerId);
        return ResponseEntity.ok(bookingService.createBooking(bookingRequestDTO));
    }
}
