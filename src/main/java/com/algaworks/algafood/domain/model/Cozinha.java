package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.Groups;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Cozinha {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Groups.CozinhaId.class)
    private Long id;
    @NotBlank
    private String nome;
    @OneToMany(mappedBy = "cozinha")
    private List<Restaurante> restaurantes = new ArrayList<>();
}
