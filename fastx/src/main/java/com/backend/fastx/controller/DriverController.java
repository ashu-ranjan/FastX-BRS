package com.backend.fastx.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Driver;
import com.backend.fastx.service.DriverService;


@RestController
public class DriverController {

    @Autowired
    private DriverService driverService;

    /**
     * @aim Add driver to Bus
     * @description This method will add a driver to a bus. It will take the bus ID and driver ID as parameters
     * @methodName addDriverToBus
     * @method POST
     * @param busId The ID of the bus to which the driver will be added.
     * @param driver The driver to be added to the bus.
     * @throws ResourceNotFoundException if the bus with the given ID does not exist.
     * @return A response indicating the success or failure of the operation.
    */

    @PostMapping("/fastx/api/driver/add/{busId}")
    public ResponseEntity<?> addDriverToBus(@PathVariable int busId, @RequestBody Driver driver) {
        return ResponseEntity.ok(driverService.addDriverToBus(busId, driver));
    }

    /**
     * @aim update driver details
     * @description This method will update the details of a driver. It will take the driver ID and the updated driver details as parameters.
     * @methodName updateDriverDetails
     * @method PUT
     * @param driverId The ID of the driver to be updated.
     * @param busId The ID of the bus associated with the driver.
     * @param driver The updated driver details.
     * @return A response indicating the success or failure of the operation.
     * @throws ResourceNotFoundException if the driver with the given ID does not exist.
    */
    @PutMapping("/fastx/api/driver/update/{driverId}/bus/{busId}")
    public ResponseEntity<?> updateDriverDetails(@PathVariable int driverId, 
                                                @PathVariable int busId, 
                                                @RequestBody Driver driver) {
        return ResponseEntity.ok(driverService.updateDriverDetails(driverId, busId, driver));
    }

    /**
     * @aim delete driver
     * @description This method will delete a driver from the system. It will take the driver ID as a parameter.
     * @methodName deleteDriver
     * @method DELETE
     * @param driverId The ID of the driver to be deleted.
     * @param busId The ID of the bus associated with the driver.
     * @return A response indicating the success or failure of the operation.
     * @throws ResourceNotFoundException if the driver with the given ID does not exist.
    */

    @DeleteMapping("/fastx/api/driver/delete/{driverId}/bus/{busId}")
    public ResponseEntity<?> deleteDriver(@PathVariable int driverId, @PathVariable int busId) {
        driverService.deleteDriver(driverId, busId);
        return ResponseEntity.noContent().build();
    }

    /**
     * @aim get all drivers
     * @description This method will retrieve all drivers from the system.      
     * @return A response containing a list of all drivers.
     * @methodName getAllDrivers
     * @method GET
     */
    @GetMapping("/fastx/api/driver/all")
    public ResponseEntity<?> getAllDrivers() {
        return ResponseEntity.ok(driverService.getAllDrivers());
    }
}
