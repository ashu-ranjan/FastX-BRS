package com.backend.fastx;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncryptor {
    public static void main(String[] args) {
        String rawPassword = "AshuCEO108";
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String encodedPassword = encoder.encode(rawPassword);
        System.out.println(encodedPassword);
    }
}

/*
* CEO email : ceo.ashu@fastx.com
* CEO pass  : AshuCEO108
* */

