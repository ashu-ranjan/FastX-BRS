package com.backend.fastx.repository;

import com.backend.fastx.enums.ScheduleDays;
import com.backend.fastx.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @Query("SELECT s FROM Schedule s " +
                "WHERE s.busRoute.origin = ?1 " +
                "AND s.busRoute.destination = ?2 " +
                "AND (s.scheduleDays = ?3 OR s.scheduleDays = 'DAILY' )")
    List<Schedule> findByOriginDestinationAndDay(String origin, String destination, ScheduleDays days);
}
