package com.backend.fastx.repository;

import com.backend.fastx.model.Schedule;
import com.backend.fastx.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    List<Seat> findByBusIdAndIsActiveTrue(int busId);
}
