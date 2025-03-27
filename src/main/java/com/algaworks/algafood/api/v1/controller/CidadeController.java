package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.helpers.ResourceUriHelper;
import com.algaworks.algafood.api.v1.assembler.CidadeInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CidadeModelAssembler;
import com.algaworks.algafood.api.v1.model.dto.CidadeModel;
import com.algaworks.algafood.api.v1.model.dto.input.CidadeInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import com.algaworks.algafood.domain.service.CadastroCidadeService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping(value = "/v1/cidades", produces = MediaType.APPLICATION_JSON_VALUE)
public class CidadeController {
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private CadastroCidadeService cadastroCidadeService;
    @Autowired
    private CidadeModelAssembler cidadeModelAssembler;
    @Autowired
    private CidadeInputDisassembler cidadeInputDisassembler;
    
    @CheckSecurity.Cidades.PodeConsultar
    @Deprecated
    @GetMapping
    public CollectionModel<CidadeModel> listar() {
        return cidadeModelAssembler.toCollectionModel(cidadeRepository.findAll());
    }

    @CheckSecurity.Cidades.PodeConsultar
    @GetMapping("/{id}")
    public CidadeModel buscar(@PathVariable Long id) {
        Cidade cidade = cadastroCidadeService.buscar(id);
        return cidadeModelAssembler.toModel(cidade);
    }

    @CheckSecurity.Cidades.PodeEditar
    @PostMapping
    public ResponseEntity<CidadeModel> salvar(@RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidade = cidadeInputDisassembler.toDomainObject(cidadeInput);

            Cidade savedCidade = cadastroCidadeService.salvar(cidade);

            URI uri = ResourceUriHelper.buildUri(savedCidade.getId());

            return ResponseEntity.created(uri).body(cidadeModelAssembler.toModel(savedCidade));

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Cidades.PodeEditar
    @PutMapping("/{id}")
    public CidadeModel atualizar(@PathVariable Long id, @RequestBody @Valid CidadeInput cidadeInput) {
        try {
            Cidade cidadeAtual = cadastroCidadeService.buscar(id);

            cidadeInputDisassembler.copyToDomainObject(cidadeInput, cidadeAtual);

            return cidadeModelAssembler.toModel(cadastroCidadeService.salvar(cidadeAtual));

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e.getCause());
        }
    }

    @CheckSecurity.Cidades.PodeEditar
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        cadastroCidadeService.excluir(id);
    }
}
