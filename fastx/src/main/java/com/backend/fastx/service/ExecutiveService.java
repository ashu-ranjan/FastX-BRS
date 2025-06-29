package com.backend.fastx.service;

import com.backend.fastx.enums.Role;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Executive;
import com.backend.fastx.model.User;
import com.backend.fastx.repository.ExecutiveRepository;
import com.backend.fastx.utility.ExecutiveUtility;

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
public class ExecutiveService {

    private final ExecutiveRepository executiveRepository;
    private final ExecutiveUtility executiveUtility;
    private final UserService userService;

    Logger logger = LoggerFactory.getLogger(ExecutiveService.class);

    public ExecutiveService(ExecutiveRepository executiveRepository, ExecutiveUtility executiveUtility, UserService userService) {
        this.executiveRepository = executiveRepository;
        this.executiveUtility = executiveUtility;
        this.userService = userService;
    }

    /**
     * @aim Add a new executive
     * @description This method will add a new executive to the system.
     * @param executive
     * @return Executive
     */

    public Executive addExecutive(Executive executive) {

        executiveUtility.validateExecutive(executive);

        // Take user out of this Executive object
        User user = executive.getUser();

        // Give role to the user
        user.setRole(Role.EXECUTIVE);

        // Save this user in db
        user = userService.register(user, executive.getEmail());

        // Attach this user back to Executive
        executive.setUser(user);

        // Save executive in DB
        return executiveRepository.save(executive);
    }


    /**
     * @aim Update an existing executive
     * @description This method will update an existing executive's details.
     * @param email
     * @param executive
     * @return Executive
     */

    public Executive updateExecutive(String email, Executive executive) {
        executiveUtility.validateExecutive(executive);
        logger.debug("Validating executive: {}", executive);

        // Find existing executive
        Executive existingExecutive = executiveRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Executive not found with email: " + email));
        logger.info("Found existing executive with email: {}", email);

        // Update details
        existingExecutive.setName(executive.getName());
        existingExecutive.setEmail(executive.getEmail());
        existingExecutive.setDesignation(executive.getDesignation());

        // Save updated executive in DB
        return executiveRepository.save(existingExecutive);
    }

    /**
     * @aim Upload executive image
     * @description This method will upload an image for the executive.
     * It will check the file extension and size before uploading.
     * @param email
     * @param file
     * @return String
     * @throws IOException
     */

    public String uploadExecutiveImage(String email, MultipartFile file) throws IOException {
        Executive executive = executiveRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Executive not found with email: " + email));
        logger.info("Uploading image for executive: {}", executive.getEmail());

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
        executive.setProfilePic(originalFileName);
        logger.info("Setting profile picture for executive: {}", executive.getProfilePic());

        /* Save driver with profile picture */
        executiveRepository.save(executive);
        logger.info("Executive profile updated with new image: {}", executive.getEmail());

        return "Image uploaded successfully";
    }

    /**
     * @aim Get executive profile
     * @description This method retrieves the executive profile based on the email.
     * @param email
     * @return Executive
     * @throws ResourceNotFoundException if no executive is found with the given email.
     */

    public Executive getExecutiveProfile(String email) {
        Executive executive = executiveRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Executive not found with email: " + email));
        return executive;
    }
    
}
