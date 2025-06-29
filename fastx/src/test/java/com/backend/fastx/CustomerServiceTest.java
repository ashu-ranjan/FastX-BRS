package com.backend.fastx;

import com.backend.fastx.enums.Gender;
import com.backend.fastx.enums.Role;
import com.backend.fastx.exception.ResourceNotFoundException;
import com.backend.fastx.model.Customer;
import com.backend.fastx.model.User;
import com.backend.fastx.repository.CustomerRepository;
import com.backend.fastx.service.CustomerService;
import com.backend.fastx.service.UserService;
import com.backend.fastx.utility.CustomerUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class CustomerServiceTest {

    @InjectMocks
    private CustomerService customerService;

    @Mock private CustomerRepository customerRepository;
    @Mock private CustomerUtility customerUtility;
    @Mock private UserService userService;

    private Customer customer;
    private User user;

    @BeforeEach
    public void setup() {
        user = new User();
        user.setUsername("testuser");
        user.setRole(Role.CUSTOMER);

        customer = new Customer();
        customer.setId(1);
        customer.setName("John Doe");
        customer.setEmail("john@example.com");
        customer.setContact("1234567890");
        customer.setAddress("123 Street");
        customer.setGender(Gender.MALE);
        customer.setUser(user);
    }

    @Test
    public void testAddCustomer_Success() {
        when(userService.register(any(User.class), eq(customer.getEmail()))).thenReturn(user);
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.addCustomer(customer);

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        verify(customerUtility).validateCustomer(customer);
        verify(userService).register(user, customer.getEmail());
        verify(customerRepository).save(customer);
    }

    @Test
    public void testGetCustomerIdFromEmail_Success() {
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));

        int id = customerService.getCustomerIdFromEmail("john@example.com");

        assertEquals(1, id);
    }

    @Test
    public void testGetCustomerIdFromEmail_NotFound() {
        when(customerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerIdFromEmail("notfound@example.com"));
    }

    @Test
    public void testUpdateCustomer_Success() {
        Customer updated = new Customer();
        updated.setName("Updated Name");
        updated.setEmail("newemail@example.com");
        updated.setContact("1111111111");
        updated.setAddress("New Address");
        updated.setGender(Gender.FEMALE);

        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        Customer result = customerService.updateCustomer("john@example.com", updated);

        assertEquals("Updated Name", result.getName());
        assertEquals("newemail@example.com", result.getEmail());
    }

    @Test
    public void testUploadCustomerImage_Success() throws IOException {
        MultipartFile file = new MockMultipartFile("profile", "image.jpg", "image/jpeg", new byte[1024]);

        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        String result = customerService.uploadCustomerImage("john@example.com", file);

        assertEquals("Image uploaded successfully", result);
    }

    @Test
    public void testUploadCustomerImage_InvalidExtension() {
        MultipartFile file = new MockMultipartFile("profile", "file.exe", "application/octet-stream", new byte[1024]);
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                customerService.uploadCustomerImage("john@example.com", file));

        assertTrue(exception.getMessage().contains("Invalid file extension"));
    }

    @Test
    public void testUploadCustomerImage_TooLarge() {
        byte[] largeFile = new byte[4000 * 1024];
        MultipartFile file = new MockMultipartFile("profile", "large.png", "image/png", largeFile);
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));

        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                customerService.uploadCustomerImage("john@example.com", file));

        assertTrue(exception.getMessage().contains("File size exceeds"));
    }

    @Test
    public void testGetCustomerByEmail_Success() {
        when(customerRepository.findByEmail("john@example.com")).thenReturn(Optional.of(customer));

        Customer result = customerService.getCustomerByEmail("john@example.com");

        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    public void testGetCustomerByEmail_NotFound() {
        when(customerRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerByEmail("notfound@example.com"));
    }
}
