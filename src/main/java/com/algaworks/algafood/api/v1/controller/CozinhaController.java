package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.helpers.ResourceUriHelper;
import com.algaworks.algafood.api.v1.assembler.CozinhaInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.CozinhaModelAssembler;
import com.algaworks.algafood.api.v1.model.dto.CozinhaModel;
import com.algaworks.algafood.api.v1.model.dto.input.CozinhaInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Slf4j
@RestController
@RequestMapping("/v1/cozinhas")
public class CozinhaController {
    @Autowired
    private CozinhaRepository cozinhaRepository;
    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;
    @Autowired
    private CozinhaModelAssembler cozinhaModelAssembler;
    @Autowired
    private CozinhaInputDisassembler cozinhaInputDisassembler;
    @Autowired
    private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;
    
    @CheckSecurity.Cozinhas.PodeConsultar
    @GetMapping
    public PagedModel<CozinhaModel> listar(Pageable pageable) {
        log.info("Consultando cozinhas...");
        Page<Cozinha> cozinhaPage = cozinhaRepository.findAll(pageable);
        return pagedResourcesAssembler
                .toModel(cozinhaPage, cozinhaModelAssembler);
    }

    @CheckSecurity.Cozinhas.PodeConsultar
    @GetMapping("/{id}")
    public CozinhaModel buscar(@PathVariable Long id) {
        return cozinhaModelAssembler.toModel(cadastroCozinhaService.buscar(id));
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @PostMapping
    public ResponseEntity<CozinhaModel> adicionar(@RequestBody @Valid CozinhaInput cozinhaInput) {
        Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
        Cozinha savedCozinha = cadastroCozinhaService.salvar(cozinha);
        URI uri = ResourceUriHelper.buildUri(savedCozinha.getId());
        return ResponseEntity.created(uri).body(cozinhaModelAssembler.toModel(savedCozinha));
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @PutMapping("/{id}")
    public CozinhaModel atualizar(@PathVariable Long id, @RequestBody @Valid CozinhaInput cozinhaInput) {
        Cozinha cozinhaAtual = cadastroCozinhaService.buscar(id);
        cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
        return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinhaAtual));
    }

    @CheckSecurity.Cozinhas.PodeEditar
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        cadastroCozinhaService.excluir(id);
    }

}
