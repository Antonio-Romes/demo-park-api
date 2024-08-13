package com.mballen.demo_park_api.web.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mballen.demo_park_api.entity.Usuario;
import com.mballen.demo_park_api.service.UsuarioService;
import com.mballen.demo_park_api.web.dto.UsuarioCreateDto;
import com.mballen.demo_park_api.web.dto.UsuarioResponseDto;
import com.mballen.demo_park_api.web.dto.UsuarioSenhaDto;
import com.mballen.demo_park_api.web.dto.mapper.UsuarioMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    @GetMapping 
    public ResponseEntity<List<UsuarioResponseDto>> getAll(){

        List<Usuario> usuario = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMapper.toListDto(usuario));
    }

    @GetMapping("/{id}") 
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id){

        Usuario usuario = usuarioService.getById(id);
        return ResponseEntity.ok( UsuarioMapper.toUsuarioResponseDto(usuario));
    }

    @PatchMapping("/{id}") 
    public ResponseEntity<Void> updatePassword( @PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto){

        usuarioService.editarSenha(id, dto.getSenhaAtual(),dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @PostMapping 
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto usuarioCreateDto){

        Usuario _usuario = usuarioService.salvar( UsuarioMapper.toUsuario(usuarioCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toUsuarioResponseDto(_usuario));
    }


   
}
