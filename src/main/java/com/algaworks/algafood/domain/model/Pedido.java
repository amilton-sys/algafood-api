package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.event.PedidoCanceladoEvent;
import com.algaworks.algafood.domain.model.event.PedidoConfirmadoEvent;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.domain.AbstractAggregateRoot;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
public class Pedido extends AbstractAggregateRoot<Pedido> {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String codigo;
    private BigDecimal subtotal;
    private BigDecimal taxaFrete;
    private BigDecimal valorTotal;
    @CreationTimestamp
    private OffsetDateTime dataCriacao;
    private OffsetDateTime dataConfirmacao;
    private OffsetDateTime dataCancelamento;
    private OffsetDateTime dataEntrega;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedido> itens = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private FormaPagamento formaPagamento;

    @ManyToOne
    private Restaurante restaurante;

    @OneToOne
    @JoinColumn(name = "usuario_cliente_id")
    private Usuario cliente;

    @Embedded
    private Endereco enderecoEntrega;

    @Enumerated(EnumType.STRING)
    private StatusPedido status = StatusPedido.CRIADO;

    public void calcularValorTotal() {
        getItens().forEach(ItemPedido::calcularPrecoTotal);

        this.subtotal = getItens().stream()
                .map(ItemPedido::getPrecoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        this.valorTotal = this.subtotal.add(this.taxaFrete);
    }

    public void definirFrete() {
        setTaxaFrete(getRestaurante().getTaxaFrete());
    }

    public void atribuirPedidoAosItens() {
        getItens().forEach(item -> item.setPedido(this));
    }

    public void confirmar() {
        setStatus(StatusPedido.CONFIRMADO);
        setDataConfirmacao(OffsetDateTime.now());
        
        registerEvent(new PedidoConfirmadoEvent(this));
    }
    
    public boolean podeSerConfirmado(){
        return getStatus().podeAlterarPara(StatusPedido.CONFIRMADO);
    }

    public void entregar() {
        setStatus(StatusPedido.ENTREGUE);
        setDataEntrega(OffsetDateTime.now());
    }

    public boolean podeSerEntregue(){
        return getStatus().podeAlterarPara(StatusPedido.ENTREGUE);
    }

    public void cancelar() {
        setStatus(StatusPedido.CANCELADO);
        setDataCancelamento(OffsetDateTime.now());

        registerEvent(new PedidoCanceladoEvent(this));
    }

    public boolean podeSerCancelado(){
        return getStatus().podeAlterarPara(StatusPedido.CANCELADO);
    }

    private void setStatus(StatusPedido novoStatus) {
        if (getStatus().naoPodeAlterarPara(novoStatus)) {
            throw new NegocioException(
                    String.format("Status do pedido %s não pode ser alterado de %s para %s", getCodigo(), getStatus().getDescricao(), novoStatus.getDescricao())
            );
        }
        this.status = novoStatus;
    }
    
    @PrePersist
    public void gerarCodigo() {
        setCodigo(UUID.randomUUID().toString());
    }
}
