package com.mballen.demo_park_api.excption;

public class CodigoUniqueViolationException extends RuntimeException {
    
    public CodigoUniqueViolationException(String message){
        super(message);
    }
}
