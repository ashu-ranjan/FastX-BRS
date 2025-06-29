package com.backend.fastx;

import com.backend.fastx.dto.CancelBookingRequestDTO;
import com.backend.fastx.dto.CancellationApprovalDTO;
import com.backend.fastx.enums.BookingStatus;
import com.backend.fastx.enums.RefundStatus;
import com.backend.fastx.enums.Role;
import com.backend.fastx.exception.IllegalStateException;
import com.backend.fastx.exception.UnauthorizedAccessException;
import com.backend.fastx.exception.UserAccessDeniedException;
import com.backend.fastx.model.*;
import com.backend.fastx.repository.*;
import com.backend.fastx.service.CancellationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CancellationServiceTest {

    @InjectMocks
    private CancellationService cancellationService;

    @Mock private BookingDetailsRepository bookingDetailsRepository;
    @Mock private CancellationRepository cancellationRepository;
    @Mock private SeatRepository seatRepository;
    @Mock private UserRepository userRepository;
    @Mock private BusRepository busRepository;

    private BookingDetails bookingDetails;
    private Booking booking;
    private Schedule schedule;
    private Seat seat;
    private Customer customer;

    @BeforeEach
    public void init() {
        seat = new Seat();
        seat.setId(1);
        seat.setActive(false);

        customer = new Customer();
        customer.setId(1);
        customer.setEmail("test@user.com");
        customer.setName("Test User");

        schedule = new Schedule();
        schedule.setDepartureTime(LocalTime.now().plusHours(3));
        schedule.setId(1);

        booking = new Booking();
        booking.setId(100);
        booking.setCustomer(customer);
        booking.setSchedule(schedule);
        booking.setJourneyDate(LocalDate.now());
        booking.setTotalFare(1000);

        bookingDetails = new BookingDetails();
        bookingDetails.setId(10);
        bookingDetails.setBooking(booking);
        bookingDetails.setPassenger("John Doe");
        bookingDetails.setSeat(seat);
        bookingDetails.setCancelled(false);
    }

    @Test
    public void testCreateCancellationRequest_Success() {
        CancelBookingRequestDTO dto = new CancelBookingRequestDTO(null);
        dto.setReason("Change of plans");

        when(bookingDetailsRepository.findById(10)).thenReturn(Optional.of(bookingDetails));
        when(bookingDetailsRepository.findByBooking(booking)).thenReturn(List.of(bookingDetails));

        assertDoesNotThrow(() -> cancellationService.createCancellationRequest(10, dto, "test@user.com"));

        verify(cancellationRepository).save(any(Cancellation.class));
    }

    @Test
    public void testCreateCancellationRequest_WrongUser() {
        when(bookingDetailsRepository.findById(10)).thenReturn(Optional.of(bookingDetails));

        assertThrows(UnauthorizedAccessException.class,
                () -> cancellationService.createCancellationRequest(10, new CancelBookingRequestDTO(), "wrong@user.com"));
    }

    @Test
    public void testCreateCancellationRequest_AlreadyCancelled() {
        bookingDetails.setCancelled(true);
        when(bookingDetailsRepository.findById(10)).thenReturn(Optional.of(bookingDetails));

        assertThrows(IllegalStateException.class,
                () -> cancellationService.createCancellationRequest(10, new CancelBookingRequestDTO(), "test@user.com"));
    }

    @Test
    public void testApproveCancellation_Success() {
        Cancellation cancellation = new Cancellation();
        cancellation.setId(1);
        cancellation.setRefundStatus(RefundStatus.REQUESTED);
        cancellation.setBookingDetails(bookingDetails);

        booking.setStatus(BookingStatus.PENDING);
        bookingDetails.setCancelled(false);

        User executive = new User();
        executive.setUsername("exec@fastx.com");
        executive.setRole(Role.EXECUTIVE);

        Schedule schedule = booking.getSchedule();
        Bus bus = new Bus();
        bus.setId(1);
        schedule.setBus(bus);

        when(cancellationRepository.findById(1)).thenReturn(Optional.of(cancellation));
        when(userRepository.findByUsername("exec@fastx.com")).thenReturn(Optional.of(executive));
        when(busRepository.findByIdWithOperator(1)).thenReturn(Optional.of(bus));
        when(bookingDetailsRepository.findByBooking(booking)).thenReturn(List.of(bookingDetails));

        CancellationApprovalDTO dto = new CancellationApprovalDTO();
        dto.setCancellationId(1);
        dto.setRefundStatus(RefundStatus.APPROVED);
        dto.setRemarks("Approved");

        assertDoesNotThrow(() -> cancellationService.approveCancellation(dto, "exec@fastx.com"));

        assertTrue(bookingDetails.isCancelled());
        assertEquals(BookingStatus.CANCELLED, booking.getStatus());
    }

    @Test
    public void testApproveCancellation_NotRequested() {
        Cancellation cancellation = new Cancellation();
        cancellation.setId(1);
        cancellation.setRefundStatus(RefundStatus.APPROVED);
        cancellation.setBookingDetails(bookingDetails);

        when(cancellationRepository.findById(1)).thenReturn(Optional.of(cancellation));

        CancellationApprovalDTO dto = new CancellationApprovalDTO();
        dto.setCancellationId(1);
        dto.setRefundStatus(RefundStatus.APPROVED);

        assertThrows(IllegalStateException.class, () ->
                cancellationService.approveCancellation(dto, "exec@fastx.com"));
    }

    @Test
    public void testApproveCancellation_NotExecutive() {
        Cancellation cancellation = new Cancellation();
        cancellation.setId(1);
        cancellation.setRefundStatus(RefundStatus.REQUESTED);
        cancellation.setBookingDetails(bookingDetails);

        User user = new User();
        user.setUsername("staff@fastx.com");
        user.setRole(Role.CUSTOMER);

        Schedule schedule = booking.getSchedule();
        Bus bus = new Bus();
        bus.setId(1);
        schedule.setBus(bus);

        when(cancellationRepository.findById(1)).thenReturn(Optional.of(cancellation));
        when(userRepository.findByUsername("staff@fastx.com")).thenReturn(Optional.of(user));
        when(busRepository.findByIdWithOperator(1)).thenReturn(Optional.of(bus));

        CancellationApprovalDTO dto = new CancellationApprovalDTO();
        dto.setCancellationId(1);
        dto.setRefundStatus(RefundStatus.APPROVED);

        assertThrows(UserAccessDeniedException.class, () ->
                cancellationService.approveCancellation(dto, "staff@fastx.com"));
    }
}
