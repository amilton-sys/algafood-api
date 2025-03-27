package com.algaworks.algafood.domain.exception;

public class ProdutoNaoEncontradoException extends EntidadeNaoEncontradaException {
    public ProdutoNaoEncontradoException(String message) {
        super(message);
    }

    public ProdutoNaoEncontradoException(Long estadoId) {
        this(String.format("Produto de código %d não foi encontrado.", estadoId));
    }
}
