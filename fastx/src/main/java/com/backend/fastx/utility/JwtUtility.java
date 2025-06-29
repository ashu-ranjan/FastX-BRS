package com.backend.fastx.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component // tell spring to create an object of this class and use it wherever required
public class JwtUtility {
    /*
     * In this class we need to create following methods
     * 1. Method ro Create JWT Token
     * 2. Method to verify the Token
     * 3. Method to extract username/email from the token
     * */

    // Secret key for signing the JWT token
    // This should be kept secret and secure, ideally stored in environment variables or a secure vault
    String secretKey = "FASTX_HEX_MAY_2025_626126108786099786";

    /* Method to get the signing key */
    // This method returns a Key object that is used to sign the JWT token
    private Key getSigningKey(){
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    /* Method to create Token */
    public String createToken(String email){
        long expirationTimeInMills = 43200000; // 12 hrs
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMills))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes()), SignatureAlgorithm.HS256)
                .compact();
    }

    /* Method to verify Token */
    public boolean verifyToken(String token, String email){
        String extractedEmail = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
        Date expirationDate = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        return extractedEmail.equals(email) && new Date().before(expirationDate);
    }

    /* Method to extract username/email from the token */
    public String extractUsername(String token) {
        return  Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
