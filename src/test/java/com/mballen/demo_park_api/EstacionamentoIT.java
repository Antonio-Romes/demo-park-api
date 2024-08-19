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
            .placa("WER-1111").marca("FIAT").modelo("PALIO 1.0")
            .cor("AZUL").clienteCpf("09191773016")
            .build();

            testClient
                .post()
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"ana@email.com.br", "123456"))
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
            .placa("WER-1111").marca("FIAT").modelo("PALIO 1.0")
            .cor("AZUL").clienteCpf("09191773016")
            .build();

            testClient
                .post() 
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"bia@email.com.br", "123456")) 
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
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"bia@email.com.br", "123456")) 
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
            .placa("WER-1111").marca("FIAT").modelo("PALIO 1.0")
            .cor("AZUL").clienteCpf("99572896083")
            .build();

            testClient
                .post() 
                .uri("/api/v1/estacionamentos/check-in")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"ana@email.com.br", "123456")) 
                .bodyValue(createDto)
                .exchange()
                .expectStatus().isNotFound() 
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in")
                .jsonPath("method").isEqualTo("POST") ;
    }

    @Test
    public void buscarCheckIn_ComPerfilAdmin_RetornarDadosComStatus200(){
  
            testClient
                .get() 
                .uri("/api/v1/estacionamentos/check-in/{recibo}","20230313-101300") 
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"ana@email.com.br", "123456"))  
                .exchange()
                .expectStatus().isOk() 
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO") 
                .jsonPath("cor").isEqualTo("VERDE") 
                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("recibo").isEqualTo("20230313-101300")
                .jsonPath("vagaCodigo").isEqualTo("A-01");
    }

    @Test
    public void buscarCheckIn_ComPerfilCliente_RetornarDadosComStatus200(){
  
            testClient
                .get() 
                .uri("/api/v1/estacionamentos/check-in/{recibo}","20230313-101300") 
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"bia@email.com.br", "123456"))  
                .exchange()
                .expectStatus().isOk() 
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO") 
                .jsonPath("cor").isEqualTo("VERDE") 
                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("recibo").isEqualTo("20230313-101300")
                .jsonPath("vagaCodigo").isEqualTo("A-01");
    }

    @Test
    public void buscarCheckIn_ComReciboInexistente_RetornarErrorMessageComStatus404(){
  
            testClient
                .get() 
                .uri("/api/v1/estacionamentos/check-in/{recibo}","20230313-999999") 
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"bob@email.com.br", "123456"))  
                .exchange()
                .expectStatus().isNotFound() 
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-in/20230313-999999")
                .jsonPath("method").isEqualTo("GET") ;
     
    }
 

    @Test
    public void criarCheckOut_ComReciboEexistente_RetornaSucessoComStatus200(){
  
            testClient
                .put() 
                .uri("/api/v1/estacionamentos/check-out/{recibo}","20230313-101300") 
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"ana@email.com.br", "123456"))  
                .exchange()
                .expectStatus().isOk() 
                .expectBody()
                .jsonPath("placa").isEqualTo("FIT-1020")
                .jsonPath("marca").isEqualTo("FIAT")
                .jsonPath("modelo").isEqualTo("PALIO") 
                .jsonPath("cor").isEqualTo("VERDE") 
                .jsonPath("dataEntrada").isEqualTo("2023-03-13 10:15:00")
                .jsonPath("recibo").isEqualTo("20230313-101300")
                .jsonPath("vagaCodigo").isEqualTo("A-01")
                .jsonPath("dataSaida").exists()
                .jsonPath("valor").exists()
                .jsonPath("desconto").exists() ; 
     
    }

    @Test
    public void criarCheckOut_ComReciboInexistente_RetornaSucessoComStatus404(){
  
            testClient
                .put() 
                .uri("/api/v1/estacionamentos/check-out/{recibo}","20230313-000000") 
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"ana@email.com.br", "123456"))  
                .exchange()
                .expectStatus().isNotFound() 
                .expectBody()
                .jsonPath("status").isEqualTo("404")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20230313-000000")
                .jsonPath("method").isEqualTo("PUT") ;
     
    }

    @Test
    public void criarCheckOut_ComRoleCliente_RetornaErrorMessageComStatus403(){
  
            testClient
                .put() 
                .uri("/api/v1/estacionamentos/check-out/{recibo}","20230313-101300") 
                .headers(JwtAuthentication.getHeaderAuthentication(testClient,"bia@email.com.br", "123456"))  
                .exchange()
                .expectStatus().isForbidden() 
                .expectBody()
                .jsonPath("status").isEqualTo("403")
                .jsonPath("path").isEqualTo("/api/v1/estacionamentos/check-out/20230313-101300")
                .jsonPath("method").isEqualTo("PUT") ;
     
    }

}
