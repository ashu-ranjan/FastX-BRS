package com.backend.fastx.service;

import com.backend.fastx.enums.Role;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Customer;
import com.backend.fastx.model.User;
import com.backend.fastx.repository.CustomerRepository;
import com.backend.fastx.utility.CustomerUtility;

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

@Service // This annotation indicates that this class is a service component in the Spring context.
public class CustomerService {

    // Logger for logging messages in this service class
    // slf4j is a simple logging facade for Java, which allows you to plug in any logging framework.
    private static final Logger logger = LoggerFactory.getLogger(CustomerService.class);
    

    private final CustomerRepository customerRepository;
    private final CustomerUtility customerUtility;
    private final UserService userService;

    public CustomerService(CustomerRepository customerRepository, 
                            CustomerUtility customerUtility, 
                            UserService userService) {
        this.customerRepository = customerRepository;
        this.customerUtility = customerUtility;
        this.userService = userService;
    }

    /**
     * @aim add a new customer
     * @param customer
     * @return Customer
     * @description This method will add a new customer to the system. It will take the
     * customer object as input and return the created customer object.
     */

    public Customer addCustomer(Customer customer) {
        // Validate the customer object
        customerUtility.validateCustomer(customer);
        logger.debug("validating customer: {}", customer);

        // Take user out of this customer object
        User user = customer.getUser();
        logger.info("Registering user: {}", user.getUsername());

        // Give role to the user
        user.setRole(Role.CUSTOMER);
        logger.info("Setting role for user: {}", user.getRole());

        // Save this User in the DB
        user = userService.register(user, customer.getEmail());
        logger.info("User registered successfully with ID: {}", user.getId());

        // Attach this user back to customer
        customer.setUser(user);
        logger.debug("Attaching user to customer: {}", customer.getUser().getUsername());

        // Save Customer in DB
        return customerRepository.save(customer);
    }

    /**
     * @aim get customer id from email
     * @param email
     * @return int
     * @description This method will take the email of the customer and return the customer id.
     */

    public int getCustomerIdFromEmail(String email) {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(()-> new ResourceNotFoundException("Customer not found"));
        return customer.getId();
    }

    /**
     * @aim update customer details
     * @param email
     * @param customer
     * @return Customer
     * @description Update customer details by email. It will take the email of the customer and the updated customer object.
     * It will return the updated customer object.
     */

    public Customer updateCustomer(String email, Customer customer) {
        Customer existingCustomer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));

        existingCustomer.setName(customer.getName());
        existingCustomer.setEmail(customer.getEmail());
        existingCustomer.setContact(customer.getContact());
        existingCustomer.setAddress(customer.getAddress());
        existingCustomer.setGender(customer.getGender());

        return customerRepository.save(existingCustomer);
    }


    /**
     * @aim upload customer image
     * @param email
     * @param file
     * @return String
     * @throws IOException if an error occurs during file upload
     * @description This method will upload an image for the customer. 
     * It will take the customer's email and the image file as parameters.
     */

    public String uploadCustomerImage(String email, MultipartFile file) throws IOException {
        
        Customer customer = customerRepository.findByEmail(email)
            .orElseThrow(() -> new ResourceNotFoundException("Customer not found"));
        
        logger.info("Uploading image for customer: {}", customer.getEmail());

        /* extension check: jpg,jpeg,png,gif,svg : */
        String originalFileName = file.getOriginalFilename();
        logger.debug("Original file name: {}", originalFileName);

        String extension = originalFileName.substring(originalFileName.lastIndexOf(".") + 1).toLowerCase();
        if(!(List.of("jpg", "jpeg", "png", "gif", "svg").contains(extension))) {
            throw new RuntimeException("Invalid file extension. Allowed extensions are jpg, jpeg, png, gif, svg.");
        }
        logger.debug("File extension is valid: {}", extension);

        /* check file size */
        long kbs = file.getSize() / 1024;
        if (kbs > 3000) {
            throw new RuntimeException("File size exceeds 3000 KB limit.");
        }
        logger.debug("File size is within limit: {} KB", kbs);

        /* Check if Directory exists, else create one */

        String directoryPath = "D:\\FastX\\fastx-ui\\public\\images";
        Files.createDirectories(Path.of(directoryPath));
        logger.debug("Directory path for image upload: {}", directoryPath);

        /* Define full path */
        Path path = Paths.get(directoryPath,"\\",originalFileName);
        logger.debug("Full path for image upload: {}", path);

        /* Upload file in the above path */
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        logger.info("File uploaded successfully to: {}", path);

        /* Set url of file or image in driver object */
        customer.setProfilePic(originalFileName);
        logger.debug("Setting profile picture for customer: {}", customer.getProfilePic());

        /* Save customer with profile picture */
        customerRepository.save(customer);

        return "Image uploaded successfully";
    }

    /**
     * @aim get customer by email
     * @param email
     * @return Customer
     * @description This method will retrieve the details of a customer by their email.
     * It will return the customer object if found, otherwise throw ResourceNotFoundException.
     */

    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with email: " + email));
    }
}
