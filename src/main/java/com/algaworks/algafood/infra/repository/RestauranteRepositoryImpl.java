package com.algaworks.algafood.infra.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.repository.RestauranteRepositoryQueries;
import com.algaworks.algafood.infra.repository.spec.RestauranteSpecs;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class RestauranteRepositoryImpl implements RestauranteRepositoryQueries {

    @PersistenceContext
    private EntityManager manager;

    @Autowired
    @Lazy
    private RestauranteRepository restauranteRepository;

    @Override
    public List<Restaurante> find(String nome, BigDecimal taxaFreteInicial, BigDecimal taxaFreteFinal) {

        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();

        CriteriaQuery<Restaurante> criteriaQuery = criteriaBuilder.createQuery(Restaurante.class);

        Root<Restaurante> root = criteriaQuery.from(Restaurante.class);

        var predicates = new ArrayList<Predicate>();

        if (StringUtils.hasText(nome)) {
            predicates.add(criteriaBuilder.like(root.get("nome"), "%" + nome + "%"));
        }

        if (taxaFreteInicial != null) {
            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("taxaFreteInicial"), taxaFreteInicial));
        }

        if (taxaFreteFinal != null) {
            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("taxaFreteFinal"), taxaFreteFinal));
        }

        criteriaQuery.where(predicates.stream().toArray(Predicate[]::new));

        TypedQuery<Restaurante> query = manager.createQuery(criteriaQuery);

        return query.getResultList();
    }

    @Override
    public List<Restaurante> findComFreteGratis(String nome) {
        return restauranteRepository.findAll(RestauranteSpecs.comFreteGratis().and(RestauranteSpecs.comNomeSemelhante(
                nome)));
    }
}
