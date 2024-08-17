package com.mballen.demo_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;

import com.mballen.demo_park_api.entity.Vaga;
import com.mballen.demo_park_api.web.dto.VagaCreateDto;
import com.mballen.demo_park_api.web.dto.VagaResponseDto;

import lombok.*; 


@NoArgsConstructor(access =  AccessLevel.PRIVATE)
public class VagaMapper {
    
    public static Vaga toVaga(VagaCreateDto dto){
        return new ModelMapper().map(dto,Vaga.class);
    }

    public static VagaResponseDto toDto(Vaga vaga){
        return new ModelMapper().map(vaga,VagaResponseDto.class);
    }
}
