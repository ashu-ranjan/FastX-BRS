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

    /**
     * Adds a new seat to a bus.
     *
     * @param busId   The ID of the bus to which the seat is being added.
     * @param seat    The Seat object containing seat details.
     * @param username The username of the logged-in operator.
     * @return The saved Seat object.
     * @throws IllegalAccessException if the bus does not belong to the logged-in operator.
     */

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

    /**
     * Retrieves a list of available seats for a specific bus schedule.
     *
     * @param scheduleId The ID of the bus schedule for which available seats are requested.
     * @return A list of SeatResponseDTO containing details of available seats.
     */

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

    /**
     * Retrieves a list of seats for a specific bus.
     *
     * @param busId The ID of the bus for which seats are requested.
     * @return A list of Seat objects associated with the given bus ID.
     * @throws ResourceNotFoundException if no seats are found for the specified bus ID.
     */

    public List<Seat> getSeatsByBusId(int busId) {
        List<Seat> seats = seatRepository.findByBusId(busId);
        if (seats.isEmpty()) {
            throw new ResourceNotFoundException("No seats found for bus with ID: " + busId);
        }
        return seats;
    }

    /**
     * Updates the status of a seat.
     *
     * @param seatId   The ID of the seat to be updated.
     * @param seatDTO  The SeatDTO object containing updated seat details.
     * @param username The username of the logged-in operator.
     * @return The updated SeatDTO object.
     * @throws ResourceNotFoundException if the seat is not found with the specified ID.
     * @throws UnauthorizedBusAccessException if the bus does not belong to the logged-in operator.
     */

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
