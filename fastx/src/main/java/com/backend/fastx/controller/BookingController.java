package com.backend.fastx.controller;

import com.backend.fastx.dto.BookingRequestDTO;
import com.backend.fastx.model.Booking;
import com.backend.fastx.service.BookingService;
import com.backend.fastx.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/fastx/api")
@CrossOrigin(origins = "http://localhost:5173") 
public class BookingController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private CustomerService customerService;

    /**
     * @aim create a booking
     * @path: /fastx/api/book
     * @method: POST
     * @param bookingRequestDTO booking request data transfer object
     * @param principal authenticated user principal
     * @return response entity with booking details
     */

    @PostMapping("/book")
    public ResponseEntity<?> createBooking(@RequestBody BookingRequestDTO bookingRequestDTO,
            Principal principal) {
        int customerId = customerService.getCustomerIdFromEmail(principal.getName());
        bookingRequestDTO.setCustomerId(customerId);
        return ResponseEntity.ok(bookingService.createBooking(bookingRequestDTO));
    }

    /**
     * @aim get booking history for a customer
     * @path: /fastx/api/booking/history
     * @method: GET
     * @param principal
     * @param page
     * @param size
     * @return response entity with booking history
     * @throws ResourceNotFoundException if customer not found or no bookings exist
     */

    @GetMapping("/booking/history")
    public ResponseEntity<?> getBookingHistory(
            Principal principal,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        String email = principal.getName();
        Page<Booking> bookings = bookingService.getBookingsByCustomerEmail(email, page, size);
        return ResponseEntity.ok(bookings);
    }

    /**
     * @aim get booking details by booking ID
     * @path: /fastx/api/bookings/{id}
     * @method: GET
     * @param id booking ID
     * @param principal authenticated user principal
     * @return response entity with booking details
     * @throws ResourceNotFoundException if booking not found or does not belong to the user
     */

    @GetMapping("/bookings/{id}")
    public ResponseEntity<?> getBookingDetails(@PathVariable int id,
            Principal principal) {
        String email = principal.getName();
        Booking booking = bookingService.getBookingByIdAndUsername(id, email);
        return ResponseEntity.ok(booking);
    }

    /**
     * @aim get booking details by booking ID
     * @path: /fastx/api/booking-details/by-bookingId/{bookingId}
     * @method: GET
     * @param id booking ID
     * @return booking details
     * @throws ResourceNotFoundException if booking not found
     */
    @GetMapping("/booking-details/by-bookingId/{bookingId}")
    public ResponseEntity<?> getBookingDetailsById(@PathVariable int bookingId) {
        return ResponseEntity.ok(bookingService.getBookingDetailsByBookingId(bookingId));
    }

}
