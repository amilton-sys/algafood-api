package com.algaworks.algafood;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("application-test.properties")
public class CadastroCozinhaIT {
    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;

    @Test
    public void deveAtribuirIdQuandoCadastrarCozinhaComDadosCorretos() {
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome("Chinesa");

        novaCozinha = cadastroCozinhaService.salvar(novaCozinha);

        assertThat(novaCozinha).isNotNull();
        assertThat(novaCozinha.getId()).isNotNull();
    }

    @Test
    public void deveFalhar_QuandoCadastrarCozinhaSemNome() {
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setNome(null);

        var exception = assertThrows(ConstraintViolationException.class, () -> {
            cadastroCozinhaService.salvar(novaCozinha);
        });

        String expectedMessage = "não deve estar em branco";
        String actualMessage = exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .findFirst().orElse("");

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

    @Test
    public void deveFalhar_QuandoExcluirCozinhaEmUso() {
        Cozinha novaCozinha = new Cozinha();
        novaCozinha.setId(1L);

        var exception = assertThrows(EntidadeEmUsoException.class, () -> {
            cadastroCozinhaService.excluir(novaCozinha.getId());
        });

        String expectedMessage = "Cozinha de código 1 não pode ser removida, pois está em uso.";
        String actualMessage = exception.getMessage();

        assertThat(actualMessage).isEqualTo(expectedMessage);
    }

}
