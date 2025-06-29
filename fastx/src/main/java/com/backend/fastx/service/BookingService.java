package com.backend.fastx.service;

import com.backend.fastx.dto.BookingRequestDTO;
import com.backend.fastx.dto.PassengerDTO;
import com.backend.fastx.enums.BookingStatus;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.*;
import com.backend.fastx.repository.*;
import com.backend.fastx.utility.FareUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service // This annotation marks the class as a service provider in the Spring context
public class BookingService {

    private final BookingRepository bookingRepository;
    private final ScheduleRepository scheduleRepository;
    private final CustomerRepository customerRepository;
    private final SeatRepository seatRepository;
    private final BookingDetailsRepository bookingDetailsRepository;
    private final UserRepository userRepository;

    Logger logger = LoggerFactory.getLogger(BookingService.class);

    public BookingService(BookingRepository bookingRepository,
                          ScheduleRepository scheduleRepository,
                          CustomerRepository customerRepository,
                          SeatRepository seatRepository,
                          BookingDetailsRepository bookingDetailsRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.scheduleRepository = scheduleRepository;
        this.customerRepository = customerRepository;
        this.seatRepository = seatRepository;
        this.bookingDetailsRepository = bookingDetailsRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a booking based on the provided booking request.
     * 
     * @param bookingRequestDTO The DTO containing booking details.
     * @return The created Booking object.
     */
    @Transactional // This annotation ensures that the method runs within a transaction context
    public Booking createBooking(BookingRequestDTO bookingRequestDTO) {

        // Step 1: Fetch customer and schedule
        Customer customer = customerRepository.findById(bookingRequestDTO.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("Customer Not Found."));
        logger.info("Customer found: {}", customer.getUser().getUsername());

        Schedule schedule = scheduleRepository.findById(bookingRequestDTO.getScheduleId())
                .orElseThrow(()-> new ResourceNotFoundException("Schedule Not Found."));
        logger.info("Schedule found: {}", schedule.getId());

        // Step 2: Create and save booking object
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setSchedule(schedule);
        booking.setBookedOn(new Timestamp(System.currentTimeMillis()));
        booking.setJourneyDate(bookingRequestDTO.getJourneyDate());
        booking.setStatus(BookingStatus.PENDING); // Initial status set to PENDING

        // Save booking early to generate ID (used in BookingDetails)
        Booking savedBooking = bookingRepository.save(booking);
        logger.info("Booking created with ID: {}", savedBooking.getId());

        // Step 3: Prepare booking details and calculate fare
        double totalFare = 0;
        List<BookingDetails> detailsList = new ArrayList<>();

        for (PassengerDTO passenger : bookingRequestDTO.getPassengers()){
            // Fetch seat
            Seat seat = seatRepository.findById(passenger.getSeatId())
                    .orElseThrow(()-> new ResourceNotFoundException("Seat Not Found."));
            logger.info("Processing seat: {}", seat.getSeatNumber());

            // Checking seat is available or not
            if (!seat.isActive()){
                throw new ResourceNotFoundException("Seat already booked." + seat.getSeatNumber());
            
            }
            logger.warn("Seat {} is already booked.", seat.getSeatNumber());

            seat.setActive(false); // as Booked
            logger.info("Marking seat {} as booked.", seat.getSeatNumber());
            seatRepository.save(seat);

            double fare = FareUtility.calculateSeatFare(schedule,seat);
            totalFare += fare;
            logger.info("Fare for seat {}: {}", seat.getSeatNumber(), fare);

            BookingDetails details = new BookingDetails();
            details.setPassenger(passenger.getPassenger());
            details.setAge(passenger.getAge());
            details.setGender(passenger.getGender());
            details.setBoardingPoint(passenger.getBoardingPoint());
            details.setDroppingPoint(passenger.getDroppingPoint());
            details.setSeat(seat);
            details.setBooking(savedBooking);

            bookingDetailsRepository.save(details);
            logger.info("Booking details saved for passenger: {}", passenger.getPassenger());
            detailsList.add(details);

        }

        // Step 4: Update saved booking with final fare and total seat count
        savedBooking.setTotalFare(totalFare);
        savedBooking.setTotalSeat(detailsList.size());

        return bookingRepository.save(savedBooking);

    }

    /**
     * Retrieves a paginated list of bookings for a customer based on their email.
     * 
     * @param username The username of the customer.
     * @param page The page number to retrieve.
     * @param size The number of bookings per page.
     * @return A Page object containing the bookings.
     */

    public Page<Booking> getBookingsByCustomerEmail(String username, int page, int size) {
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User not found."));

    Customer customer = customerRepository.findByUser(user)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found."));
    
    Pageable pageable = PageRequest.of(page, size, Sort.by("bookedOn").descending());
    return bookingRepository.findByCustomer(customer, pageable);
    }

    /**
     * Retrieves a booking by its ID and the customer's email.
     * 
     * @param id The ID of the booking.
     * @param email The email of the customer.
     * @return The Booking object if found.
     */

    public Booking getBookingByIdAndUsername(int id, String email) {
        return bookingRepository.findByIdAndCustomerEmail(id, email)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found or access denied"));
    }

    /**
     * Retrieves booking details for a specific booking ID.
     * 
     * @param bookingId The ID of the booking.
     * @return A list of BookingDetails associated with the booking.
     */

    public List<BookingDetails> getBookingDetailsByBookingId(int bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));
        return bookingDetailsRepository.findByBooking(booking);
    }
}
