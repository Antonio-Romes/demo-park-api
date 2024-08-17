package com.mballen.demo_park_api.web.dto.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;

import com.mballen.demo_park_api.web.dto.PageableDto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PageableMApper {
    
    public static PageableDto tDto(Page page){
        return new ModelMapper().map(page, PageableDto.class);
    }
}
