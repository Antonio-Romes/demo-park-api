package com.mballen.demo_park_api.web.dto.mapper;
  

import org.modelmapper.ModelMapper; 

import com.mballen.demo_park_api.entity.Usuario;
import com.mballen.demo_park_api.web.dto.UsuarioCreateDto;
import com.mballen.demo_park_api.web.dto.UsuarioResponseDto;

public class UsuarioMapper {

    public static Usuario toUsuario(UsuarioCreateDto usuarioCreateDto){
        return new ModelMapper().map(usuarioCreateDto, Usuario.class);
    }

    public static UsuarioResponseDto toUsuarioResponseDto(Usuario usuario){
       
        ModelMapper mapper = new ModelMapper();
        UsuarioResponseDto usuarioResponseDto = mapper.map(usuario, UsuarioResponseDto.class);
        usuarioResponseDto.setRole(usuario.getRole().name().substring("ROLE_".length()));
        return usuarioResponseDto; 
    }

}
