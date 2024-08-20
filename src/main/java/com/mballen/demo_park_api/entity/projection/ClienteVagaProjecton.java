package com.mballen.demo_park_api.entity.projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public interface ClienteVagaProjecton {

    String getPlaca(); 
    String getMarca(); 
    String getModelo(); 
    String getCor(); 
    String getRecibo(); 

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDataEntrada(); 

    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    LocalDateTime getDataSaida(); 
    String getVagaCodigo(); 
    BigDecimal getValor(); 
    BigDecimal getDesconto(); 
}
