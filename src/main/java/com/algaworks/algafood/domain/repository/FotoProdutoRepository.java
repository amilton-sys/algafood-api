package com.algaworks.algafood.domain.repository;

import com.algaworks.algafood.domain.model.FotoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoProdutoRepository extends JpaRepository<FotoProduto, Long> {
}
