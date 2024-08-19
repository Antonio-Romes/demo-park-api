package com.mballen.demo_park_api;

 

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mballen.demo_park_api.web.dto.EstacionamentoCreateDto;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/sql/estacionamentos/estacionamentos-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/sql/estacionamentos/estacionamento-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class EstacionamentoIT {
      @Autowired
    WebTestClient testClient;

    @Test
    public void criaCheckIn_ComDadosValidos_RetornarCreateAndLocation(){

        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
            .placa("WER-1111").marca("FIAT").modelo("PALIO 1.0").cor("AZUL").clienteCpf("48810691016")
            .build();

            testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"ana@email.com", "123456"))
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isCreated()
                .expectHeader().exists(HttpHeaders.LOCATION)
                .expectBody()
                .jsonPath("placa").isEqualTo("WER-1111")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO 1.0") 
                .jsonPath("cor").isEqualTo("AZUL") 
                .jsonPath("dataEntrada").exists()
                .jsonPath("recibo").exists()
                .jsonPath("vagaCodigo").exists();
    }

    @Test
    public void criaCheckIn_ComRoleCliente_RetornarErrorComStatus403(){

        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
            .placa("WER-1111").marca("FIAT").modelo("PALIO 1.0").cor("AZUL").clienteCpf("48810691016")
            .build();

            testClient
                .post() 
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"bia@email.com", "123456")) 
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isForbidden() 
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST") ;
    }

    @Test
    public void criaCheckIn_ComDadosInvalidos_RetornarErrorComStatus422(){

        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
            .placa("").marca("").modelo("").cor("").clienteCpf("")
            .build();

            testClient
                .post() 
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"bia@email.com", "123456")) 
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isEqualTo(422) 
                .expectBody()
                .jsonPath("status").isEqualTo("422")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST") ;
    }

    @Test
    public void criaCheckIn_ComCpfInexistente_RetornarErrorComStatus404(){

        EstacionamentoCreateDto createDto = EstacionamentoCreateDto.builder()
            .placa("WER-1111").marca("FIAT").modelo("PALIO 1.0").cor("AZUL").clienteCpf("99572896083")
            .build();

            testClient
                .post() 
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"ana@email.com", "123456")) 
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound() 
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST") ;
    }
}
