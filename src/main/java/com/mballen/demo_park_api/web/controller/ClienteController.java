package com.mballen.demo_park_api.web.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mballen.demo_park_api.entity.Cliente;
import com.mballen.demo_park_api.jwt.JwtUserDetails;
import com.mballen.demo_park_api.service.ClienteService;
import com.mballen.demo_park_api.service.UsuarioService;
import com.mballen.demo_park_api.web.dto.ClienteCreateDto;
import com.mballen.demo_park_api.web.dto.ClienteResponseDto;
import com.mballen.demo_park_api.web.dto.mapper.ClienteMapper;
 
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController  {
    
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto, @AuthenticationPrincipal JwtUserDetails userDetails){

        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.getById(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toClienteResponseDto(cliente));
    }
}
