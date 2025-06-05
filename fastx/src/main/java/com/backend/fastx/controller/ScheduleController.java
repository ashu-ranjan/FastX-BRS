package com.backend.fastx.controller;

import com.backend.fastx.dto.BusScheduleDto;
import com.backend.fastx.model.Schedule;
import com.backend.fastx.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/fastx/api/schedules")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /*
     * AIM: Adding Schedule to db
     * PATH: /fastx/api/schedules/create
     * METHOD: POST
     * RESPONSE: Schedule
     * Authority: OPERATOR, EXECUTIVE
     * */
    @PostMapping("/create/bus/{busId}/route/{routeId}")
    public ResponseEntity<?> createSchedule(@RequestBody Schedule schedule,
                                            @PathVariable int busId,
                                            @PathVariable int routeId){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(scheduleService.createSchedule(schedule, busId, routeId));
    }

    /*
     * AIM: Search buses
     * PATH: /fastx/search
     * METHOD: GET
     * RESPONSE: List<Schedule>
     * Authority: CUSTOMER
     * */

    @GetMapping("/search")
    public ResponseEntity<?> getScheduleByRouteAndDate(@RequestParam String origin,
                                                       @RequestParam String destination,
                                                       @RequestParam LocalDate date){
        List<BusScheduleDto> response = scheduleService.getScheduleByRouteAndDate(origin,destination,date);
        return ResponseEntity.ok(response);
    }

    /*
     * AIM: Getting all Schedule
     * PATH: /fastx/api/schedules/all
     * METHOD: GET
     * RESPONSE: List<Schedule>
     * Authority: OPERATOR, EXECUTIVE
     * */
    @GetMapping("/all")
    public ResponseEntity<?> getAllSchedules(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(scheduleService.getAllSchedules());
    }

    /*
     * AIM: Getting Schedule by ID
     * PATH: /fastx/api/schedules/id/{id}
     * METHOD: GET
     * RESPONSE: Schedule
     * Authority: OPERATOR, EXECUTIVE
     * */
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getScheduleById(@PathVariable int id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(scheduleService.getScheduleById(id));
    }

    /*
     * AIM: Delete Schedule by ID
     * PATH: /fastx/api/schedules/delete/{id}
     * METHOD: DELETE
     * RESPONSE: void
     * Authority: EXECUTIVE
     * */

    // To do...
}
