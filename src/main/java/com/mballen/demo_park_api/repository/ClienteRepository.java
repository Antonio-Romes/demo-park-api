package com.mballen.demo_park_api.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mballen.demo_park_api.entity.Cliente;
import com.mballen.demo_park_api.entity.projection.ClienteProjection;

public interface ClienteRepository  extends JpaRepository<Cliente, Long> {
    
     @Query("Select c from Cliente c")
    Page<ClienteProjection> findAllPageable(Pageable pageable);
}
