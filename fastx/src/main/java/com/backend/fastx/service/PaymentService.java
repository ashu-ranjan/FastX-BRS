package com.backend.fastx.service;

import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.backend.fastx.dto.PaymentRequestDTO;
import com.backend.fastx.dto.PaymentResponseDTO;
import com.backend.fastx.enums.BookingStatus;
import com.backend.fastx.enums.PaymentStatus;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.BookedSeat;
import com.backend.fastx.model.Booking;
import com.backend.fastx.model.Customer;
import com.backend.fastx.model.Payment;
import com.backend.fastx.model.Seat;
import com.backend.fastx.repository.BookedSeatRepository;
import com.backend.fastx.repository.BookingRepository;
import com.backend.fastx.repository.CustomerRepository;
import com.backend.fastx.repository.PaymentRepository;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final PaymentGatewayService paymentGatewayService;
    private final BookedSeatRepository bookedSeatRepository;
    
    Logger logger = LoggerFactory.getLogger(PaymentService.class);

    public PaymentService(PaymentRepository paymentRepository,
                          CustomerRepository customerRepository,
                          BookingRepository bookingRepository,
                          PaymentGatewayService paymentGatewayService,
                          BookedSeatRepository bookedSeatRepository) {
        this.paymentRepository = paymentRepository;
        this.customerRepository = customerRepository;
        this.bookingRepository = bookingRepository;
        this.paymentGatewayService = paymentGatewayService;
        this.bookedSeatRepository = bookedSeatRepository;
    }

    /**
     * Processes a payment for a booking.
     *
     * @param bookingId  The ID of the booking for which the payment is being made.
     * @param requestDTO The payment request details including payment method.
     * @param email      The email of the customer making the payment.
     * @return A PaymentResponseDTO containing payment details.
     * @throws ResourceNotFoundException if the customer or booking is not found, or if unauthorized access is attempted.
     */
    public PaymentResponseDTO makePayment(int bookingId,
                                          PaymentRequestDTO requestDTO,
                                          String email) {

        // Get customer
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Customer not Found"));
        logger.info("Processing payment for booking ID: {}", bookingId);

        // get booking details
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not Found"));
        logger.info("Booking found for customer: {}", booking.getCustomer().getEmail());

        // Check booking done by customer only
        if (!booking.getCustomer().getEmail().equals(email))
            throw new ResourceNotFoundException("Unauthorized Access to booking");

        double amount = booking.getTotalFare();
        logger.info("Total fare for booking ID {}: {}", bookingId, amount);

        // Mock gateway to get payment status
        boolean success = paymentGatewayService.processPayment(requestDTO.getPaymentMethod(), amount);
        logger.info("Payment processing result for booking ID {}: {}", bookingId, success ? "SUCCESS" : "FAILED");

        Payment payment = new Payment();


        payment.setAmountPaid(booking.getTotalFare());
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(requestDTO.getPaymentMethod());
        payment.setBooking(booking);
        logger.info("Payment object created for booking ID: {}", bookingId);

        if (success){
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            booking.setStatus(BookingStatus.CONFIRMED);
        } else {
            payment.setPaymentStatus(PaymentStatus.FAILED);
            booking.setStatus(BookingStatus.PENDING);

            List<BookedSeat> bookedSeats = bookedSeatRepository.findByBooking(booking);
            for (BookedSeat bookedSeat : bookedSeats){
                Seat seat = bookedSeat.getSeat();
                seat.setActive(true); // to revert the seat from inactive to active
            }
        }
        logger.info("Payment status set to: {}", payment.getPaymentStatus());

        paymentRepository.save(payment);
        bookingRepository.save(booking);

        return new PaymentResponseDTO(payment.getId(), payment.getPaymentStatus(), payment.getAmountPaid());
    }
}
