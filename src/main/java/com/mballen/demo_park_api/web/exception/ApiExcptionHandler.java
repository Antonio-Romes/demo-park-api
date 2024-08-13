package com.mballen.demo_park_api.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mballen.demo_park_api.excption.UsernameUniqueViolationExcpion;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestControllerAdvice
public class ApiExcptionHandler {
    
    @ExceptionHandler(UsernameUniqueViolationExcpion.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(RuntimeException ex, HttpServletRequest request){
       
        log.error("Api Error - ", ex);// Consegue ver no console onde ocorreu o erro
        return ResponseEntity
                .status(HttpStatus.CONFLICT) // status 409, indica que teve conflido ao inserir dado no banco, ex campo unico
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.CONFLICT, ex.getMessage())); 
    }

    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessage> methodArgumentNotValidException(MethodArgumentNotValidException ex, HttpServletRequest request, BindingResult result){
       
        log.error("Api Error - ", ex);// Consegue ver no console onde ocorreu o erro
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.UNPROCESSABLE_ENTITY, "Campo(s) invalido(s).", result )); // pode pegar a mensagem de erro da seguinte forma ex.getMessage()
    }
}
