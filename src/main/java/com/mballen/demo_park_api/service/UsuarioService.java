package com.mballen.demo_park_api.service;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballen.demo_park_api.entity.Usuario; 
import com.mballen.demo_park_api.excption.EntityNotFoundException;
import com.mballen.demo_park_api.excption.PasswordInvalidException;
import com.mballen.demo_park_api.excption.UsernameUniqueViolationExcpion;
import com.mballen.demo_park_api.repository.UsuarioRepository;

 
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor // cria um metodo construdor e faze injeção de dependência do repositorio 
@Service
public class UsuarioService {
    
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Usuario salvar(Usuario usuario) {
       try {
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        return usuarioRepository.save(usuario);
       } catch (DataIntegrityViolationException e) {  
         throw new UsernameUniqueViolationExcpion(String.format("Username {%s} já cadastrado", usuario.getUsername()));
       }
        
    }

    @Transactional(readOnly = true)// definer que a transação é de somente leitura no banco
    public Usuario getById(Long id) {
         return usuarioRepository.findById(id).orElseThrow(

            // tomar cuidado, no jakarte tem a classe EntityNotFoundException, precisa importa a 
            //classe que foi criada na pasta excption
            () -> new EntityNotFoundException( String.format("Usuário id=%s não encontrado", id ))
         );
    }

    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmarSenha) {
        if(!novaSenha.equals(confirmarSenha)){
            throw new PasswordInvalidException("Nova senha não confere com confirmação de senha!");
        }
        Usuario usuario = getById(id);

        if(!passwordEncoder.matches(senhaAtual, usuario.getPassword())){
            throw new PasswordInvalidException("Sua senha não confere!");
        }

        usuario.setPassword(passwordEncoder.encode(novaSenha));
        return usuario;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
         return usuarioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Usuario getByUsername(String username) {
         return usuarioRepository.findByUsername(username).orElseThrow( 
         () -> new EntityNotFoundException( String.format("Usuário com 'username' não encontrado", username ))
      );
    }

    @Transactional(readOnly = true)
    public Usuario.Role getRoleByUsername(String username) {
        return usuarioRepository.findRoleByUsername(username);
    }
}
