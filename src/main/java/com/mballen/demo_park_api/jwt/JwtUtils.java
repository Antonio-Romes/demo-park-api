package com.mballen.demo_park_api.jwt;

import java.nio.charset.StandardCharsets; 
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {
    
    public static final String JWT_BEARER = "Bearer ";
    public static final String JWT_AUTHORIZATION = "Authorization";
    private static final String SECRET_KEY = "jfhdgoi-jfhgewu-aldoifnenh";
    private static final Long EXPIRE_DAYS = (long) 0;
    private static final Long EXPIRE_HOURS = (long)0;
    private static final Long EXPIRE_MINUTES = (long) 30; 

    private JwtUtils(){

    }

    private static Algorithm generateKey(){
        return  Algorithm.HMAC256(SECRET_KEY.getBytes(StandardCharsets.UTF_8));
    }

    private static Date toExpireDate(Date start){
          LocalDateTime dateTime = start.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
          LocalDateTime end = dateTime.plusDays(EXPIRE_DAYS).plusHours(EXPIRE_HOURS).plusMinutes(EXPIRE_MINUTES); 
          return Date.from(end.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static JwtToken createToken(String username, String role){

        Date issuedAt = new Date();
        Date limit = toExpireDate(issuedAt);

        Algorithm algorithm = Algorithm.HMAC256(generateKey().toString());
        JWTCreator.Builder builder = JWT.create()
                .withSubject(username)
                .withIssuedAt(issuedAt)
                .withExpiresAt(limit)
                .withClaim("role", role);

         try {
            String token = builder.sign(algorithm);
            return new JwtToken(token);
        } catch (JWTCreationException e) {
            log.error("Erro ao criar token", e.getMessage());
            return null;
        }
    }

    public static DecodedJWT getDecodedToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(generateKey().toString());
            return JWT.require(algorithm).build().verify(refactorToken(token));
        } catch (JWTVerificationException e) {
            log.error("Token inválido", e.getMessage());
            return null;
        }
    }

    public static String getUsernameFromToken(String token){
        DecodedJWT decodedJWT = getDecodedToken(token);
        return decodedJWT != null ? decodedJWT.getSubject() : null;
    }

    public static boolean isTokenValid(String token){
        return getDecodedToken(token) != null;
    }

    private static String refactorToken(String token){
        if(token.contains(JWT_BEARER)){
            return token.substring(JWT_BEARER.length());
        }
        return token;

    }
    
    
                
            
}
