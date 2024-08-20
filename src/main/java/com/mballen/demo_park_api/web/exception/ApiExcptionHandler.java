package com.mballen.demo_park_api.web.exception;



import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mballen.demo_park_api.excption.CodigoUniqueViolationException;
import com.mballen.demo_park_api.excption.CpfUniqueViolationException;
import com.mballen.demo_park_api.excption.EntityNotFoundException;
import com.mballen.demo_park_api.excption.PasswordInvalidException;
import com.mballen.demo_park_api.excption.UsernameUniqueViolationExcpion;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestControllerAdvice
public class ApiExcptionHandler {
    
    
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage>  accessDeniedException(AccessDeniedException ex, HttpServletRequest request){
       
        log.error("Api Error - ", ex);// Consegue ver no console onde ocorreu o erro
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN) // status 409, indica que teve conflido ao inserir dado no banco, ex campo unico
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.FORBIDDEN, ex.getMessage())); 
    }

    @ExceptionHandler(PasswordInvalidException.class)
    public ResponseEntity<ErrorMessage>  passwordInvalidException(RuntimeException ex, HttpServletRequest request){
       
        log.error("Api Error - ", ex);// Consegue ver no console onde ocorreu o erro
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST) // status 409, indica que teve conflido ao inserir dado no banco, ex campo unico
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, ex.getMessage())); 
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorMessage>  entityNotFoundException(RuntimeException ex, HttpServletRequest request){
       
        log.error("Api Error - ", ex);// Consegue ver no console onde ocorreu o erro
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND) // status 409, indica que teve conflido ao inserir dado no banco, ex campo unico
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ErrorMessage(request, HttpStatus.NOT_FOUND, ex.getMessage())); 
    }

    @ExceptionHandler({UsernameUniqueViolationExcpion.class, CpfUniqueViolationException.class,CodigoUniqueViolationException.class})
    public ResponseEntity<ErrorMessage> usernameUniqueViolationExcpion(RuntimeException ex, HttpServletRequest request){
       
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

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> internalServerErrorException(Exception ex, HttpServletRequest request){
       ErrorMessage error = new ErrorMessage(
        request, HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()
       );

        log.error("Internal Server Error {} {}",error, ex.getMessage());// Consegue ver no console onde ocorreu o erro
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(error);
    }
}
