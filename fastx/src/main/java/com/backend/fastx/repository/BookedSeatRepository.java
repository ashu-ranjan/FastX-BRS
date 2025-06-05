package com.backend.fastx.repository;

import com.backend.fastx.model.BookedSeat;
import com.backend.fastx.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookedSeatRepository extends JpaRepository<BookedSeat, Integer> {
    List<BookedSeat> findByBooking(Booking booking);
}
