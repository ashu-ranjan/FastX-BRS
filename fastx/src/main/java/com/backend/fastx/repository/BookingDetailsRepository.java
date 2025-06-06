package com.backend.fastx.repository;

import com.backend.fastx.model.Booking;
import com.backend.fastx.model.BookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Integer> {
    List<BookingDetails> findByBooking(Booking booking);
}
