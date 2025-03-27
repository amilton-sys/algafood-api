package com.algaworks.algafood.domain.exception;

public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaException {
    public GrupoNaoEncontradoException(String message) {
        super(message);
    }

    public GrupoNaoEncontradoException(Long estadoId) {
        this(String.format("Grupo de código %d não foi encontrada.", estadoId));
    }
}
