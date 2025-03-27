package com.algaworks.algafood.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {
    public CozinhaNaoEncontradaException(String message) {
        super(message);
    }

    public CozinhaNaoEncontradaException(Long estadoId) {
        this(String.format("Cozinha de código %d não foi encontrada.", estadoId));
    }
}
