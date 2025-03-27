package com.algaworks.algafood.domain.model;

import com.algaworks.algafood.core.validation.Groups;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import javax.validation.constraints.*;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Estado {
    @EqualsAndHashCode.Include
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotNull(groups = Groups.EstadoId.class)
    private Long id;
    @NotBlank
    private String nome;
}
