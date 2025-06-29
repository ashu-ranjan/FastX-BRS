package com.backend.fastx.service;

import com.backend.fastx.enums.Role;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.BusOperator;
import com.backend.fastx.model.User;
import com.backend.fastx.repository.BusOperatorRepository;
import com.backend.fastx.utility.BusOperatorUtility;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class BusOperatorService {

    private final BusOperatorRepository busOperatorRepository;
    private final BusOperatorUtility busOperatorUtility;
    private final UserService userService;

    Logger logger = LoggerFactory.getLogger(BusOperatorService.class);

    public BusOperatorService(BusOperatorRepository busOperatorRepository, BusOperatorUtility busOperatorUtility, UserService userService) {
        this.busOperatorRepository = busOperatorRepository;
        this.busOperatorUtility = busOperatorUtility;
        this.userService = userService;
    }

    /**
     * @aim add a new bus operator
     * @description This method will add a new bus operator to the system.
     * @param busOperator
     * @return BusOperator
     */

    public BusOperator addBusOperator(BusOperator busOperator) {
        busOperatorUtility.validateBusOperator(busOperator);

        // Take user out of this customer object
        User user = busOperator.getUser();

        // Give role to the user
        user.setRole(Role.OPERATOR);

        // Save this User in the DB
        user = userService.register(user, busOperator.getEmail());

        // Attach this user back to busOperator
        busOperator.setUser(user);

        // Save busOperator in DB
        return busOperatorRepository.save(busOperator);
    }

    /**
     * @aim update bus operator details
     * @description Update bus operator details by email
     * @param busOperator
     * @return BusOperator
     * @throws ResourceNotFoundException if bus operator not found
     */

    public BusOperator updateBusOperator(BusOperator busOperator) {
        busOperatorUtility.validateBusOperator(busOperator);

        // Find existing bus operator
        BusOperator existingBusOperator = busOperatorRepository.findByEmail(busOperator.getEmail())
            .orElseThrow(() -> new ResourceNotFoundException("BusOperator not found"));

        // Update details
        existingBusOperator.setName(busOperator.getName());
        existingBusOperator.setEmail(busOperator.getEmail());
        existingBusOperator.setCompany(busOperator.getCompany());
        existingBusOperator.setContact(busOperator.getContact());
        existingBusOperator.setCompanyAddress(busOperator.getCompanyAddress());

        // Save updated bus operator in DB
        return busOperatorRepository.save(existingBusOperator);
    }

    /**
     * @aim upload bus operator image
     * @description This method will upload an image for the bus operator.
     * @param email
     * @param file
     * @return String URL of the uploaded image
     * @throws IOException
     */

    public String uploadBusOperatorImage(String email, MultipartFile file) throws IOException {
        BusOperator busOperator = busOperatorRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("BusOperator not found"));
        logger.info("Uploading image for bus operator: {}", busOperator.getEmail());

        /* extension check: jpg,jpeg,png,gif,svg : */
        String originalFileName = file.getOriginalFilename();
        logger.info("Original file name: {}", originalFileName);

        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
        if(!(List.of("jpg", "jpeg", "png", "gif", "svg").contains(extension))) {
            throw new RuntimeException("Invalid file extension. Allowed extensions are jpg, jpeg, png, gif, svg.");
        }
        logger.info("File extension is valid: {}", extension);

        /* check file size */
        long kbs = file.getSize() / 1024;
        if (kbs > 3000) {
            throw new RuntimeException("File size exceeds 3000 KB limit.");
        }
        logger.info("File size is within limit: {} KB", kbs);

        /* Check if Directory exists, else create one */

        String directoryPath = "D:\\FastX\\fastx-ui\\public\\images";
        Files.createDirectories(Path.of(directoryPath));
        logger.info("Directory exists or created: {}", directoryPath);

        /* Define full path */
        Path path = Paths.get(directoryPath,"\\",originalFileName);
        logger.info("Full path for uploaded image: {}", path);

        /* Upload file in the above path */
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File uploaded successfully to: {}", path);

        /* Set url of file or image in driver object */
        busOperator.setProfilePic(originalFileName);
        logger.info("Profile picture set for bus operator: {}", busOperator.getEmail());

        /* Save driver with profile picture */
        busOperatorRepository.save(busOperator);
        logger.info("Bus operator profile updated with new image: {}", busOperator.getEmail());

        return "Image uploaded successfully";
    }

    /**
     * @aim get bus operator profile
     * @description This method will retrieve the bus operator's profile using their email.
     * @param email
     * @return BusOperator
     * @throws ResourceNotFoundException if bus operator not found
     */

    public BusOperator getBusOperatorByEmail(String email) {
        return busOperatorRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("BusOperator not found"));
    }
}
