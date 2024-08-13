package com.mballen.demo_park_api.excption;

public class EntityNotFoundException extends RuntimeException{
    
    public EntityNotFoundException(String message){
        super(message);
    }
}
