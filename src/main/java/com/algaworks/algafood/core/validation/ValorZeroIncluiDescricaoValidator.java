package com.algaworks.algafood.core.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.ValidationException;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.Objects;

public class ValorZeroIncluiDescricaoValidator implements ConstraintValidator<ValorZeroIncluiDescricao, Object> {
    private String valorField;

    private String descricaoField;

    private String descricaoObrigatoriaField;

    @Override
    public void initialize(ValorZeroIncluiDescricao constraintAnnotation) {
        this.valorField = constraintAnnotation.valorField();
        this.descricaoField = constraintAnnotation.descricaoField();
        this.descricaoObrigatoriaField = constraintAnnotation.descricaoObrigatoriaField();
    }

    @Override
    public boolean isValid(Object objectoValidacao, ConstraintValidatorContext context) {
        boolean valido = true;
        try {
            BigDecimal valor = (BigDecimal) Objects.requireNonNull(BeanUtils.getPropertyDescriptor(objectoValidacao.getClass(), valorField))
                    .getReadMethod().invoke(objectoValidacao);
            String descricao = (String) Objects.requireNonNull(BeanUtils.getPropertyDescriptor(objectoValidacao.getClass(), descricaoField))
                    .getReadMethod().invoke(objectoValidacao);
            if (valor != null && BigDecimal.ZERO.compareTo(valor) == 0 && descricao != null) {
                valido = descricao.toLowerCase().contains(this.descricaoObrigatoriaField.toLowerCase());
            }

            return valido;
        } catch (Exception e) {
            throw new ValidationException(e);
        }
    }
}
