package com.algaworks.algafood.domain.exception;

public class UsuarioNaoEncontradoException extends EntidadeNaoEncontradaException {
    public UsuarioNaoEncontradoException(String message) {
        super(message);
    }

    public UsuarioNaoEncontradoException(Long estadoId) {
        this(String.format("Usuario de código %d não foi encontrado.", estadoId));
    }
}
