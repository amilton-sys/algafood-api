package com.algaworks.algafood.domain.listener;

import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.service.EnvioEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.event.TransactionalEventListener;

public class NotificacaoClientePedidoCanceladoListener {

    @Autowired
    private EnvioEmailService envioEmailService;

    @TransactionalEventListener
    public void aoCancelarPedido(PedidoCanceladoEvent event) {
        Pedido pedido = event.getPedido();
        var mensagem = EnvioEmailService.Mensagem.builder()
                .assunto(pedido.getRestaurante().getNome() + " - Pedido cancelado")
                .corpo("emails/pedido-cancelado.html")
                .variavel("pedido", pedido)
                .destinatario(pedido.getCliente().getEmail())
                .build();
        envioEmailService.enviar(mensagem);
    }
}
