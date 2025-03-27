package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.Optional;

@Service
public class CadastroFotoProdutoService {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private FotoStorageService fotoStorageService;

    public FotoProduto buscar(Long restauranteId, Long produtoId) {
        return produtoRepository.findFotoById(restauranteId, produtoId).orElseThrow(() -> new FotoProdutoNaoEncontradaException(produtoId, restauranteId));
    }

    @Transactional
    public FotoProduto salvar(FotoProduto fotoProduto, InputStream dadosArquivo) {
        Long restauranteId = fotoProduto.getRestauranteId();
        Long produtoId = fotoProduto.getProduto().getId();
        String nomeNovoArquivo = fotoStorageService.gerarNomeArquivo(fotoProduto.getNomeArquivo());
        String nomeArquivoExistente = null;

        Optional<FotoProduto> fotoExistente = produtoRepository.findFotoById(restauranteId, produtoId);
        if (fotoExistente.isPresent()) {
            nomeArquivoExistente = fotoExistente.get().getNomeArquivo();
            fotoStorageService.remover(nomeArquivoExistente);
            produtoRepository.delete(fotoExistente.get());
        }

        fotoProduto.setNomeArquivo(nomeNovoArquivo);
        fotoProduto = produtoRepository.save(fotoProduto);
        produtoRepository.flush();

        FotoStorageService.NovaFoto novaFoto = FotoStorageService.NovaFoto.builder()
                .nomeArquivo(fotoProduto.getNomeArquivo())
                .contentType(fotoProduto.getContentType())
                .inputStream(dadosArquivo)
                .build();

        fotoStorageService.substituir(nomeArquivoExistente, novaFoto);
        return fotoProduto;
    }
    
    @Transactional
    public void remover(Long restauranteId, Long produtoId) {
        FotoProduto fotoProduto = buscar(restauranteId, produtoId);
        produtoRepository.delete(fotoProduto);
        produtoRepository.flush();
        fotoStorageService.remover(fotoProduto.getNomeArquivo());
    }
}
