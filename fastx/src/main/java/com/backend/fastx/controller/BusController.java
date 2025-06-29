package com.backend.fastx.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.fastx.dto.BusAmenitiesDTO;
import com.backend.fastx.enums.BusType;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Bus;
import com.backend.fastx.service.BusService;

@RestController
@RequestMapping("/fastx/api/bus")
@CrossOrigin(origins = "http://localhost:5173") // Adjust the origin as needed
public class BusController {

    @Autowired
    private BusService busService;

    /**
     * @aim add a new bus
     * @description This method will add a new bus to the system. It will take the bus details as a parameter.
     * @methodName addBus
     * @path /fastx/api/bus/add
     * @method POST
     * @param bus
     * @param principal
     * @return A response indicating the success or failure of the operation.
     * @throws ResourceNotFoundException if the operator does not exist.
     */
    @PostMapping("/add")
    public ResponseEntity<?> addBus(@RequestBody Bus bus, Principal principal){
        String username = principal.getName();
        Bus addedBus = busService.addBus(bus, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedBus);
    }

    /**
     * @aim fetch buses by operator
     * @description This method will retrieve all buses associated with a specific operator.
     * @methodName getBusesByOperator
     * @path /fastx/api/bus/get-buses
     * @method GET
     * @param principal
     * @return A response containing a list of buses associated with the operator.
     * @throws ResourceNotFoundException if the operator does not exist or has no buses.
     */
    @GetMapping("/get-buses")
    public ResponseEntity<?> getBusesByOperator(Principal principal) {
        String username = principal.getName();
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(busService.getBusesByOperator(username));
    }

    /**
     * @aim get all bus types
     * @description This method will retrieve all available bus types.
     * @methodName getBusTypes
     * @path /fastx/api/bus/get-bus-type
     * @method GET
     * @return A response containing a list of all bus types.
     */

    @GetMapping("/get-bus-type")
    public ResponseEntity<?> getBusTypes() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BusType.values());
    }

    /**
     * @aim add amenities to a bus
     * @description This method will add amenities to a bus. It will take the bus amenities
     * @path /fastx/api/bus/add-amenities
     * @method POST
     * @param amenitiesDTO
     * @param principal
     * @return A response indicating the success or failure of the operation.
     * @throws ResourceNotFoundException if the bus does not exist or the operator does not exist.
     */
    @PostMapping("/add-amenities")
    public ResponseEntity<?> addAmenitiesToBus(@RequestBody BusAmenitiesDTO amenitiesDTO, Principal principal) {
        String username = principal.getName();
        Bus updatedBus = busService.addAmenitiesToBus(amenitiesDTO, username);
        return ResponseEntity.status(HttpStatus.OK).body(updatedBus);
    }

    /**
     * @aim get amenities for a bus
     * @description This method will retrieve the amenities associated with a bus.
     * @methodName getAmenitiesForBus
     * @path /fastx/api/bus/get-amenities
     * @method GET
     * @param principal
     * @return A response containing the amenities associated with the bus.
     * @throws ResourceNotFoundException if the bus does not exist or the operator does not exist.
     */
    @GetMapping("/get-amenities")
    public ResponseEntity<?> getAmenitiesForBus(Principal principal) {
        String username = principal.getName();
        BusAmenitiesDTO amenities = busService.getAmenitiesForBus(username);
        return ResponseEntity.status(HttpStatus.OK).body(amenities);
    }

    /**
     * @aim get bus by id
     * @description This method will retrieve a bus by its ID. It will take the bus ID as a parameter.
     * @methodName getBusById   
     * @param busId The ID of the bus to be retrieved.
     * @return A response containing the bus associated with the given ID.
     * @throws ResourceNotFoundException if the bus with the given ID does not exist.
     */

    @GetMapping("/get-bus/{busId}")
    public ResponseEntity<?> getBusById(@PathVariable int busId) {
        return ResponseEntity.ok(busService.getBusById(busId));
    }

    /**
     * @aim update bus details
     * @description This method will update the details of a bus. It will take the bus ID and the updated bus details as parameters.
     * @methodName updateBusDetails
     * @param busId The ID of the bus to be updated.
     * @param bus The updated bus details.
     * @return A response indicating the success or failure of the operation.
     * @throws ResourceNotFoundException if the bus with the given ID does not exist.
     */
    @PutMapping("/update/{busId}")
    public ResponseEntity<?> updateBusDetails(@PathVariable int busId, @RequestBody Bus bus) {
        Bus updatedBus = busService.updateBusDetails(busId, bus);
        return ResponseEntity.ok(updatedBus);
    }
}
