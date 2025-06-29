package com.backend.fastx.controller;

import com.backend.fastx.dto.SeatDTO;
import com.backend.fastx.model.Seat;
import com.backend.fastx.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
@RestController
@RequestMapping("/fastx/api")
@CrossOrigin(origins = "http://localhost:5173")
public class SeatController {

    @Autowired
    private SeatService seatService;

    /**
     * @aim add seat to bus
     * @description This method allows an operator to add a seat to a specific bus.
     * @path /fastx/add/seat/{busId}
     * @method POST
     * @param busId The ID of the bus to which the seat is being added.
     * @param seat The seat object containing the seat details.
     * @param principal The principal object representing the authenticated user.
     * @return ResponseEntity containing the created seat.
     * @throws IllegalAccessException
     */
    @PostMapping("/add/seat/{busId}")
    public ResponseEntity<?> addSeatToBus(@PathVariable int busId,
                                          @RequestBody Seat seat,
                                          Principal principal) throws IllegalAccessException {
        String username = principal.getName();
        Seat createdSeat = seatService.addSeatToBus(busId, seat, username);
        return ResponseEntity.ok(createdSeat);
    }

    /*
    * @aim getting active seats from bus
    * @path /fastx/get/seats?scheduleId=?
    * @method GET
    * @response Seat
    * */

    @GetMapping("/get/seat")
    public ResponseEntity<?> getAvailableSeats(@RequestParam int scheduleId){
        return ResponseEntity.ok(seatService.getAvailableSeatsForSchedule(scheduleId));
    }

    /**
     * @aim get all seats by bus id
     * @path /fastx/get/seats/bus/{busId}
     * @method GET
     * @description Get all seats by bus id
     * @return List<SeatResponseDTO>
     * 
     */
    @GetMapping("/get/seats/bus/{busId}")
    public ResponseEntity<?> getSeatsByBusId(@PathVariable int busId) {
        return ResponseEntity.ok(seatService.getSeatsByBusId(busId));
    }

    /**
     * @aim update seat
     * @path /fastx/update/seat/{seatId}
     * @method PUT
     * @description Update a seat by its ID
     * @param seatId The ID of the seat to be updated.
     * @param seatDTO The SeatDTO object containing the updated seat details.
     * @param principal The principal object representing the authenticated user.
     * @return ResponseEntity containing the updated SeatDTO.
     * @throws IllegalAccessException
     */
    @PutMapping("/update/seat/{seatId}")
    public ResponseEntity<?> updateSeat(@PathVariable int seatId,
                                         @RequestBody SeatDTO seatDTO,
                                         Principal principal) throws IllegalAccessException {
        String username = principal.getName();
        SeatDTO updatedSeat = seatService.updateSeat(seatId, seatDTO, username);
        return ResponseEntity.ok(updatedSeat);
    }

    

    

}
