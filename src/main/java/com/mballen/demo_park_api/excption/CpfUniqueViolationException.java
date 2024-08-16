package com.mballen.demo_park_api.excption;

public class CpfUniqueViolationException extends RuntimeException{

    public CpfUniqueViolationException(String message){
        super(message);
    }
}
