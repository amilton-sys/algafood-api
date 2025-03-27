package com.algaworks.algafood.api.v1.model.dto.input;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class RestauranteInput {
    @NotBlank
    private String nome;
    @NotNull
    @PositiveOrZero
    private BigDecimal taxaFrete;
    @Valid
    @NotNull
    private CozinhaIdInput cozinha;
    @Valid
    @NotNull
    private EnderecoInput endereco;
}
