package com.mballen.demo_park_api.web.controller;

import java.net.URI;
import org.springframework.http.HttpHeaders;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.mballen.demo_park_api.entity.Vaga;
import com.mballen.demo_park_api.service.VagaService; 
import com.mballen.demo_park_api.web.dto.VagaCreateDto;
import com.mballen.demo_park_api.web.dto.VagaResponseDto;
import com.mballen.demo_park_api.web.dto.mapper.VagaMapper;
import com.mballen.demo_park_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse; 
import jakarta.validation.Valid; 
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable; 



@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vagas")
public class VagaController {
    

    private final VagaService vagaService;

     @Operation(summary = "Criar um novo vaga", 
                description = "Recurso para criar uma nova vaga."+
                        "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
                 //security = @SecurityRequirement(name = "security"),
                responses = {
                    @ApiResponse(responseCode = "201", description="Recurso criado com sucesso",
                        headers = @Header(name = HttpHeaders.LOCATION, description = "URL do recurso criado")),
                    @ApiResponse(responseCode = "409", description="Cliente CPF já cadastrado no sistema",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))), 
                    @ApiResponse(responseCode = "422", description="Recurso não processado por falta de dados ou dados inválidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),  
                })
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDto dto){
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.salvar(vaga);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri().path("/{codigo}")
                .buildAndExpand(vaga.getCodigo())
                .toUri();

            return ResponseEntity.created(location).build();
    }

    @Operation(summary = "Localizar uma vaga", 
    description = "Recurso para retornar uma vaga pelo seu código."+
            "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN'",
     //security = @SecurityRequirement(name = "security"),
    responses = {
        @ApiResponse(responseCode = "200", description="Recurso criado com sucesso",
        content = @Content(mediaType = "application/json,charset=UTF-8", schema = @Schema(implementation = VagaResponseDto.class))),
        @ApiResponse(responseCode = "404", description="Cliente CPF já cadastrado no sistema",
        content = @Content(mediaType = "application/json,charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))), 
    })
    @GetMapping("/{codigo}") 
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDto> getByCodigo(@PathVariable String codigo){
        Vaga vaga =  vagaService.buscarPorCodigo(codigo);
        return ResponseEntity.ok(VagaMapper.toDto(vaga));  
    }
}