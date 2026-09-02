package com.antony.benchmate.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordGenerator {

    public static void main(String[] args) {

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("Arun: " + encoder.encode("123456"));
        System.out.println("Priya: " + encoder.encode("123456"));
        System.out.println("Admin: " + encoder.encode("123456"));
        System.out.println("REP: " + encoder.encode("123456"));
    }
}


