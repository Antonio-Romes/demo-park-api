package com.mballen.demo_park_api.web.controller;
 


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mballen.demo_park_api.jwt.JwtToken;
import com.mballen.demo_park_api.jwt.JwtUserDetailsService;
import com.mballen.demo_park_api.web.dto.UsuarioLoginDto;
import com.mballen.demo_park_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;


@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {
    
    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    
    @PostMapping("/auth") 
    public ResponseEntity<?> Authentication(@RequestBody @Valid UsuarioLoginDto usuarioLoginDto, HttpServletRequest request){
        
        log.info("Processo de auteinticação pelo login {}", usuarioLoginDto.getUsername());

        try {
            
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(usuarioLoginDto.getUsername(), usuarioLoginDto.getPassword());

                authenticationManager.authenticate(authenticationToken);
                JwtToken token = detailsService.getTokenAuthenticated(usuarioLoginDto.getUsername());
                return ResponseEntity.ok(token);
        } catch (AuthenticationException ex) {
            log.warn("Bad Request Credentials from username {}", usuarioLoginDto.getUsername());
            return ResponseEntity.badRequest().body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais inválidas"));
        }
    }
}
