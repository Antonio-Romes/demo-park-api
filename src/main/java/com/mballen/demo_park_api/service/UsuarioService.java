package com.mballen.demo_park_api.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballen.demo_park_api.entity.Usuario;
import com.mballen.demo_park_api.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // cria um metodo construdor e faze injeção de dependência do repositorio 
@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public Usuario salvar(Usuario usuario) {
       
        return usuarioRepository.save(usuario);
    }
}
