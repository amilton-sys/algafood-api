package com.algaworks.algafood.core.email;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

@Validated
@Component
@Getter
@Setter
@ConfigurationProperties("algafood.email")
public class EmailProperties {
    @NotBlank
    private String remetente;
    private Implementacao impl = Implementacao.FAKE;
    private Sandbox sandbox = new Sandbox();

    public enum Implementacao {
        SMTP, FAKE, SANDBOX;
    }

    @Getter
    @Setter
    public class Sandbox {
        private String destinatario;
    }
}
