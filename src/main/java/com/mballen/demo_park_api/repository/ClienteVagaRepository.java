package com.mballen.demo_park_api.repository;

import java.util.Optional;
import org.springframework.data.domain.Pageable; 
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import com.mballen.demo_park_api.entity.ClienteVaga;
import com.mballen.demo_park_api.entity.projection.ClienteVagaProjecton;

public interface ClienteVagaRepository  extends JpaRepository<ClienteVaga, Long>{

    Optional<ClienteVaga> findByReciboAndDataSaidaIsNull(String recibo);

    Long countByClienteCpfAndDataSaidaIsNotNull(String cpf);

    Page<ClienteVagaProjecton> findAllByClienteCpf(String cpf, Pageable pageable);

    Page<ClienteVagaProjecton> findAllByClienteUsuarioId(Long id, Pageable pageable);
    
}
