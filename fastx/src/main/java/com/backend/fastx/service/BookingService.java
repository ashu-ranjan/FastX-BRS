package com.backend.fastx.service;

import com.backend.fastx.dto.BookingRequestDTO;
import com.backend.fastx.dto.PassengerDTO;
import com.backend.fastx.enums.BookingStatus;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.*;
import com.backend.fastx.repository.*;
import com.backend.fastx.utility.FareUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingService {

    @Autowired private BookingRepository bookingRepository;
    @Autowired private ScheduleRepository scheduleRepository;
    @Autowired private CustomerRepository customerRepository;
    @Autowired private SeatRepository seatRepository;
    @Autowired private BookingDetailsRepository bookingDetailsRepository;

    @Transactional
    public Booking createBooking(BookingRequestDTO bookingRequestDTO) {

        // Step 1: Fetch customer and schedule
        Customer customer = customerRepository.findById(bookingRequestDTO.getCustomerId())
                .orElseThrow(()-> new ResourceNotFoundException("Customer Not Found."));
        Schedule schedule = scheduleRepository.findById(bookingRequestDTO.getScheduleId())
                .orElseThrow(()-> new ResourceNotFoundException("Schedule Not Found."));

        // Step 2: Create and save booking object
        Booking booking = new Booking();
        booking.setCustomer(customer);
        booking.setSchedule(schedule);
        booking.setBookedOn(new Timestamp(System.currentTimeMillis()));
        booking.setStatus(BookingStatus.CONFIRMED);

        // Save booking early to generate ID (used in BookingDetails)
        Booking savedBooking = bookingRepository.save(booking);

        // Step 3: Prepare booking details and calculate fare
        double totalFare = 0;
        List<BookingDetails> detailsList = new ArrayList<>();

        for (PassengerDTO passenger : bookingRequestDTO.getPassengers()){
            // Fetch seat
            Seat seat = seatRepository.findById(passenger.getSeatId())
                    .orElseThrow(()-> new ResourceNotFoundException("Seat Not Found."));

            // Checking seat is available or not
            if (!seat.isActive()){
                throw new ResourceNotFoundException("Seat already booked." + seat.getSeatNumber());
            }

            seat.setActive(false); // as Booked
            seatRepository.save(seat);

            double fare = FareUtility.calculateSeatFare(schedule,seat);
            totalFare += fare;

            BookingDetails details = new BookingDetails();
            details.setPassenger(passenger.getPassenger());
            details.setAge(passenger.getAge());
            details.setGender(passenger.getGender());
            details.setBoardingPoint(passenger.getBoardingPoint());
            details.setDroppingPoint(passenger.getDroppingPoint());
            details.setSeat(seat);
            details.setBooking(savedBooking);
            bookingDetailsRepository.save(details);
            detailsList.add(details);

        }

        // Step 4: Update saved booking with final fare and total seat count
        savedBooking.setTotalFare(totalFare);
        savedBooking.setTotalSeat(detailsList.size());

        return bookingRepository.save(savedBooking);

    }
}
