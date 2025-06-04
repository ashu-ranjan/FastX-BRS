package com.backend.fastx.controller;

import com.backend.fastx.model.User;
import com.backend.fastx.service.UserService;
import com.backend.fastx.utility.JwtUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/fastx/api/user")
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
     * */
    @PostMapping("/register")
    public User register(@RequestBody User user){
        String email = user.getUsername();
        return userService.register(user,email);
    }

    /*
     * AIM: Getting token after logging in
     * PATH: /fastx/api/user/token
     * PARAM: Principal principal
     * Response: Responsive<?>
     * METHOD: GET
     * */

    @GetMapping("/token")
    public ResponseEntity<?> getToken(Principal principal){
        try {
            String token = jwtUtility.createToken(principal.getName());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
