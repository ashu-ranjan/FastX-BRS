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

    /*
     * AIM: adding seat to db
     * PATH: /fastx/add/seat/{busId}
     * METHOD: POST
     * RESPONSE: Seat
     * */
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

    /*
     * AIM: get seats by busId
     * PATH: /fastx/get/seats/bus/{busId}
     * METHOD: GET
     * RESPONSE: Seat
     */
    @GetMapping("/get/seats/bus/{busId}")
    public ResponseEntity<?> getSeatsByBusId(@PathVariable int busId) {
        return ResponseEntity.ok(seatService.getSeatsByBusId(busId));
    }

    /*
     * AIM: update seat 
     * PATH: /fastx/update/seat/{seatId}
     * METHOD: PUT
     * RESPONSE: SeatDTO
     * 
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
