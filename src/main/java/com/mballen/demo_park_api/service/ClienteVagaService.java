package com.mballen.demo_park_api.service;

import org.springframework.stereotype.Service;

import com.mballen.demo_park_api.entity.ClienteVaga;
import com.mballen.demo_park_api.repository.ClienteVagaRepository;

import jakarta.persistence.EntityNotFoundException;

import org.springframework.transaction.annotation.Transactional;
 
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class ClienteVagaService {
    
    private final ClienteVagaRepository clienteVagaRepository;

    @Transactional
    public ClienteVaga salvar(ClienteVaga clienteVaga){
        return clienteVagaRepository.save(clienteVaga);
    }

    @Transactional(readOnly = true)
    public ClienteVaga buscarPorRecibo(String recibo) {
         return clienteVagaRepository.findByReciboAnSaidaIsNull(recibo).orElseThrow(
            () -> new EntityNotFoundException(
                String.format("Recibo '%s' não encontrado no sistema ou check-out já realizado", recibo)
            )
         );
    }
}
