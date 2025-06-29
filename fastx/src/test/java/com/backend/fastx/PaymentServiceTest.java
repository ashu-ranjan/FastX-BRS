package com.backend.fastx;

import com.backend.fastx.dto.PaymentRequestDTO;
import com.backend.fastx.dto.PaymentResponseDTO;
import com.backend.fastx.enums.BookingStatus;
import com.backend.fastx.enums.PaymentMethod;
import com.backend.fastx.enums.PaymentStatus;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.*;
import com.backend.fastx.repository.*;
import com.backend.fastx.service.PaymentGatewayService;
import com.backend.fastx.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PaymentServiceTest {

    @InjectMocks
    private PaymentService paymentService;

    @Mock private PaymentRepository paymentRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private BookingRepository bookingRepository;
    @Mock private PaymentGatewayService paymentGatewayService;
    @Mock private BookedSeatRepository bookedSeatRepository;

    private Booking booking;
    private Customer customer;
    private PaymentRequestDTO requestDTO;

    @BeforeEach
    public void init() {
        customer = new Customer();
        customer.setId(1);
        customer.setEmail("user@example.com");

        booking = new Booking();
        booking.setId(101);
        booking.setCustomer(customer);
        booking.setTotalFare(500);
        booking.setStatus(BookingStatus.PENDING);

        requestDTO = new PaymentRequestDTO();
        requestDTO.setPaymentMethod(PaymentMethod.UPI);
    }

    @Test
    public void testMakePayment_Success() {
        when(customerRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(customer));

        when(bookingRepository.findById(101))
                .thenReturn(Optional.of(booking));

        when(paymentGatewayService.processPayment(PaymentMethod.UPI, 500))
                .thenReturn(true);

        PaymentResponseDTO response = paymentService.makePayment(101, requestDTO, "user@example.com");

        assertNotNull(response);
        assertEquals(PaymentStatus.SUCCESS, response.getPaymentStatus());
        assertEquals(500, response.getAmount());
        assertEquals(BookingStatus.CONFIRMED, booking.getStatus());

        verify(paymentRepository).save(any(Payment.class));
        verify(bookingRepository).save(booking);
    }

    @Test
    public void testMakePayment_Failure() {
        when(customerRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(customer));

        when(bookingRepository.findById(101))
                .thenReturn(Optional.of(booking));

        when(paymentGatewayService.processPayment(PaymentMethod.UPI, 500))
                .thenReturn(false);

        BookedSeat bookedSeat = new BookedSeat();
        Seat seat = new Seat();
        seat.setActive(false);
        bookedSeat.setSeat(seat);

        when(bookedSeatRepository.findByBooking(booking)).thenReturn(List.of(bookedSeat));

        PaymentResponseDTO response = paymentService.makePayment(101, requestDTO, "user@example.com");

        assertNotNull(response);
        assertEquals(PaymentStatus.FAILED, response.getPaymentStatus());
        assertEquals(BookingStatus.PENDING, booking.getStatus());
        assertTrue(seat.isActive()); // Seat reactivated
    }

    @Test
    public void testMakePayment_CustomerNotFound() {
        when(customerRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.makePayment(101, requestDTO, "user@example.com"));
    }

    @Test
    public void testMakePayment_BookingNotFound() {
        when(customerRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(customer));

        when(bookingRepository.findById(101))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.makePayment(101, requestDTO, "user@example.com"));
    }

    @Test
    public void testMakePayment_UnauthorizedAccess() {
        Customer anotherCustomer = new Customer();
        anotherCustomer.setEmail("hacker@example.com");
        booking.setCustomer(anotherCustomer);

        when(customerRepository.findByEmail("user@example.com"))
                .thenReturn(Optional.of(customer));

        when(bookingRepository.findById(101))
                .thenReturn(Optional.of(booking));

        assertThrows(ResourceNotFoundException.class,
                () -> paymentService.makePayment(101, requestDTO, "user@example.com"));
    }
}
