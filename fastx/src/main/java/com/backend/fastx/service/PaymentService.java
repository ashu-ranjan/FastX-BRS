package com.backend.fastx.service;

import com.backend.fastx.dto.PaymentRequestDTO;
import com.backend.fastx.dto.PaymentResponseDTO;
import com.backend.fastx.enums.BookingStatus;
import com.backend.fastx.enums.PaymentStatus;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.*;
import com.backend.fastx.repository.BookedSeatRepository;
import com.backend.fastx.repository.BookingRepository;
import com.backend.fastx.repository.CustomerRepository;
import com.backend.fastx.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final CustomerRepository customerRepository;
    private final BookingRepository bookingRepository;
    private final PaymentGatewayService paymentGatewayService;
    private final BookedSeatRepository bookedSeatRepository;

    @Autowired
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

    public PaymentResponseDTO makePayment(int bookingId,
                                          PaymentRequestDTO requestDTO,
                                          String email) {

        // Get customer
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Customer not Found"));

        // get booking details
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(()-> new ResourceNotFoundException("Booking not Found"));

        // Check booking done by customer only
        if (!booking.getCustomer().getEmail().equals(email))
            throw new ResourceNotFoundException("Unauthorized Access to booking");

        double amount = booking.getTotalFare();

        // Mock gateway to get payment status
        boolean success = paymentGatewayService.processPayment(requestDTO.getPaymentMethod(), amount);

        Payment payment = new Payment();

        payment.setAmountPaid(booking.getTotalFare());
        payment.setPaymentDate(LocalDate.now());
        payment.setPaymentMethod(requestDTO.getPaymentMethod());
        payment.setBooking(booking);

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

        paymentRepository.save(payment);
        bookingRepository.save(booking);

        return new PaymentResponseDTO(payment.getId(), payment.getPaymentStatus(), payment.getAmountPaid());
    }
}
