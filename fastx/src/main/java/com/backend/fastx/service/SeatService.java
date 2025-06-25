package com.backend.fastx.service;

import com.backend.fastx.dto.SeatDTO;
import com.backend.fastx.dto.SeatResponseDTO;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.exception.UnauthorizedBusAccessException;
import com.backend.fastx.model.Bus;
import com.backend.fastx.model.Schedule;
import com.backend.fastx.model.Seat;
import com.backend.fastx.repository.BusOperatorRepository;
import com.backend.fastx.repository.BusRepository;
import com.backend.fastx.repository.ScheduleRepository;
import com.backend.fastx.repository.SeatRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SeatService {

    private final BusRepository busRepository;
    private final SeatRepository seatRepository;
    private final ScheduleRepository scheduleRepository;

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

    public List<SeatResponseDTO> getAvailableSeatsForSchedule(int scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(()-> new ResourceNotFoundException("Schedule Not Found"));

        int busId = schedule.getBus().getId();

        List<Seat> seats = seatRepository.findByBusIdAndIsActiveTrue(busId);

        return seats.stream().map(seat -> new SeatResponseDTO(
                seat.getId(),
                seat.getSeatNumber(),
                seat.getSeatType(),
                seat.isActive()
        )).collect(Collectors.toList());
    }

    public List<Seat> getSeatsByBusId(int busId) {
        List<Seat> seats = seatRepository.findByBusId(busId);
        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("No seats found for bus with ID: " + busId);
        }
        return seats;
    }

    public SeatDTO updateSeat(int seatId, SeatDTO seatDTO, String username) {
        Seat seat = seatRepository.findById(seatId)
                .orElseThrow(() -> new ResourceNotFoundException("Seat not found with ID: " + seatId));

        // Checking the bus belongs to the logged in operator
        if (!seat.getBus().getBusOperator().getUser().getUsername().equalsIgnoreCase(username)) {
            throw new UnauthorizedBusAccessException("You are not authorized to modify this seat");
        }

        // Update the seat's active status
        seat.setActive(seatDTO.isActive());
        seat.setPrice(seatDTO.getPrice());
        return new SeatDTO(seatRepository.save(seat));
    }

}
