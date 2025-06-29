package com.backend.fastx.controller;

import com.backend.fastx.model.User;
import com.backend.fastx.service.UserService;
import com.backend.fastx.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fastx/api/user")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private JwtUtility jwtUtility;

    /**
     * @aim Register a new user
     * @description This method allows a new user to register by providing their details.
     * @path /fastx/api/user/register
     * @method POST
     * @param user The user object containing the registration details.
     * @return User
     */
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        String email = user.getUsername();
        return userService.register(user, email);
    }

    /**
     * @aim Login a user
     * @description This method allows a user to log in by providing their username and password.
     * @path /fastx/api/user/login
     * @method POST
     * @param user The user object containing the login credentials.
     * @return ResponseEntity containing the JWT token if login is successful.
     */

    @GetMapping("/token")
    public ResponseEntity<?> getToken(Principal principal) {
        try {
            String token = jwtUtility.createToken(principal.getName());
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    /**
     * @aim Get logged-in user details
     * @description This method retrieves the details of the currently logged-in user.
     * @path /fastx/api/user/details
     * @method GET
     * @param principal The principal object representing the authenticated user.
     * @return ResponseEntity containing the user details or an error message if not found.
     */
    @GetMapping("/details")
    public Object getLoggedInUserDetails(Principal principal) {
        String username = principal.getName();// logged In username
        Object userInfo = userService.getUserInfo(username);
        if (userInfo == null)
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "User not found or role is invalid"));
        return ResponseEntity.ok(userInfo);
    }
}
