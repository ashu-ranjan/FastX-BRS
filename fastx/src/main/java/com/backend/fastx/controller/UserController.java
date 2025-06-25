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

    /*
     * AIM: insert the user in the DB with password encrypted.
     * PATH: /fastx/api/user/register
     * PARAM: @RequestBody User user
     * Response: User
     * METHOD: POST
     */
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        String email = user.getUsername();
        return userService.register(user, email);
    }

    /*
     * AIM: Getting token after logging in
     * PATH: /fastx/api/user/token
     * PARAM: Principal principal
     * Response: Responsive<?>
     * METHOD: GET
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
