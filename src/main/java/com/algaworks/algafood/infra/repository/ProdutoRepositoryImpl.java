package com.algaworks.algafood.infra.repository;

import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepositoryQueries;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public class ProdutoRepositoryImpl implements ProdutoRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Transactional
    @Override
    public FotoProduto save(FotoProduto fotoProduto) {
        return manager.merge(fotoProduto);
    }
    
    @Transactional
    @Override
    public void delete(FotoProduto fotoProduto) {
        manager.remove(fotoProduto);
    }
}
