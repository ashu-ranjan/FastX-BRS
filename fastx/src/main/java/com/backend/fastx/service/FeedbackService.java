package com.backend.fastx.service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.backend.fastx.dto.FeedbackCommentDTO;
import com.backend.fastx.dto.FeedbackCommentSummaryDTO;
import com.backend.fastx.dto.FeedbackDTO;
import com.backend.fastx.dto.FeedbackSummaryDTO;
import com.backend.fastx.enums.FeedbackStatus;
import com.backend.fastx.enums.Role;
import com.backend.fastx.exception.IllegalStateException;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.exception.UserAccessDeniedException;
import com.backend.fastx.model.Booking;
import com.backend.fastx.model.Bus;
import com.backend.fastx.model.Feedback;
import com.backend.fastx.model.User;
import com.backend.fastx.repository.BookingRepository;
import com.backend.fastx.repository.BusRepository;
import com.backend.fastx.repository.FeedbackRepository;
import com.backend.fastx.repository.UserRepository;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final BusRepository busRepository;

    public FeedbackService(FeedbackRepository feedbackRepository, BookingRepository bookingRepository,
            BusRepository busRepository, UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.bookingRepository = bookingRepository;
        this.userRepository = userRepository;
        this.busRepository = busRepository;
    }

    /**
     * Submits feedback for a booking.
     *
     * @param feedbackDTO The feedback data transfer object containing feedback details.
     * @param username    The username of the customer submitting the feedback.
     * @return A map containing a success message.
     * @throws ResourceNotFoundException if the booking is not found.
     * @throws UserAccessDeniedException if the user is not authorized to give feedback.
     * @throws IllegalStateException if feedback has already been submitted for the booking.
     */

    public Object submitFeedback(FeedbackDTO feedbackDTO, String username) {

        Booking booking = bookingRepository.findById(feedbackDTO.getBooingId())
                .orElseThrow(() -> new ResourceNotFoundException("Booking Not found"));

        if (!booking.getCustomer().getUser().getUsername().equals(username)) {
            throw new UserAccessDeniedException("You are not authorized to givr feedback");
        }

        boolean existingFeedback = feedbackRepository.findByBooking(booking);
        if (existingFeedback) {
            throw new IllegalStateException("Feedback already submited for the booking");
        }

        Feedback feedback = new Feedback();
        feedback.setBooking(booking);
        feedback.setComment(feedbackDTO.getComment());
        feedback.setFeedbackStatus(feedbackDTO.getFeedbackStatus());
        feedback.setFeedbackDate(LocalDate.now());

        feedbackRepository.save(feedback);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Feedback submitted successfully.");
        return response;

    }

    /**
     * Retrieves feedback submitted by the operator.
     *
     * @param username The username of the operator.
     * @return A list of FeedbackSummaryDTO containing feedback details.
     * @throws ResourceNotFoundException if the user is not found.
     * @throws UserAccessDeniedException if the user is not an operator.
     */
   
    public List<FeedbackSummaryDTO> getFeedbackForOperator(String username) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.OPERATOR) {
            throw new UserAccessDeniedException("Only Operators can acces feedback");
        }

        List<Bus> buses = busRepository.findByOperator(user);
        if (buses.isEmpty()) {
            return Collections.emptyList();
        }

        List<Booking> bookings = bookingRepository.findByScheduleBusIn(buses);
        if (bookings.isEmpty()) {
            return Collections.emptyList();
        }

        List<Feedback> feedbacks = feedbackRepository.findByBookingIn(bookings);

        return feedbacks.stream()
                .map(this::mapToDTO)
                .toList();
    }

    private FeedbackSummaryDTO mapToDTO(Feedback feedback) {

        FeedbackSummaryDTO dto = new FeedbackSummaryDTO();
        dto.setId(feedback.getId());
        dto.setComment(feedback.getComment());
        dto.setFeedbackDate(feedback.getFeedbackDate());
        dto.setFeedbackStatus(feedback.getFeedbackStatus());
        dto.setBookingId(feedback.getBooking().getId());
        return dto;
    }


    /**
     * Retrieves feedback summary for a specific bus.
     *
     * @param busId The ID of the bus for which feedback is requested.
     * @return A FeedbackCommentSummaryDTO containing average rating and comments.
     * @throws ResourceNotFoundException if the bus is not found.
     */

    public FeedbackCommentSummaryDTO getFeedbackSummaryForBus(int busId) {

        List<Feedback> feedbacks = feedbackRepository.findByBusId(busId);

        List<FeedbackCommentDTO> comments = feedbacks.stream()
                .filter(f -> f.getComment() != null && !f.getComment().isBlank())
                .map(f -> new FeedbackCommentDTO(
                        f.getBooking().getCustomer().getName(),
                        f.getComment(),
                        f.getFeedbackStatus(),
                        f.getFeedbackDate()))
                .sorted(Comparator.comparing(FeedbackCommentDTO::getFeedbackDate))
                .toList();

        double avgRating = feedbacks.stream()
                .mapToInt(f -> feedbackStatusToRating(f.getFeedbackStatus()))
                .average()
                .orElse(0.0);
        
        return new FeedbackCommentSummaryDTO(avgRating,comments);
    }

    private int feedbackStatusToRating(FeedbackStatus status) {
        return switch (status) {
            case TRASH -> 1;
            case BAD -> 2;
            case DECENT -> 3;
            case GREAT -> 4;
            case AMAZING -> 5;
        };
    }

}
