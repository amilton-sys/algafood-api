package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.ValorZeroIncluiDescricao;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ValorZeroIncluiDescricao(valorField = "taxaFrete", descricaoField = "nome", descricaoObrigatoriaField = "Frete Gratís")
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Restaurante {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private BigDecimal taxaFrete;

    @ManyToOne
    private Cozinha cozinha;

    @Embedded
    private Endereco endereco;

    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataCadastro;

    @UpdateTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataAtualizacao;

    private Boolean ativo = Boolean.TRUE;

    private Boolean aberto = Boolean.TRUE;

    @ManyToMany
    @JoinTable(name = "restaurante_forma_pagamento", joinColumns = @JoinColumn(name = "restaurante_id"), inverseJoinColumns = @JoinColumn(name = "forma_pagamento_id"))
    private Set<FormaPagamento> formasPagamento = new HashSet<>();

    @ManyToMany
    @JoinTable(name = "restaurante_usuario_responsavel", joinColumns = @JoinColumn(name = "restaurante_id"), inverseJoinColumns = @JoinColumn(name = "usuario_id"))
    private Set<Usuario> responsaveis = new HashSet<>();

    @OneToMany(mappedBy = "restaurante")
    private List<Produto> produtos = new ArrayList<>();

    public void ativar() {
        setAtivo(Boolean.TRUE);
    }

    public boolean isAtivo() {
        return this.ativo;
    }

    public void inativar() {
        setAtivo(Boolean.FALSE);
    }

    public void abrir() {
        setAberto(Boolean.TRUE);
    }

    public boolean isAberto() {
        return aberto;
    }

    public void fechar() {
        setAberto(Boolean.FALSE);
    }

    public void associarResponsavel(Usuario usuario) {
        getResponsaveis().add(usuario);
    }

    public void desassociarResponsavel(Usuario usuario) {
        getResponsaveis().remove(usuario);
    }

    public void desassociarFormaPagamento(FormaPagamento formaPagamento) {
        getFormasPagamento().remove(formaPagamento);
    }

    public void associarFormaPagamento(FormaPagamento formaPagamento) {
        getFormasPagamento().add(formaPagamento);
    }

    public boolean aceitaFormaPagamento(FormaPagamento formaPagamento) {
        return getFormasPagamento().contains(formaPagamento);
    }

    public boolean naoAceitaFormaPagamento(FormaPagamento formaPagamento) {
        return !aceitaFormaPagamento(formaPagamento);
    }

    public boolean ativacaoPermitida() {
        return !isAtivo();
    }

    public boolean inativacaoPermitida() {
        return isAtivo();
    }

    public boolean aberturaPermitida() {
        return isAtivo() && !isAberto();
    }

    public boolean fechamentoPermitido() {
        return isAberto();
    }
}
