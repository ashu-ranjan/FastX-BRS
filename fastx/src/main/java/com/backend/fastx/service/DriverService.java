package com.backend.fastx.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Bus;
import com.backend.fastx.model.Driver;
import com.backend.fastx.repository.BusRepository;
import com.backend.fastx.repository.DriverRepository;

@Service
public class DriverService {

    private final DriverRepository driverRepository;
    private final BusRepository busRepository;

    public DriverService(DriverRepository driverRepository, BusRepository busRepository) {
        this.driverRepository = driverRepository;
        this.busRepository = busRepository;
    }
    /**
     * Adds a driver to a bus.
     *
     * @param busId   The ID of the bus to which the driver will be added.
     * @param driver  The driver to be added.
     * @return The added driver.
     * @throws ResourceNotFoundException if the bus with the given ID does not exist.
     */
    public Driver addDriverToBus(int busId, Driver driver) {
        Bus bus = busRepository.findById(busId)
            .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        driver.setBus(bus);
        return driverRepository.save(driver);
    }

    /**
     * Updates the details of a driver.
     *
     * @param driverId The ID of the driver to be updated.
     * @param busId    The ID of the bus associated with the driver.
     * @param driver   The updated driver details.
     * @return The updated driver.
     * @throws ResourceNotFoundException if the driver or bus with the given IDs do not exist.
     */

    public Driver updateDriverDetails(int driverId, int busId, Driver driver) {
        Driver existingDriver = driverRepository.findById(driverId)
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        Bus bus = busRepository.findById(busId)
            .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        
        existingDriver.setName(driver.getName());
        existingDriver.setContact(driver.getContact());
        existingDriver.setEmail(driver.getEmail());
        existingDriver.setLicenseNumber(driver.getLicenseNumber());
        existingDriver.setProfilePic(driver.getProfilePic());
        existingDriver.setBus(bus);
        return driverRepository.save(existingDriver);
    }

    /**
     * Deletes a driver from the system.
     *
     * @param driverId The ID of the driver to be deleted.
     * @param busId    The ID of the bus associated with the driver.
     * @throws ResourceNotFoundException if the driver or bus with the given IDs do not exist.
     */

    public void deleteDriver(int driverId, int busId) {
        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        Bus bus = busRepository.findById(busId)
            .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));

        if (driver.getBus().equals(bus)) {
            driverRepository.delete(driver);
        } else {
            throw new ResourceNotFoundException("Driver is not associated with this bus");
        }
    }

    /**
     * Retrieves all drivers from the system.
     *
     * @return A list of all drivers.
     */

    public List<Driver> getAllDrivers() {
        return driverRepository.findAll();
    }

}
