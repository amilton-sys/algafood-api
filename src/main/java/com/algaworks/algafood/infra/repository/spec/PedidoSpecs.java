package com.algaworks.algafood.infra.repository.spec;

import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import javax.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;

public class PedidoSpecs {

    public static Specification<Pedido> usandoFiltro(PedidoFilter filtro) {
        return (root, query, builder) -> {
            if (Pedido.class.equals(query.getResultType())) {
                root.fetch("restaurante").fetch("cozinha");
                root.fetch("cliente");
            }

            var predicates = new ArrayList<Predicate>();

            if (filtro.getClienteId() != null)
                predicates.add(builder.equal(builder.toLong(root.get("cliente")), filtro.getClienteId()));
            if (filtro.getRestauranteId() != null)
                predicates.add(builder.equal(builder.toLong(root.get("restaurante")), filtro.getRestauranteId()));
            if (filtro.getDataCriacaoInicio() != null)
                predicates.add(builder.greaterThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoInicio()));
            if (filtro.getDataCriacaoFim() != null)
                predicates.add(builder.lessThanOrEqualTo(root.get("dataCriacao"), filtro.getDataCriacaoFim()));

            return builder.and(predicates.toArray(Predicate[]::new));
        };

    }
}
