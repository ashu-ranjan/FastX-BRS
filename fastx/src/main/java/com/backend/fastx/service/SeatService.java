package com.backend.fastx.service;

import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.exception.UnauthorizedBusAccessException;
import com.backend.fastx.model.Bus;
import com.backend.fastx.model.BusOperator;
import com.backend.fastx.model.Schedule;
import com.backend.fastx.model.Seat;
import com.backend.fastx.repository.BusOperatorRepository;
import com.backend.fastx.repository.BusRepository;
import com.backend.fastx.repository.ScheduleRepository;
import com.backend.fastx.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {

    private final BusRepository busRepository;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;

    @Autowired
    public SeatService(BusRepository busRepository, SeatRepository seatRepository, BusOperatorRepository busOperatorRepository, ScheduleRepository scheduleRepository) {
        this.busRepository = busRepository;
        this.seatRepository = seatRepository;
        this.scheduleRepository = scheduleRepository;
    }

    public Seat addSeatToBus(int busId, Seat seat, String username) throws IllegalAccessException {
        Bus bus = busRepository.findById(busId)
                .orElseThrow(()-> new ResourceNotFoundException("Bus not found with ID" + busId));

        // Checking the bus belongs to the logged in operator
        if (!bus.getBusOperator().getUser().getUsername().equalsIgnoreCase(username)){
            throw new UnauthorizedBusAccessException("You are not authorized to modify this bus");
        }

        seat.setBus(bus);
        seat.setActive(true); // default true
        return seatRepository.save(seat);
    }

    public List<Seat> getAvailableSeatsForSchedule(int scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new ResourceNotFoundException("Schedule Not Found"));
        int busId = schedule.getBus().getId();
        return seatRepository.findByBusIdAndIsActiveTrue(busId);
    }

}
