package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.FotoProdutoModelAssembler;
import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.dto.FotoProdutoModel;
import com.algaworks.algafood.api.v1.model.dto.input.FotoProdutoInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.FotoProdutoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FotoProduto;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.service.CadastroFotoProdutoService;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.FotoStorageService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/v1/restaurantes/{restauranteId}/produtos/{produtoId}/foto")
public class RestauranteProdutoFotoController {

    @Autowired
    private CadastroFotoProdutoService cadastroFotoProdutoService;
    @Autowired
    private CadastroProdutoService cadastroProdutoService;
    @Autowired
    private FotoProdutoModelAssembler fotoProdutoModelAssembler;
    @Autowired
    private FotoStorageService fotoStorageService;
    @Autowired
    private AlgaLinks algaLinks;

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public FotoProdutoModel buscar(@PathVariable Long restauranteId, @PathVariable Long produtoId) {

        return fotoProdutoModelAssembler.toModel(cadastroFotoProdutoService.buscar(restauranteId, produtoId));
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping
    public ResponseEntity<?> servir(@PathVariable Long restauranteId, @PathVariable Long produtoId,
                                    @RequestHeader(name = "accept") String acceptHeader) throws HttpMediaTypeNotAcceptableException {
        try {
            FotoProduto fotoProduto = cadastroFotoProdutoService.buscar(restauranteId, produtoId);
            MediaType mediaTypeFoto = MediaType.parseMediaType(fotoProduto.getContentType());
            List<MediaType> mediaTypesAceitas = MediaType.parseMediaTypes(acceptHeader);
            verificarCompatibilidadeMediaType(mediaTypeFoto, mediaTypesAceitas);
            FotoStorageService.FotoRecuperada fotoRecuperada = fotoStorageService.recuperar(fotoProduto.getNomeArquivo());

            if (fotoRecuperada.temInputStream()) {
                return ResponseEntity.ok()
                        .contentType(mediaTypeFoto)
                        .body(new InputStreamResource(fotoRecuperada.getInputStream()));
            } else {
                return ResponseEntity.status(HttpStatus.FOUND)
                        .header(HttpHeaders.LOCATION, fotoRecuperada.getUrl())
                        .build();
            }
        } catch (FotoProdutoNaoEncontradaException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public FotoProdutoModel atualizarFoto(@PathVariable Long restauranteId, @PathVariable Long produtoId, @Valid FotoProdutoInput fotoProdutoInput) throws IOException {
        Produto produto = cadastroProdutoService.buscar(restauranteId, produtoId);

        MultipartFile arquivo = fotoProdutoInput.getArquivo();

        FotoProduto fotoProduto = new FotoProduto();
        toModel(fotoProdutoInput, fotoProduto, produto, arquivo);

        return fotoProdutoModelAssembler.toModel(cadastroFotoProdutoService.salvar(fotoProduto, arquivo.getInputStream()));
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long restauranteId, @PathVariable Long produtoId) {
        cadastroFotoProdutoService.remover(restauranteId, produtoId);
    }

    private static void toModel(FotoProdutoInput fotoProdutoInput, FotoProduto fotoProduto, Produto produto, MultipartFile arquivo) {
        fotoProduto.setProduto(produto);
        fotoProduto.setDescricao(fotoProdutoInput.getDescricao());
        fotoProduto.setContentType(arquivo.getContentType());
        fotoProduto.setTamanho(arquivo.getSize());
        fotoProduto.setNomeArquivo(arquivo.getOriginalFilename());
    }

    private void verificarCompatibilidadeMediaType(MediaType mediaTypeFoto, List<MediaType> mediaTypesAceitas) throws HttpMediaTypeNotAcceptableException {
        boolean compativel = mediaTypesAceitas.stream()
                .anyMatch(mediaTypeAceita -> mediaTypeAceita.isCompatibleWith(mediaTypeFoto));
        if (!compativel) {
            throw new HttpMediaTypeNotAcceptableException(mediaTypesAceitas);
        }
    }
}
