package com.backend.fastx.controller;

import com.backend.fastx.model.Seat;
import com.backend.fastx.service.SeatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
public class SeatController {

    @Autowired
    private SeatService seatService;

    /*
     * AIM: adding seat to db
     * PATH: /fastx/add/seat/{busId}
     * METHOD: POST
     * RESPONSE: Seat
     * */
    @PostMapping("/fastx/add/seat/{busId}")
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

    @GetMapping("/fastx/get/seat")
    public ResponseEntity<?> getAvailableSeats(@RequestParam int scheduleId){
        List<Seat> seats = seatService.getAvailableSeatsForSchedule(scheduleId);
        return ResponseEntity.ok(seats);
    }
}
