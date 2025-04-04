package com.algaworks.algafood.domain.exception;

public class PedidoNaoEncontradoException extends EntidadeNaoEncontradaException {
    public PedidoNaoEncontradoException(String codigo) {
        super(String.format("Não existe um pedido com código %s", codigo));
    }
}  