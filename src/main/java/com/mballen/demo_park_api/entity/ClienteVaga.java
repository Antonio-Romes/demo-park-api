package com.mballen.demo_park_api.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
 
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Entity
@Table(name="clientes_tem_vagas")
@EntityListeners(AuditingEntityListener.class)
public class ClienteVaga {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    

    @Column(name="numero_recibo", nullable = false, unique = true, length = 15)
    private String recibo;

    @Column(name="placa", nullable = false, length = 8)
    private String placa;

    @Column(name="marca", nullable = false, length = 45)
    private String marca;

    @Column(name="modelo", nullable = false, length = 45)
    private String modelo;

    @Column(name="cor", nullable = false, length = 45)
    private String cor; 

    @Column(name="data_entrada", nullable = false)
    private LocalDateTime dataEntrada; 

    @Column(name="data_saida")
    private LocalDateTime dataSaida; 

    @Column(name="valor", columnDefinition = "decimal(7,2)")
    private BigDecimal valor; 

    @Column(name="desconto", columnDefinition = "decimal(7,2)")
    private BigDecimal desconto; 


    @ManyToOne
    @JoinColumn(name = "id_cliente", nullable = false)
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_vaga", nullable = false)
    private Vaga vaga;


    @CreatedDate
    @Column(name = "data_criacao")
    private LocalDateTime dataCriacao;

    @LastModifiedDate
    @Column(name = "data_modificacao")
    private LocalDateTime dataModificacao;

    @CreatedBy
    @Column(name = "criado_por")
    private String criadoPor;

    @LastModifiedBy
    @Column(name = "modificado_por")
    private String modificadoPor;


    @Override
    public boolean equals(Object o){
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;

        ClienteVaga clienteVaga = (ClienteVaga) o;
        return Objects.equals(id, clienteVaga.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }
    
}
