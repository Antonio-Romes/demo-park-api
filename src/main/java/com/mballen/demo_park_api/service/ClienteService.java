package com.mballen.demo_park_api.service;
 
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballen.demo_park_api.entity.Cliente;
import com.mballen.demo_park_api.entity.projection.ClienteProjection;
import com.mballen.demo_park_api.excption.CpfUniqueViolationException;
import com.mballen.demo_park_api.repository.ClienteRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor; 

@RequiredArgsConstructor
@Service
public class ClienteService {   

    
    private final ClienteRepository clienteRepository;
    

    @Transactional
    public Cliente salvar(Cliente cliente){

        try {
            return clienteRepository.save(cliente);
        } catch (DataIntegrityViolationException ex) { 
            throw new CpfUniqueViolationException(String.format("CPF '%s' não pode ser cadastrado, já existe no sistema", cliente.getCpf()));
        }
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorId(Long id) {
         
        return clienteRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format("Cliente id = %s não encontrado no sistema", id))
        );
    }

    @Transactional(readOnly = true)
    public Page<ClienteProjection> buscarTodos(Pageable pageable) {
       return clienteRepository.findAllPageable(pageable);
    }

    @Transactional(readOnly = true)
    public Cliente buscarPorUsuarioId(Long id) {
        return clienteRepository.findByUsuarioId(id);
    }
}
