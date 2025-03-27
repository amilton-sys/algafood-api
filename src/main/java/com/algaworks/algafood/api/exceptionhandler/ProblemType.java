package com.algaworks.algafood.api.exceptionhandler;

import lombok.Getter;

@Getter
public enum ProblemType {
    MENSAGEM_INCOMPREENSIVEL("Mensagem incompreensivel", "/mensagem-incompreensivel"),
    ERRO_NEGOCIO("Violação de regra de negócio", "/erro-negocio"),
    ENTIDADE_EM_USO("Entidade em uso", "/entidade-em-uso"),
    PARAMETRO_INVALIDO("Parâmetro inválido", "/parametro-invalido"),
    RECURSO_NAO_ENCONTRADO("Recurso não encontrado", "/recurso-nao-encontrado"),
    ERRO_DE_SISTEMA("Erro no sistema", "/erro-de-sistema"),
    DADOS_INVALIDOS("Dados inválidos", "/dados-invalidos"), 
    ACESSO_NEGADO("Acesso negado", "/acesso-negado");
    private String title;
    private String uri;

    ProblemType(String title, String path) {
        this.title = title;
        this.uri = "https://algafood.com.br" + path;
    }
}
