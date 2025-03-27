package com.algaworks.algafood.domain.exception;

public class EstadoNaoEncontradoException extends EntidadeNaoEncontradaException {
    public EstadoNaoEncontradoException(String message) {
        super(message);
    }

    public EstadoNaoEncontradoException(Long estadoId) {
        this(String.format("Estado de código %d não foi encontrado.", estadoId));
    }
}
