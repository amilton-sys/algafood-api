package com.algaworks.algafood.domain.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Usuario {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;
    private String email;
    private String senha;
    @CreationTimestamp
    @Column(nullable = false, columnDefinition = "datetime")
    private OffsetDateTime dataCadastro;
    @ManyToMany
    @JoinTable(name = "usuario_grupo", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "grupo_id"))
    private Set<Grupo> grupos = new HashSet<>();

    public boolean senhaCoincideCom(String senha) {
        return getSenha().equals(senha);
    }

    public boolean senhaNaoCoincideCom(String senha) {
        return !senhaCoincideCom(senha);
    }

    public void associarGrupo(Grupo grupo) {
        getGrupos().add(grupo);
    }

    public void desassociarGrupo(Grupo grupo) {
        getGrupos().remove(grupo);
    }

    public boolean isNovo() {
        return getId() == null;
    }
}
