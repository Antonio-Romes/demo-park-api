package com.mballen.demo_park_api;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mballen.demo_park_api.web.dto.UsuarioCreateDto;
import com.mballen.demo_park_api.web.dto.UsuarioResponseDto;
import com.mballen.demo_park_api.web.dto.UsuarioSenhaDto;
import com.mballen.demo_park_api.web.exception.ErrorMessage;

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

    @Test
    public void createUsuario_ComUsernameInvalidos_RetornarErrorMessageComStatus422(){
        ErrorMessage responseBody =  testClient
            .post()
            .uri("/api/v1/usuarios")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioCreateDto("","123456"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422); 
            
            responseBody =  testClient
            .post()
            .uri("/api/v1/usuarios")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioCreateDto("tody@","123456"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUsuario_ComPasswordInvalidos_RetornarErrorMessageComStatus422(){

        ErrorMessage responseBody =  testClient
            .post()
            .uri("/api/v1/usuarios")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioCreateDto("tody@email.com",""))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422); 
            
            responseBody =  testClient
            .post()
            .uri("/api/v1/usuarios")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioCreateDto("tody@email.com","1234567"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

            responseBody =  testClient
            .post()
            .uri("/api/v1/usuarios")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioCreateDto("tody@email.com","123"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }

    @Test
    public void createUsuario_ComUsernameRepitido_RetornarErrorMessageComStatus409(){
        ErrorMessage responseBody =  testClient
            .post()
            .uri("/api/v1/usuarios")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioCreateDto("ana@email.com","123456"))
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull(); 
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409); 

    }

    @Test
    public void getUsuario_ComIdExistente_RetornarUsuarioComStatus200(){
        UsuarioResponseDto responseDto =  testClient
        .get()
        .uri("/api/v1/usuarios/100")   
        .exchange()
        .expectStatus().isOk()
        .expectBody(UsuarioResponseDto.class)
        .returnResult().getResponseBody();

    
        org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseDto.getId()).isEqualTo(100);
        org.assertj.core.api.Assertions.assertThat(responseDto.getUsername()).isEqualTo("ana@email.com");
        org.assertj.core.api.Assertions.assertThat(responseDto.getRole()).isEqualTo("ADMIN"); 

    }

    @Test
    public void getUsuario_ComIdInexistente_RetornarErrorMessageComStatus404(){
        ErrorMessage responseBody =  testClient
        .get()
        .uri("/api/v1/usuarios/0")   
        .exchange()
        .expectStatus().isNotFound()
        .expectBody(ErrorMessage.class)
        .returnResult().getResponseBody();

    
        org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
        org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404); 

    }

    @Test
    public void editarSenha_ComDadosValidos_RetornarUComStatus204(){
        testClient
            .patch()
            .uri("/api/v1/usuarios/100")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioSenhaDto("123456","101010","101010"))
            .exchange()
            .expectStatus().isNoContent(); 
    }

    @Test
    public void editarSenha_ComSenhaIsInexistente_RetornarUComStatus404(){
        ErrorMessage responseBody =  testClient
            .patch()
            .uri("/api/v1/usuarios/0")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioSenhaDto("123455","123456","123456"))
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404); 

    }

    @Test
    public void editarSenha_ComCamposInvalidos_RetornarErrorMessageComStatus422(){
        ErrorMessage responseBody =  testClient
            .patch()
            .uri("/api/v1/usuarios/100")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioSenhaDto("","",""))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422); 

            responseBody =  testClient
            .patch()
            .uri("/api/v1/usuarios/100")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioSenhaDto("12345","12345","12345"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422); 

            responseBody =  testClient
            .patch()
            .uri("/api/v1/usuarios/100")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioSenhaDto("1234567","1234567","1234567"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422); 

    }

    @Test
    public void editarSenha_ComSenhaInvalidas_RetornarErrorMessageComStatus400(){
        ErrorMessage responseBody =  testClient
            .patch()
            .uri("/api/v1/usuarios/100")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioSenhaDto("123456","123456","000000"))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400); 

            responseBody =  testClient
            .patch()
            .uri("/api/v1/usuarios/100")    
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(new UsuarioSenhaDto("000000","123456","123456"))
            .exchange()
            .expectStatus().isBadRequest()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(400); 
 
    }

    @Test
    public void listaDeUsuario_SemParametros_RetornarTodosUsuarioComStatus200(){
       List<UsuarioResponseDto> responseDto =  testClient
            .get()
            .uri("/api/v1/usuarios")     
            .exchange()
            .expectStatus().isOk()
            .expectBodyList(UsuarioResponseDto.class)
            .returnResult().getResponseBody();

        
            org.assertj.core.api.Assertions.assertThat(responseDto).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseDto.size()).isEqualTo(3);  

    }
}
