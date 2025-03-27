package com.algaworks.algafood.api.v2.controller;

import com.algaworks.algafood.api.helpers.ResourceUriHelper;
import com.algaworks.algafood.api.v2.assembler.CozinhaInputDisassemblerV2;
import com.algaworks.algafood.api.v2.assembler.CozinhaModelAssemblerV2;
import com.algaworks.algafood.api.v2.model.CozinhaModelV2;
import com.algaworks.algafood.api.v2.model.input.CozinhaInputV2;
import com.algaworks.algafood.domain.model.Cozinha;
import com.algaworks.algafood.domain.repository.CozinhaRepository;
import com.algaworks.algafood.domain.service.CadastroCozinhaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping("/v2/cozinhas")
public class CozinhaControllerV2 {
    @Autowired
    private CozinhaRepository cozinhaRepository;
    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;
    @Autowired
    private CozinhaModelAssemblerV2 cozinhaModelAssembler;
    @Autowired
    private CozinhaInputDisassemblerV2 cozinhaInputDisassembler;
    @Autowired
    private PagedResourcesAssembler<Cozinha> pagedResourcesAssembler;

    @GetMapping
    public PagedModel<CozinhaModelV2> listar(Pageable pageable) {
        Page<Cozinha> cozinhaPage = cozinhaRepository.findAll(pageable);
        return pagedResourcesAssembler
                .toModel(cozinhaPage, cozinhaModelAssembler);
    }

    @GetMapping("/{id}")
    public CozinhaModelV2 buscar(@PathVariable Long id) {
        return cozinhaModelAssembler.toModel(cadastroCozinhaService.buscar(id));
    }

    @PostMapping
    public ResponseEntity<CozinhaModelV2> adicionar(@RequestBody @Valid CozinhaInputV2 cozinhaInput) {
        Cozinha cozinha = cozinhaInputDisassembler.toDomainObject(cozinhaInput);
        Cozinha savedCozinha = cadastroCozinhaService.salvar(cozinha);
        URI uri = ResourceUriHelper.buildUri(savedCozinha.getId());
        return ResponseEntity.created(uri).body(cozinhaModelAssembler.toModel(savedCozinha));
    }

    @PutMapping("/{id}")
    public CozinhaModelV2 atualizar(@PathVariable Long id, @RequestBody @Valid CozinhaInputV2 cozinhaInput) {
        Cozinha cozinhaAtual = cadastroCozinhaService.buscar(id);
        cozinhaInputDisassembler.copyToDomainObject(cozinhaInput, cozinhaAtual);
        return cozinhaModelAssembler.toModel(cadastroCozinhaService.salvar(cozinhaAtual));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void remover(@PathVariable Long id) {
        cadastroCozinhaService.excluir(id);
    }

}
