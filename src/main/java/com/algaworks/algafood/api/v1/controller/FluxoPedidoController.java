package com.algaworks.algafood.api.v1.controller;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.service.FluxoPedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/v1/pedidos/{codigo}")
public class FluxoPedidoController {

    @Autowired
    private FluxoPedidoService fluxoPedidoService;
    
    @CheckSecurity.Pedidos.PodeGerenciarPedidos
    @PutMapping("/confirmacao")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> confirmar(@PathVariable String codigo) {
        fluxoPedidoService.confirmar(codigo);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Pedidos.PodeGerenciarPedidos
    @PutMapping("/entrega")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> entregar(@PathVariable String codigo) {
        fluxoPedidoService.entregar(codigo);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Pedidos.PodeGerenciarPedidos
    @PutMapping("/cancelamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> cancelar(@PathVariable String codigo) {
        fluxoPedidoService.cancelar(codigo);
        return ResponseEntity.noContent().build();
    }

}  