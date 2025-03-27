package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.v1.assembler.UsuarioInputDisassembler;
import com.algaworks.algafood.api.helpers.ResourceUriHelper;
import com.algaworks.algafood.api.v1.model.dto.UsuarioModel;
import com.algaworks.algafood.api.v1.model.dto.input.SenhaInput;
import com.algaworks.algafood.api.v1.model.dto.input.UsuarioComSenhaInput;
import com.algaworks.algafood.api.v1.model.dto.input.UsuarioInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/v1/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;
    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;
    @Autowired
    private UsuarioInputDisassembler usuarioInputDisassembler;

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<UsuarioModel> buscar() {
        return usuarioModelAssembler.toCollectionModel(usuarioRepository.findAll());
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping("/{id}")
    public UsuarioModel buscar(@PathVariable Long id) {
        return usuarioModelAssembler.toModel(cadastroUsuarioService.buscar(id));
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PostMapping
    public ResponseEntity<UsuarioModel> salvar(@RequestBody @Valid UsuarioComSenhaInput usuarioComSenhaInput) {
        try {
            Usuario usuario = usuarioInputDisassembler.toDomainObject(usuarioComSenhaInput);

            Usuario usuarioSalvo = cadastroUsuarioService.salvar(usuario);

            URI uri = ResourceUriHelper.buildUri(usuarioSalvo.getId());

            return ResponseEntity.created(uri).body(usuarioModelAssembler.toModel(usuarioSalvo));

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeAlterarUsuario
    @PutMapping("/{id}")
    public UsuarioModel atualizar(@PathVariable Long id, @RequestBody @Valid UsuarioInput usuarioInput) {
        try {
            Usuario usuarioAtual = cadastroUsuarioService.buscar(id);

            usuarioInputDisassembler.copyToDomainObject(usuarioInput, usuarioAtual);

            return usuarioModelAssembler.toModel(cadastroUsuarioService.salvar(usuarioAtual));

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e.getCause());
        }
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeAlterarPropriaSenha
    @PutMapping("/{id}/senha")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void alterarSenha(@PathVariable Long id, @RequestBody @Valid SenhaInput senha) {
        cadastroUsuarioService.alterarSenha(id, senha.getSenhaAtual(), senha.getNovaSenha());
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        cadastroUsuarioService.excluir(id);
    }
}
