package com.algaworks.algafood.api.v2.controller;

import com.algaworks.algafood.api.helpers.ResourceUriHelper;
import com.algaworks.algafood.api.v2.assembler.CidadeInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CidadeModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.CidadeModelV2;
import com.algaworks.algafood.api.v2.model.input.CidadeInputV2;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/v2/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeControllerV2 {
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private CadastroCidadeService cadastroCidadeService;
    @Autowired
    private CidadeModelAssemblerV2 cidadeModelAssembler;
    @Autowired
    private CidadeInputDisassemblerV2 cidadeInputDisassembler;

    @GetMapping
    public CollectionModel<CidadeModelV2> listar() {
        return cidadeModelAssembler.toCollectionModel(cidadeRepository.findAll());
    }

    @GetMapping("/{id}")
    public CidadeModelV2 buscar(@PathVariable Long id) {
        Cidade cidade = cadastroCidadeService.buscar(id);
        return cidadeModelAssembler.toModel(cidade);
    }

    @PostMapping
    public ResponseEntity<CidadeModelV2> salvar(@RequestBody @Valid CidadeInputV2 cidadeInput) {
        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

            Cidade savedCidade = cadastroCidadeService.salvar(cidade);

            URI uri = ResourceUriHelper.buildUri(savedCidade.getId());

            return ResponseEntity.created(uri).body(cidadeModelAssembler.toModel(savedCidade));

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @PutMapping("/{id}")
    public CidadeModelV2 atualizar(@PathVariable Long id, @RequestBody @Valid CidadeInputV2 cidadeInput) {
        try {
            Cidade cidadeAtual = cadastroCidadeService.buscar(id);

            cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

            return cidadeModelAssembler.toModel(cadastroCidadeService.salvar(cidadeAtual));

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e.getCause());
        }
    }

    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        cadastroCidadeService.excluir(id);
    }
}
