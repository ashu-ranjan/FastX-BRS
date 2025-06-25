package com.backend.fastx.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.backend.fastx.model.Booking;
import com.backend.fastx.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Integer>{

    boolean findByBooking(Booking booking);

    List<Feedback> findByBookingIn(List<Booking> bookings);

    @Query("select f from Feedback f where f.booking.schedule.bus.id = ?1")
    List<Feedback> findByBusId(int busId);

}
