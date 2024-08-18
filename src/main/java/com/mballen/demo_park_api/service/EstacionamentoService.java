package com.mballen.demo_park_api.service;

import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import com.mballen.demo_park_api.entity.Cliente;
import com.mballen.demo_park_api.entity.ClienteVaga;
import com.mballen.demo_park_api.entity.Vaga;
import com.mballen.demo_park_api.util.EstacionamentoUtils;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class EstacionamentoService {
    
    private final ClienteVagaService clienteVagaService;
    private final ClienteService clienteService;
    private final VagaService vagaService;

    public ClienteVaga checkIn(ClienteVaga clienteVaga){
        Cliente cliente = clienteService.buscarPorCpf(clienteVaga.getCliente().getCpf());
        clienteVaga.setCliente(cliente);

        Vaga vaga = vagaService.buscarPorValaLivre();
        vaga.setStatus(Vaga.StatusVaga.OCUPADA);
        clienteVaga.setVaga(vaga);

        clienteVaga.setDataEntrada(LocalDateTime.now());
        clienteVaga.setRecibo(EstacionamentoUtils.gerarRecibor());

        return clienteVagaService.salvar(clienteVaga);

    }

}
