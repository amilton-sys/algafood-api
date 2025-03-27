package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FluxoPedidoService {

    @Autowired
    private EmissaoPedidoService emissaoPedidoService;
    @Autowired
    private PedidoRepository pedidoRepository;

    @Transactional
    public void confirmar(String codigo) {
        Pedido pedido = emissaoPedidoService.buscar(codigo);
        pedido.confirmar();
        
        pedidoRepository.save(pedido);
    }

    @Transactional
    public void entregar(String codigo) {
        Pedido pedido = emissaoPedidoService.buscar(codigo);
        pedido.entregar();
    }

    @Transactional
    public void cancelar(String codigo) {
        Pedido pedido = emissaoPedidoService.buscar(codigo);
        pedido.cancelar();

        pedidoRepository.save(pedido);
    }

}   