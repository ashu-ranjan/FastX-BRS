package com.backend.fastx;

import com.backend.fastx.dto.BookingRequestDTO;
import com.backend.fastx.dto.PassengerDTO;
import com.backend.fastx.enums.BookingStatus;
import com.backend.fastx.enums.BusType;
import com.backend.fastx.enums.Gender;
import com.backend.fastx.enums.SeatDeck;
import com.backend.fastx.enums.SeatType;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.*;
import com.backend.fastx.repository.*;
import com.backend.fastx.service.BookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BookingServiceTest {

    @InjectMocks
    private BookingService bookingService;

    @Mock private BookingRepository bookingRepository;
    @Mock private ScheduleRepository scheduleRepository;
    @Mock private CustomerRepository customerRepository;
    @Mock private SeatRepository seatRepository;
    @Mock private BookingDetailsRepository bookingDetailsRepository;
    @Mock private UserRepository userRepository;

    private Customer customer;
    private Schedule schedule;
    private Seat seat;
    private Booking booking;
    private BookingRequestDTO request;

    @BeforeEach
    public void init() {
        customer = new Customer();
        customer.setId(1);
        customer.setName("John Doe");

        Bus bus = new Bus();
        bus.setId(1);
        bus.setBusName("FastX Travels");
        bus.setBusType(BusType.AC_SLEEPER);
        bus.setActive(true);
        bus.setBusNumber("DL-1-2345");
        bus.setCapacity(40);

        schedule = new Schedule();
        schedule.setId(101);
        schedule.setBus(bus);

        seat = new Seat();
        seat.setId(501);
        seat.setSeatNumber("A1");
        seat.setActive(true);
        seat.setSeatType(SeatType.WINDOW);
        seat.setSeatDeck(SeatDeck.LOWER);
        seat.setPrice(100);

        booking = new Booking();
        booking.setId(201);
        booking.setCustomer(customer);
        booking.setSchedule(schedule);
        booking.setBookedOn(new Timestamp(System.currentTimeMillis()));
        booking.setJourneyDate(LocalDate.now());
        booking.setStatus(BookingStatus.PENDING);

        PassengerDTO passenger = new PassengerDTO();
        passenger.setPassenger("John");
        passenger.setGender(Gender.MALE);
        passenger.setAge(25);
        passenger.setSeatId(seat.getId());
        passenger.setBoardingPoint("Delhi");
        passenger.setDroppingPoint("Jaipur");

        request = new BookingRequestDTO();
        request.setCustomerId(customer.getId());
        request.setScheduleId(schedule.getId());
        request.setJourneyDate(LocalDate.now());
        request.setPassengers(List.of(passenger));
    }

    @Test
    public void testCreateBooking_Success() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(scheduleRepository.findById(101)).thenReturn(Optional.of(schedule));
        when(seatRepository.findById(501)).thenReturn(Optional.of(seat));
        when(bookingRepository.save(any())).thenReturn(booking);
        when(bookingDetailsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        Booking result = bookingService.createBooking(request);

        assertEquals(customer, result.getCustomer());
        assertEquals(1, result.getTotalSeat());
        assertTrue(result.getTotalFare() > 0);
        verify(seatRepository).save(any(Seat.class));
        verify(bookingDetailsRepository).save(any(BookingDetails.class));
    }

    @Test
    public void testCreateBooking_InvalidCustomer() {
        when(customerRepository.findById(99)).thenReturn(Optional.empty());
        request.setCustomerId(99);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> bookingService.createBooking(request));
        assertEquals("Customer Not Found.", ex.getMessage());
    }

    @Test
    public void testCreateBooking_InvalidSchedule() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(scheduleRepository.findById(999)).thenReturn(Optional.empty());
        request.setScheduleId(999);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> bookingService.createBooking(request));
        assertEquals("Schedule Not Found.", ex.getMessage());
    }

    @Test
    public void testCreateBooking_SeatNotFound() {
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(scheduleRepository.findById(101)).thenReturn(Optional.of(schedule));
        when(seatRepository.findById(999)).thenReturn(Optional.empty());
        request.getPassengers().get(0).setSeatId(999);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> bookingService.createBooking(request));
        assertEquals("Seat Not Found.", ex.getMessage());
    }

    @Test
    public void testCreateBooking_SeatAlreadyBooked() {
        seat.setActive(false); // already booked
        when(customerRepository.findById(1)).thenReturn(Optional.of(customer));
        when(scheduleRepository.findById(101)).thenReturn(Optional.of(schedule));
        when(seatRepository.findById(501)).thenReturn(Optional.of(seat));

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> bookingService.createBooking(request));
        assertTrue(ex.getMessage().contains("Seat already booked"));
    }

    @Test
    public void testGetBookingsByCustomerEmail_Success() {
        User user = new User();
        user.setId(10);
        user.setUsername("john@example.com");

        Page<Booking> mockPage = new PageImpl<>(List.of(booking));

        when(userRepository.findByUsername("john@example.com")).thenReturn(Optional.of(user));
        when(customerRepository.findByUser(user)).thenReturn(Optional.of(customer));
        when(bookingRepository.findByCustomer(eq(customer), any(Pageable.class))).thenReturn(mockPage);

        Page<Booking> result = bookingService.getBookingsByCustomerEmail("john@example.com", 0, 5);

        assertEquals(1, result.getContent().size());
        assertEquals(customer, result.getContent().get(0).getCustomer());
    }

    @Test
    public void testGetBookingsByCustomerEmail_InvalidUser() {
        when(userRepository.findByUsername("invalid@example.com")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> bookingService.getBookingsByCustomerEmail("invalid@example.com", 0, 5));
        assertEquals("User not found.", ex.getMessage());
    }

    @Test
    public void testGetBookingsByCustomerEmail_InvalidCustomer() {
        User user = new User();
        when(userRepository.findByUsername("john@example.com")).thenReturn(Optional.of(user));
        when(customerRepository.findByUser(user)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> bookingService.getBookingsByCustomerEmail("john@example.com", 0, 5));
        assertEquals("Customer not found.", ex.getMessage());
    }

    @Test
    public void testGetBookingByIdAndUsername_Success() {
        when(bookingRepository.findByIdAndCustomerEmail(201, "john@example.com")).thenReturn(Optional.of(booking));

        Booking result = bookingService.getBookingByIdAndUsername(201, "john@example.com");

        assertEquals(201, result.getId());
        assertEquals(customer, result.getCustomer());
    }

    @Test
    public void testGetBookingByIdAndUsername_Invalid() {
        when(bookingRepository.findByIdAndCustomerEmail(999, "john@example.com")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> bookingService.getBookingByIdAndUsername(999, "john@example.com"));
        assertEquals("Booking not found or access denied", ex.getMessage());
    }

    @Test
    public void testGetBookingDetailsByBookingId_Success() {
        BookingDetails detail = new BookingDetails();
        detail.setBooking(booking);

        when(bookingRepository.findById(201)).thenReturn(Optional.of(booking));
        when(bookingDetailsRepository.findByBooking(booking)).thenReturn(List.of(detail));

        List<BookingDetails> details = bookingService.getBookingDetailsByBookingId(201);
        assertEquals(1, details.size());
    }

    @Test
    public void testGetBookingDetailsByBookingId_InvalidBooking() {
        when(bookingRepository.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> bookingService.getBookingDetailsByBookingId(999));

        assertEquals("Booking not found with ID: 999", ex.getMessage());
    }
}
