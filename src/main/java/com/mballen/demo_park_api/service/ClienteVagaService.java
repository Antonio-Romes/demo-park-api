package com.mballen.demo_park_api.service;

import org.springframework.stereotype.Service;

import com.mballen.demo_park_api.entity.ClienteVaga;
import com.mballen.demo_park_api.repository.ClienteVagaRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {
    
    private final ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga){
        return clienteVagaRepository.save(clienteVaga);
    }
}
