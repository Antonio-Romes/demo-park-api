package com.mballen.demo_park_api.excption;

public class PasswordInvalidException extends RuntimeException {
    
    public PasswordInvalidException(String message){
        super(message);
    }
}
