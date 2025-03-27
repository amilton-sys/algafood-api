package com.algaworks.algafood.core.validation;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.validation.beanvalidation.OptionalValidatorFactoryBean;

@Configuration
public class ValidationConfig {

    @Bean
    public LocalValidatorFactoryBean validator(MessageSource messageSource) {
        LocalValidatorFactoryBean bean = new OptionalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);
        return bean;
    }
}
