package com.mballen.demo_park_api.excption;

public class UsernameUniqueViolationExcpion extends RuntimeException {
    
    public UsernameUniqueViolationExcpion (String message){
        super(message);
    }
}
