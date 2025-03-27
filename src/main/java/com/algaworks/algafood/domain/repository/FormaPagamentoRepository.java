package com.algaworks.algafood.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.algaworks.algafood.domain.model.FormaPagamento;

import java.time.OffsetDateTime;

@Repository
public interface FormaPagamentoRepository extends JpaRepository<FormaPagamento, Long> {
    @Query("select max(f.dataAtualizacao) from FormaPagamento f")
    OffsetDateTime getDataUltimaAtualizacao();
    
    @Query("select f.dataAtualizacao from FormaPagamento f where f.id = :formaPagamentoId")
    OffsetDateTime getDataAtualizacaoById(Long formaPagamentoId);
}
