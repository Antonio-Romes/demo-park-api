package com.mballen.demo_park_api.service;
 
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mballen.demo_park_api.entity.Vaga;
import com.mballen.demo_park_api.excption.CodigoUniqueViolationException;
import com.mballen.demo_park_api.excption.EntityNotFoundException;
import com.mballen.demo_park_api.repository.VagaRepository; 
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class VagaService {
    
    private final VagaRepository vagaRepository;

    @Transactional
    public Vaga salvar(Vaga vaga){
        try {
            return  vagaRepository.save(vaga);
        } catch (DataIntegrityViolationException ex) {
           throw new CodigoUniqueViolationException(String.format("Vaga com código '%s' já cadastrada", vaga.getCodigo()));
        } 
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorCodigo(String codigo){
        return vagaRepository.findByCodigo(codigo).orElseThrow(
            () -> new EntityNotFoundException(String.format("Vaga com código '%s' no foi encontrada", codigo))
        );
        
    }

    @Transactional(readOnly = true)
    public Vaga buscarPorValaLivre() { 
        return vagaRepository.findFirstByStatus(Vaga.StatusVaga.LIVRE).orElseThrow(
            () -> new EntityNotFoundException(String.format("Nenhuma vaga livre foi encontrada"))
        );
    }
}
