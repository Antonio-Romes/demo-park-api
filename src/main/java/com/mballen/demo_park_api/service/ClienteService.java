package com.mballen.demo_park_api.service;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballen.demo_park_api.entity.Cliente;
import com.mballen.demo_park_api.excption.CpfUniqueViolationException;
import com.mballen.demo_park_api.repository.ClienteRepository;

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
}
