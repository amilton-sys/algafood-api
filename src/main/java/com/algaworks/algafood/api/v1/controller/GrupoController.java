package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.GrupoInputDisassembler;
import com.algaworks.algafood.api.helpers.ResourceUriHelper;
import com.algaworks.algafood.api.v1.model.dto.GrupoModel;
import com.algaworks.algafood.api.v1.model.dto.input.GrupoInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.repository.GrupoRepository;
import com.algaworks.algafood.domain.service.CadastroGrupoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/grupos")
public class GrupoController {
    @Autowired
    private GrupoRepository grupoRepository;
    @Autowired
    private CadastroGrupoService cadastroGrupoService;
    @Autowired
    private GrupoModelAssembler grupoModelAssembler;
    @Autowired
    private GrupoInputDisassembler grupoInputDisassembler;
    
    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<GrupoModel> listar() {
        return grupoModelAssembler.toCollectionModel(grupoRepository.findAll());
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping("/{id}")
    public GrupoModel buscar(@PathVariable Long id) {
        return grupoModelAssembler.toModel(cadastroGrupoService.buscar(id));
    }
    
    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PostMapping
    public ResponseEntity<GrupoModel> salvar(@RequestBody @Valid GrupoInput grupoInput) {
        try {
            Grupo grupo = grupoInputDisassembler.toDomainObject(grupoInput);

            Grupo savedGrupo = cadastroGrupoService.salvar(grupo);

            URI uri = ResourceUriHelper.buildUri(savedGrupo.getId());

            return ResponseEntity.created(uri).body(grupoModelAssembler.toModel(savedGrupo));

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping("/{id}")
    public GrupoModel atualizar(@PathVariable Long id, @RequestBody @Valid GrupoInput grupoInput) {
        try {
            Grupo grupoAtual = cadastroGrupoService.buscar(id);

            grupoInputDisassembler.copyToDomainObject(grupoInput, grupoAtual);

            return grupoModelAssembler.toModel(cadastroGrupoService.salvar(grupoAtual));

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e.getCause());
        }
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        cadastroGrupoService.excluir(id);
    }
}
