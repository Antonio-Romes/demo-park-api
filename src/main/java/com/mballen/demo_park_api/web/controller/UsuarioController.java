package com.mballen.demo_park_api.web.controller;

import java.util.List; 

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mballen.demo_park_api.entity.Usuario;
import com.mballen.demo_park_api.service.UsuarioService;
import com.mballen.demo_park_api.web.dto.UsuarioCreateDto;
import com.mballen.demo_park_api.web.dto.UsuarioResponseDto;
import com.mballen.demo_park_api.web.dto.UsuarioSenhaDto;
import com.mballen.demo_park_api.web.dto.mapper.UsuarioMapper;
import com.mballen.demo_park_api.web.exception.ErrorMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Tag(name="Usuarios", description = "Contém todas as operações relativas aos recurso para cadastro, edição e leitura de um usuário.")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/usuarios")
public class UsuarioController {
    
    private final UsuarioService usuarioService;

    @Operation(summary = "Recuperar todos usuário", description = "Recuperar todos usuário cadastrados",
                responses = {
                    @ApiResponse(responseCode = "200", description="Recurso todos usuários cadastrados",
                    content = @Content(mediaType = "application/json",
                    array = @ArraySchema( schema = @Schema(implementation = UsuarioResponseDto.class)))),
                     
                })
    @GetMapping 
    public ResponseEntity<List<UsuarioResponseDto>> getAll(){

        List<Usuario> usuario = usuarioService.buscarTodos();
        return ResponseEntity.ok(UsuarioMapper.toListDto(usuario));
    }

    @Operation(summary = "Recuperar usuário pelo id", description = "Recuperar usuário pelo id",
                responses = {
                    @ApiResponse(responseCode = "200", description="Recurso encontrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "404", description="Usuário não encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    
                })
    @GetMapping("/{id}") 
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENTE') AND #id == authentication.principal.id)")
    public ResponseEntity<UsuarioResponseDto> getById(@PathVariable Long id){

        Usuario usuario = usuarioService.getById(id);
        return ResponseEntity.ok( UsuarioMapper.toUsuarioResponseDto(usuario));
    }

    @Operation(summary = "Altera senha do usuário", description = "Altera senha do usuário",
    responses = {
        @ApiResponse(responseCode = "204", description="Senha alterada com sucesso",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
        @ApiResponse(responseCode = "400", description="Senha não confere",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
        @ApiResponse(responseCode = "404", description="Usuário não encontrado",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
        @ApiResponse(responseCode = "422", description="Campos invalidos ou  mal formatados",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
        
    }) 
    @PatchMapping("/{id}") 
    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE') AND (#id == authentication.principal.id)")
    public ResponseEntity<Void> updatePassword( @PathVariable Long id, @Valid @RequestBody UsuarioSenhaDto dto){

        usuarioService.editarSenha(id, dto.getSenhaAtual(),dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Criar um novo usuário", description = "Recurso para criar um novo usuário",
                responses = {
                    @ApiResponse(responseCode = "201", description="Recurso criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDto.class))),
                    @ApiResponse(responseCode = "409", description="Usuário com e-mail já cadastrado no sistema",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description="Recurso não processado por dados de entrada invalidos",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
                })
    @PostMapping 
    public ResponseEntity<UsuarioResponseDto> create(@Valid @RequestBody UsuarioCreateDto usuarioCreateDto){

        Usuario _usuario = usuarioService.salvar( UsuarioMapper.toUsuario(usuarioCreateDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toUsuarioResponseDto(_usuario));
    }


   
}
