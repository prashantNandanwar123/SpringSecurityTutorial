package com.security.controller;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class A {
    public static void main(String[] args) {
        String password = "Spay@123";
        
        // Create an instance of BCryptPasswordEncoder
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        // Encode the password
        String encodedPassword = passwordEncoder.encode(password);
        
        // Print the encoded password
        System.out.println("Encoded Password: " + encodedPassword);
    }
}
