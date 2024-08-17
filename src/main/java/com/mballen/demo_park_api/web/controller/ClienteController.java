package com.mballen.demo_park_api.web.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus; 
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.mballen.demo_park_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Clientes", description = "Contém todas as operações relativas ao recurso de um cliente")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/clientes")
public class ClienteController  {
    
    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @Operation(summary = "Criar um novo cliente", description = "Recurso para criar um novo cliente vinculado a um usuário cadastrado."+
                        "Requisição exige uso de um bearer token. Acesso restrito a Role='CLIENTE'",
            
                responses = {
                    @ApiResponse(responseCode = "201", description="Recurso criado com sucesso",
                    content = @Content(mediaType = "application/json",  schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "409", description="Cliente CPF já cadastrado no sistema",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))), 
                    @ApiResponse(responseCode = "422", description="Recurso não processado por falta de dados ou dados inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))), 
                    @ApiResponse(responseCode = "403", description="Recurso não permito ao perfil de 'ADMIN'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),                     
                })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDto> create(@RequestBody @Valid ClienteCreateDto dto, @AuthenticationPrincipal JwtUserDetails userDetails){

        Cliente cliente = ClienteMapper.toCliente(dto);
        cliente.setUsuario(usuarioService.getById(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(HttpStatus.CREATED).body(ClienteMapper.toClienteResponseDto(cliente));
    }

    @Operation(summary = "Localizar um cliente", description = "Recurso para Localizar um novo cliente pelo ID."+
                        "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
            
                responses = {
                    @ApiResponse(responseCode = "200", description="Recurso localizado com sucesso",
                    content = @Content(mediaType = "application/json",  schema = @Schema(implementation = ClienteResponseDto.class))),
                    @ApiResponse(responseCode = "404", description="Cliente não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),  
                    @ApiResponse(responseCode = "403", description="Recurso não permito ao perfil de 'CLIENTE'",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),                     
                })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDto> getById(@PathVariable Long id){
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toClienteResponseDto(cliente));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<Cliente>> getAll( Pageable pageable){
        Page<Cliente> cliente = clienteService.buscarTodos( pageable);
        return ResponseEntity.ok(cliente);
    }
}
