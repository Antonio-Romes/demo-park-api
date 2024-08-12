package com.mballen.demo_park_api.service;

import java.util.List;

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

    @Transactional(readOnly = true)// definer que a transação é de somente leitura no banco
    public Usuario getById(Long id) {
         return usuarioRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Usuário não encontrado")
         );
    }

    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmarSenha) {
        if(!novaSenha.equals(confirmarSenha)){
            throw new RuntimeException("Nova senha não confere com confirmação de senha!");
        }
        Usuario usuario = getById(id);

        if(!usuario.getPassword().equals(senhaAtual)){
            throw new RuntimeException("Sua senha não confere!");
        }

        usuario.setPassword(novaSenha);
        return usuario;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
         return usuarioRepository.findAll();
    }
}
