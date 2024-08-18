package com.mballen.demo_park_api;

 

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.mballen.demo_park_api.web.dto.EstacionamentoCreateDto;

import org.springframework.http.HttpHeaders;


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
}
