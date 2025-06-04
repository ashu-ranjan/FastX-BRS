package com.backend.fastx.repository;

import com.backend.fastx.model.BookingDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingDetailsRepository extends JpaRepository<BookingDetails, Integer> {
}
