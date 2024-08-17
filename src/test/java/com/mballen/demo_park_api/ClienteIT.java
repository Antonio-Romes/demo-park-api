package com.mballen.demo_park_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mballen.demo_park_api.web.dto.ClienteCreateDto;
import com.mballen.demo_park_api.web.dto.ClienteResponseDto;
import com.mballen.demo_park_api.web.dto.PageableDto;
import com.mballen.demo_park_api.web.exception.ErrorMessage;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/clientes/cliente-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {
    
     @Autowired
    WebTestClient testClient;

    @Test
    public void criarCliente_ComDadosValidos_RetornarClienteComStatus201(){
        ClienteResponseDto responseBody =  testClient
            .post()
            .uri("/api/v1/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "toby@email.com", "123456"))
            .bodyValue(new ClienteCreateDto("Tobias Ferreira", "39430153005"))
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ClienteResponseDto.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getNome()).isEqualTo("Tobias Ferreira"); 
            org.assertj.core.api.Assertions.assertThat(responseBody.getCpf()).isEqualTo("39430153005");
    }

    @Test
    public void criarCliente_ComCpfJaCadastrado_RetornarErrorMessageComStatus409(){
        ErrorMessage responseBody =  testClient
            .post()
            .uri("/api/v1/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "toby@email.com", "123456"))
            .bodyValue(new ClienteCreateDto("Tobias Ferreira", "51224756010"))
            .exchange()
            .expectStatus().isEqualTo(409)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);  
    }

    @Test
    public void criarCliente_ComDadosInvalidos_RetornarErrorMessageComStatus422(){
        ErrorMessage responseBody =  testClient
            .post()
            .uri("/api/v1/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "toby@email.com", "123456"))
            .bodyValue(new ClienteCreateDto("", ""))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);  

            responseBody =  testClient
            .post()
            .uri("/api/v1/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "toby@email.com", "123456"))
            .bodyValue(new ClienteCreateDto("toby", "00000000000"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422); 

            responseBody =  testClient
            .post()
            .uri("/api/v1/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "toby@email.com", "123456"))
            .bodyValue(new ClienteCreateDto("toby", "394.301.530-05"))
            .exchange()
            .expectStatus().isEqualTo(422)
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }

    @Test
    public void criarCliente_ComUsuarioNaoPermitido_RetornarErrorMessageComStatus403(){
        ErrorMessage responseBody =  testClient
            .post()
            .uri("/api/v1/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "ana@email.com", "123456"))
            .bodyValue(new ClienteCreateDto("Tobias Ferreira", "39430153005"))
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);  
    }

    @Test
    public void buscarCliente_ComIdExistentePeloAdmin_RetornarClienteComStatus200(){
        ClienteResponseDto responseBody =  testClient
            .get()
            .uri("/api/v1/clientes/10") 
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "ana@email.com", "123456"))  
            .exchange()
            .expectStatus().isOk()
            .expectBody(ClienteResponseDto.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getId()).isEqualTo(10);   
    }

    @Test
    public void buscarCliente_ComIdInexistentePeloAdmin_RetornarErrorMessageComStatus404(){
        ErrorMessage responseBody =  testClient
            .get()
            .uri("/api/v1/clientes/0") 
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "ana@email.com", "123456"))  
            .exchange()
            .expectStatus().isNotFound()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);   
    }

    @Test
    public void buscarCliente_ComIdExistentePeloCliente_RetornarErrorMessageComStatus403(){
        ErrorMessage responseBody =  testClient
            .get()
            .uri("/api/v1/clientes/0") 
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "bia@email.com", "123456"))  
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);   
    }

    @Test
    public void buscarClientes_ComPaginacaoPeloAdmin_RetornarClientesComStatus200(){
        PageableDto responseBody =  testClient
            .get()
            .uri("/api/v1/clientes") 
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "ana@email.com", "123456"))  
            .exchange()
            .expectStatus().isOk()
            .expectBody(PageableDto.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(2);   
            org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(0);   
            org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(1); 
            
            responseBody =  testClient
            .get()
            .uri("/api/v1/clientes?size=1&page=1") 
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "ana@email.com", "123456"))  
            .exchange()
            .expectStatus().isOk()
            .expectBody(PageableDto.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getContent().size()).isEqualTo(1);   
            org.assertj.core.api.Assertions.assertThat(responseBody.getNumber()).isEqualTo(1);   
            org.assertj.core.api.Assertions.assertThat(responseBody.getTotalPages()).isEqualTo(2); 
    }

    @Test
    public void buscarClientes_ComPaginacaoPeloCliente_RetornarErrorMessageComStatus403(){
        ErrorMessage responseBody =  testClient
            .get()
            .uri("/api/v1/clientes") 
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "bia@email.com", "123456"))  
            .exchange()
            .expectStatus().isForbidden()
            .expectBody(ErrorMessage.class)
            .returnResult().getResponseBody();

            org.assertj.core.api.Assertions.assertThat(responseBody).isNotNull();  
            org.assertj.core.api.Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);   
            
    }
}
