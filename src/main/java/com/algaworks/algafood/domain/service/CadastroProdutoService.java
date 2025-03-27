package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.ProdutoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CadastroProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;

    @Transactional
    public Produto salvar(Produto produto) {
        return produtoRepository.save(produto);
    }

    public Produto buscar(Long id, Long produtoId) {
        return produtoRepository.findById(id, produtoId).orElseThrow(() -> new ProdutoNaoEncontradoException(id));
    }
}
