package com.mballen.demo_park_api;


import java.util.function.Consumer;
 
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mballen.demo_park_api.jwt.JwtToken;
import com.mballen.demo_park_api.web.dto.UsuarioLoginDto;

public class JwtAuthentication {

    public static Consumer<HttpHeaders> getHeaderAuthentication(WebTestClient webTestClient, String username, String password){
        String token = webTestClient
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDto(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();

        return headers -> headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);
    }
}
