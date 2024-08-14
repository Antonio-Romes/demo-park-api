package com.mballen.demo_park_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mballen.demo_park_api.web.dto.UsuarioCreateDto;
import com.mballen.demo_park_api.web.dto.UsuarioResponseDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/usuarios/usuario-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UsuarioIT {
    
    @Autowired
    WebTestClient testClient;

    @Test
    public void createUsuario_ComUsernameePasswordValidos_RetornarUsuarioCriadoComStatus201(){
       UsuarioResponseDto responseDto =  testClient
            .post()
            .uri("/api/v1/usuarios")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioCreateDto("felipe@email.com","123456"))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(UsuarioResponseDto.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseDto.getId()).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseDto.getUsername()).isEqualTo("felipe@email.com");
            org.assertj.core.api.Assertions.assertThat(responseDto.getRole()).isEqualTo("CLIENTE"); 

    }
}
