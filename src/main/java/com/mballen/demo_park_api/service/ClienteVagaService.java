package com.mballen.demo_park_api.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.mballen.demo_park_api.entity.ClienteVaga;
import com.mballen.demo_park_api.entity.projection.ClienteVagaProjecton;
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
         return clienteVagaRepository.findByReciboAndDataSaidaIsNull(recibo).orElseThrow(
            () -> new EntityNotFoundException(
                String.format("Recibo '%s' não encontrado no sistema ou check-out já realizado", recibo)
            )
         );
    }

    @Transactional(readOnly = true)
    public Long getTotalDeVezesEstacionamentoCompleto(String cpf) {
        
        return clienteVagaRepository.countByClienteCpfAndDataSaidaIsNotNull(cpf);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjecton> buscarTodosPorClienteCpf(String cpf, Pageable pageable) {
         
        return clienteVagaRepository.findAllByClienteCpf(cpf,pageable);
    }

    @Transactional(readOnly = true)
    public Page<ClienteVagaProjecton> buscarTodosPorUsuarioId(Long id, Pageable pageable) {
        return clienteVagaRepository.findAllByClienteUsuarioId(id,pageable);
    }
}
