package com.backend.fastx.service;

import com.backend.fastx.dto.CancelBookingRequestDTO;
import com.backend.fastx.dto.CancelRequestDTO;
import com.backend.fastx.dto.CancellationApprovalDTO;
import com.backend.fastx.enums.BookingStatus;
import com.backend.fastx.enums.RefundStatus;
import com.backend.fastx.enums.Role;
import com.backend.fastx.exception.IllegalStateException;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.exception.UnauthorizedAccessException;
import com.backend.fastx.exception.UserAccessDeniedException;
import com.backend.fastx.model.*;
import com.backend.fastx.repository.*;
import com.backend.fastx.utility.CancellationUtility;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CancellationService {

    private final BookingDetailsRepository bookingDetailsRepository;
    private final CancellationRepository cancellationRepository;
    private final SeatRepository seatRepository;
    private final UserRepository userRepository;
    private final BusRepository busRepository;


    public CancellationService(BookingDetailsRepository bookingDetailsRepository, CancellationRepository cancellationRepository, SeatRepository seatRepository, UserRepository userRepository, BusRepository busRepository) {
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.cancellationRepository = cancellationRepository;
        this.seatRepository = seatRepository;
        this.userRepository = userRepository;
        this.busRepository = busRepository;
    }


    public void createCancellationRequest(int bookingDetailId, CancelBookingRequestDTO requestDTO, String email) {
        BookingDetails bookingDetails = bookingDetailsRepository.findById(bookingDetailId)
                .orElseThrow(()-> new ResourceNotFoundException("No booking details found."));

        Booking booking = bookingDetails.getBooking();

        if (!booking.getCustomer().getEmail().equals(email))
            throw new UnauthorizedAccessException("You can cancel only your bookings");

        if (bookingDetails.isCancelled())
            throw new IllegalStateException("This passenger already cancelled");

        // Fetch schedule for time calculation
        Schedule schedule = booking.getSchedule();
        if (booking.getJourneyDate() == null || schedule.getDepartureTime() == null) {
            throw new IllegalStateException("Schedule date or departure time is missing.");
        }
        LocalDateTime departureDateTime = LocalDateTime.of(
                booking.getJourneyDate(),
                schedule.getDepartureTime()
        );
        LocalDateTime now = LocalDateTime.now();

        List<BookingDetails> detailsList = bookingDetailsRepository.findByBooking(booking);

        double rate = CancellationUtility.getCancellationCharges(departureDateTime,now);
        double originalFare = booking.getTotalFare() / detailsList.size();
        double deductedAmount = CancellationUtility.refund(originalFare, rate);
        double refundAmount = Math.round((originalFare - deductedAmount) * 100.0) / 100.0;

        // Create Cancellation Entry
        Cancellation cancellation = new Cancellation();
        cancellation.setCancelDate(now);
        cancellation.setRefundAmount(refundAmount);
        cancellation.setReason(requestDTO.getReason());
        cancellation.setBookingDetails(bookingDetails);
        cancellation.setRefundStatus(RefundStatus.REQUESTED);

        cancellationRepository.save(cancellation);


    }

    public void approveCancellation(CancellationApprovalDTO approvalDTO, String username) {
        
        Cancellation cancellation = cancellationRepository.findById(approvalDTO.getCancellationId())
                .orElseThrow(() -> new ResourceNotFoundException("Cancellation not found"));

        if (cancellation.getRefundStatus() != RefundStatus.REQUESTED) {
            throw new IllegalStateException("Cancellation already processed");
        }

        BookingDetails bookingDetails = cancellation.getBookingDetails();
        Booking booking = bookingDetails.getBooking();
        Schedule schedule = booking.getSchedule();

        // Fetch full Bus info with operator
        Bus bus = busRepository.findByIdWithOperator(schedule.getBus().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));

        // Check operator / executive permissions
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new ResourceNotFoundException("Approving user not found"));

        if (user.getRole() != Role.EXECUTIVE) {
            throw new UserAccessDeniedException("Only executives can approve cancellations");
        }


        if (approvalDTO.getRefundStatus() == RefundStatus.APPROVED){

            Seat seat = bookingDetails.getSeat();
            seat.setActive(true);
            seatRepository.save(seat);

            bookingDetails.setCancelled(true);
            bookingDetailsRepository.save(bookingDetails);

            List<BookingDetails> detailsList = bookingDetailsRepository.findByBooking(booking);
            long total = detailsList.size();
            long cancelled = detailsList.stream().filter(BookingDetails::isCancelled).count();

            if (cancelled == total){
                booking.setStatus(BookingStatus.CANCELLED);
            } else {
                booking.setStatus(BookingStatus.PARTIALLY_CANCELLED);
            }
            bookingDetailsRepository.save(bookingDetails);
        }
        // update refund status
        cancellation.setRefundStatus(approvalDTO.getRefundStatus());
        cancellation.setReason(approvalDTO.getRemarks());
        cancellationRepository.save(cancellation);
    }

    public List<CancelRequestDTO> getRequestedCancellation(){
        return cancellationRepository.findByRefundStatus(RefundStatus.REQUESTED)
                .stream()
                .map(this::buildDTO)
                .collect(Collectors.toList());

    }

    public List<CancelRequestDTO> getCancellationHistory() {
        return cancellationRepository
                .findAll()
                .stream()
                .map(this::buildDTO)
                .collect(Collectors.toList());
    }

    // CancelRequestDTO build method
    private CancelRequestDTO buildDTO(Cancellation cancellation) {
        BookingDetails details = cancellation.getBookingDetails();
        Booking booking = details.getBooking();
        Customer customer = booking.getCustomer();

        return new CancelRequestDTO(
                cancellation.getId(),
                cancellation.getReason(),
                cancellation.getRefundStatus().name(),
                booking.getId(),
                details.getId(),
                details.getPassenger(),
                customer.getName(),
                customer.getEmail()
        );
    }
}
