package com.algaworks.algafood.domain.exception;

public class FormaPagamentoNaoEncontradaException extends EntidadeNaoEncontradaException {
    public FormaPagamentoNaoEncontradaException(String message) {
        super(message);
    }

    public FormaPagamentoNaoEncontradaException(Long estadoId) {
        this(String.format("FormaPagamento de código %d não foi encontrada.", estadoId));
    }
}
