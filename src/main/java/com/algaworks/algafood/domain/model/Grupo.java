package com.algaworks.algafood.domain.model;

import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Grupo {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    @ManyToMany
    @JoinTable(
            name = "grupo_permissao",
            joinColumns = @JoinColumn(name = "grupo_id"),
            inverseJoinColumns = @JoinColumn(name = "permissao_id")
    )
    private Set<Permissao> permissoes = new HashSet<>();

    public void removerPermissao(Permissao permissao) {
        getPermissoes().remove(permissao);
    }

    public void adicionarPermissao(Permissao permissao) {
        getPermissoes().add(permissao);
    }
}
