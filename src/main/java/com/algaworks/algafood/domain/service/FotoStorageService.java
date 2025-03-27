package com.algaworks.algafood.domain.service;

import lombok.Builder;
import lombok.Getter;

import java.io.InputStream;
import java.util.UUID;

public interface FotoStorageService {
    void armazenar(NovaFoto novaFoto);
    void remover(String nomeArquivo);
    FotoRecuperada recuperar(String nomeArquivo);

    default String gerarNomeArquivo(String nomeOriginal) {
        return UUID.randomUUID().toString().concat("_").concat(nomeOriginal);
    }

    default void substituir(String nomeArquivoExistente, NovaFoto novaFoto){
        this.armazenar(novaFoto);
        if(nomeArquivoExistente != null){
            this.remover(nomeArquivoExistente);
        }
    };

    @Getter
    @Builder
    class NovaFoto {
        private String nomeArquivo;
        private String contentType;
        private InputStream inputStream;
    }
    @Getter
    @Builder
    class FotoRecuperada{
        private InputStream inputStream;
        private String url;
        
        public boolean temUrl(){
            return url != null;
        }
        
        public boolean temInputStream(){
            return inputStream != null;
        }
    }
}
