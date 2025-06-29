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
@CrossOrigin(origins = "http://localhost:5173") // Adjust the origin as needed
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    /**
     * @aim Create a new schedule
     * @description This method allows an operator to create a new schedule for a bus on a specific route.
     * @path /fastx/api/schedules/create/bus/{busId}/route/{routeId}
     * @method POST
     * @param schedule The schedule details to be created.
     * @param busId The ID of the bus for which the schedule is being created.
     * @param routeId The ID of the route for which the schedule is being created.
     * @return ResponseEntity containing the created schedule.
     */
    @PostMapping("/create/bus/{busId}/route/{routeId}")
    public ResponseEntity<?> createSchedule(@RequestBody Schedule schedule,
                                            @PathVariable int busId,
                                            @PathVariable int routeId){
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(scheduleService.createSchedule(schedule, busId, routeId));
    }

    /**
     * @aim Get schedule by route and date
     * @description This method retrieves the bus schedule for a specific route and date.
     * @path /fastx/api/schedules/search
     * @method GET
     * @param origin The origin location for the bus schedule.
     * @param destination The destination location for the bus schedule.
     * @param date The date for which the bus schedule is being requested.
     * @return ResponseEntity containing the bus schedule for the specified route and date.
     */

    @GetMapping("/search")
    public ResponseEntity<?> getScheduleByRouteAndDate(@RequestParam String origin,
                                                       @RequestParam String destination,
                                                       @RequestParam LocalDate date){
        List<BusScheduleDto> response = scheduleService.getScheduleByRouteAndDate(origin,destination,date);
        return ResponseEntity.ok(response);
    }

    /**
     * @aim Get all schedules
     * @description This method retrieves all bus schedules available in the system.
     * @path /fastx/api/schedules/all
     * @method GET
     * @return ResponseEntity containing the list of all bus schedules.
     */
    @GetMapping("/all")
    public ResponseEntity<?> getAllSchedules(){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(scheduleService.getAllSchedules());
    }

    /**
     * @aim Get schedule by ID
     * @description This method retrieves a bus schedule by its unique ID.
     * @path /fastx/api/schedules/id/{id}
     * @method GET
     * @param id The unique identifier of the bus schedule.
     * @return ResponseEntity containing the bus schedule with the specified ID.
     */
    @GetMapping("/id/{id}")
    public ResponseEntity<?> getScheduleById(@PathVariable int id){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(scheduleService.getScheduleById(id));
    }

    /**
     * @aim get schedule by bus id
     * @path /fastx/api/schedules/bus/{busId}
     * @method GET
     * @return ResponseEntity<Schedule>
     */
    @GetMapping("/bus/{busId}")
    public ResponseEntity<?> getScheduleByBusId(@PathVariable int busId){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(scheduleService.getScheduleByBusId(busId));
    }

}
