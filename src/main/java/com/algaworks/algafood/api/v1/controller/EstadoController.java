package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.EstadoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.EstadoModelAssembler;
import com.algaworks.algafood.api.helpers.ResourceUriHelper;
import com.algaworks.algafood.api.v1.model.dto.input.EstadoInput;
import com.algaworks.algafood.api.v1.model.dto.EstadoModel;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import com.algaworks.algafood.domain.service.CadastroEstadoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/estados")
public class EstadoController {
    @Autowired
    private EstadoRepository estadoRepository;
    @Autowired
    private CadastroEstadoService cadastroEstadoService;
    @Autowired
    private EstadoModelAssembler estadoModelAssembler;
    @Autowired
    private EstadoInputDisassembler estadoInputDisassembler;
    
    @CheckSecurity.Estados.PodeConsultar
    @GetMapping
    public CollectionModel<EstadoModel> listar() {
        return estadoModelAssembler.toCollectionModel(estadoRepository.findAll());
    }

    @CheckSecurity.Estados.PodeConsultar
    @GetMapping("/{id}")
    public EstadoModel buscar(@PathVariable Long id) {
        return estadoModelAssembler.toModel(cadastroEstadoService.buscar(id));
    }

    @CheckSecurity.Estados.PodeEditar
    @PostMapping
    public ResponseEntity<EstadoModel> salvar(@RequestBody @Valid EstadoInput estadoInput) {
        Estado estado = estadoInputDisassembler.toDomainObject(estadoInput);
        Estado savedEstado = cadastroEstadoService.salvar(estado);
        URI uri = ResourceUriHelper.buildUri(savedEstado.getId());
        return ResponseEntity.created(uri).body(estadoModelAssembler.toModel(savedEstado));
    }

    @CheckSecurity.Estados.PodeEditar
    @PutMapping("/{id}")
    public EstadoModel atualizar(@PathVariable Long id, @RequestBody @Valid EstadoInput estadoInput) {
        Estado estadoAtual = cadastroEstadoService.buscar(id);
        estadoInputDisassembler.copyToDomainObject(estadoInput, estadoAtual);
        return estadoModelAssembler.toModel(cadastroEstadoService.salvar(estadoAtual));
    }

    @CheckSecurity.Estados.PodeEditar
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        cadastroEstadoService.excluir(id);
    }
}
