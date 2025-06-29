package com.backend.fastx.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

    public Driver updateDriverDetails(int driverId, Driver driver) {
        Driver existingDriver = driverRepository.findById(driverId)
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));
        
        existingDriver.setName(driver.getName());
        existingDriver.setContact(driver.getContact());
        existingDriver.setEmail(driver.getEmail());
        existingDriver.setLicenseNumber(driver.getLicenseNumber());
        existingDriver.setExperience(driver.getExperience());
        existingDriver.setProfilePic(driver.getProfilePic());
        return driverRepository.save(existingDriver);
    }

    /**
     * Deletes a driver from the system.
     *
     * @param driverId The ID of the driver to be deleted.
     * @param busId    The ID of the bus associated with the driver.
     * @throws ResourceNotFoundException if the driver or bus with the given IDs do not exist.
     */

     @Transactional
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

    /**
     * Retrieves a driver by the bus ID.
     *
     * @param busId The ID of the bus for which the driver is being retrieved.
     * @return The driver associated with the given bus ID.
     * @throws ResourceNotFoundException if the bus or driver does not exist.
     */

    public Driver getDriverByBusId(int busId) {
        Bus bus = busRepository.findById(busId)
            .orElseThrow(() -> new ResourceNotFoundException("Bus not found"));
        
        return driverRepository.findByBus(bus)
            .orElseThrow(() -> new ResourceNotFoundException("No driver found for this bus"));
    }

    /**
     * Uploads a driver's profile image.
     *
     * @param driverId The ID of the driver whose image is being uploaded.
     * @param file     The image file to be uploaded.
     * @return A message indicating the success of the upload.
     * @throws IOException if an error occurs during file upload.
     * @throws RuntimeException if the file extension is invalid or the file size exceeds the limit.
     */

    public String uploadDriverImage(int driverId, MultipartFile file) throws IOException {
        Driver driver = driverRepository.findById(driverId)
            .orElseThrow(() -> new ResourceNotFoundException("Driver not found"));

        /* extension check: jpg,jpeg,png,gif,svg : */
        String originalFileName = file.getOriginalFilename();

        String extension = originalFileName.split("\\")[1];
        if(!(List.of("jpg", "jpeg", "png", "gif", "svg").contains(extension))) {
            throw new RuntimeException("Invalid file extension. Allowed extensions are jpg, jpeg, png, gif, svg.");
        }

        /* check file size */
        long kbs = file.getSize() / 1024;
        if (kbs > 3000) {
            throw new RuntimeException("File size exceeds 3000 KB limit.");
        }

        /* Check if Directory exists, else create one */

        String directoryPath = "D:\\FastX\\fastx-ui\\public\\images";
        Files.createDirectories(Path.of(directoryPath));

        /* Define full path */
        Path path = Paths.get(directoryPath,"\\",originalFileName);

        /* Upload file in the above path */
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        /* Set url of file or image in driver object */
        driver.setProfilePic(originalFileName);

        /* Save driver with profile picture */
        driverRepository.save(driver);

        return "Image uploaded successfully";
    }
    
}
