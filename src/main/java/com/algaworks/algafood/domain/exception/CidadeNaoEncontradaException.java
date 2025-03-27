package com.algaworks.algafood.domain.exception;

public class CidadeNaoEncontradaException extends EntidadeNaoEncontradaException {
    public CidadeNaoEncontradaException(String message) {
        super(message);
    }

    public CidadeNaoEncontradaException(Long estadoId) {
        this(String.format("Cidade de código %d não foi encontrada.", estadoId));
    }
}
