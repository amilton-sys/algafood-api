package com.algaworks.algafood.domain.exception;

public class FotoProdutoNaoEncontradaException extends EntidadeNaoEncontradaException {
    public FotoProdutoNaoEncontradaException(String message) {
        super(message);
    }

    public FotoProdutoNaoEncontradaException(Long produtoId, Long restauranteId) {
        this(String.format("Foto do produto de código %d para o restaurante %d não foi encontrada.", produtoId, restauranteId));
    }
}
