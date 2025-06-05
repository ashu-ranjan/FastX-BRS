package com.backend.fastx;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptor {
    public static void main(String[] args) {
        String rawPassword = "Ashutosh@108";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}

/*
* CEO email : ashu.ceo@fastx.com
* CEO pass  : Ashutosh@108
* */

