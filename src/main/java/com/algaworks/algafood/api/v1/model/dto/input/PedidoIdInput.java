package com.algaworks.algafood.api.v1.model.dto.input;

import javax.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoIdInput {
    @NotNull
    private Long id;
}
