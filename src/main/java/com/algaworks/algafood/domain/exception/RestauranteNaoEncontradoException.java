package com.algaworks.algafood.domain.exception;

public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {
    public RestauranteNaoEncontradoException(String message) {
        super(message);
    }

    public RestauranteNaoEncontradoException(Long estadoId) {
        this(String.format("Restaurante de código %d não foi encontrado.", estadoId));
    }
}
