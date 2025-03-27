package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.GrupoModelAssembler;
import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.dto.GrupoModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.service.CadastroUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/usuarios/{id}/grupos")
public class UsuarioGrupoController {
    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    @Autowired
    private GrupoModelAssembler grupoModelAssembler;
    @Autowired
    private AlgaLinks algaLinks;
    @Autowired
    private AlgaSecurity algaSecurity;


    @CheckSecurity.UsuariosGruposPermissoes.PodeConsultar
    @GetMapping
    public CollectionModel<GrupoModel> listar(@PathVariable Long usuarioId) {
        Usuario usuario = cadastroUsuarioService.buscar(usuarioId);

        CollectionModel<GrupoModel> gruposModel = grupoModelAssembler.toCollectionModel(usuario.getGrupos())
                .removeLinks();

        if (algaSecurity.podeEditarUsuariosGruposPermissoes()) {
            gruposModel.add(algaLinks.linkToUsuarioGrupoAssociacao(usuarioId, "associar"));

            gruposModel.getContent().forEach(grupoModel -> {
                grupoModel.add(algaLinks.linkToUsuarioGrupoDesassociacao(
                        usuarioId, grupoModel.getId(), "desassociar"));
            });
        }

        return gruposModel;
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @PutMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> associar(@PathVariable Long id, @PathVariable Long grupoId) {
        cadastroUsuarioService.associarGrupo(id, grupoId);
        
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.UsuariosGruposPermissoes.PodeEditar
    @DeleteMapping("/{grupoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> desassociar(@PathVariable Long id, @PathVariable Long grupoId) {
        cadastroUsuarioService.desassociarGrupo(id, grupoId);

        return ResponseEntity.noContent().build();
    }
}
