package com.mballen.demo_park_api;
import org.springframework.http.HttpHeaders;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mballen.demo_park_api.web.dto.VagaCreateDto;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/vagas/vagas-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/vagas/vaga-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagaIT {
    
    @Autowired
    WebTestClient testClient;


    @Test
    public void criarVaga_ComDadosValidos_RetornarLocationComStatus201(){
        
        testClient
            .post()
            .uri("/api/v1/vagas")
            .contentType(MediaType.APPLICATION_JSON)
            .headers(JwtAuthentication.getHeaderAuthentication(testClient, "ana@email.com", "123456")) 
            .bodyValue(new VagaCreateDto("A-05","LIVRE"))
            .exchange()
            .expectStatus().isCreated() 
            .expectHeader().exists(HttpHeaders.LOCATION);
    }
}
