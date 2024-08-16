package com.mballen.demo_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper; 

import com.mballen.demo_park_api.entity.Cliente;
import com.mballen.demo_park_api.web.dto.ClienteCreateDto;
import com.mballen.demo_park_api.web.dto.ClienteResponseDto;

import lombok.*; 

// definir com private, por que a classe vai ter metodo static e a classe não vai pode ser instanciada
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ClienteMapper {
    
    public static Cliente toCliente(ClienteCreateDto dto){
        return new ModelMapper().map(dto, Cliente.class);
    }

    public static ClienteResponseDto toClienteResponseDto(Cliente cliente){
        return new ModelMapper().map(cliente, ClienteResponseDto.class);
    }
}
