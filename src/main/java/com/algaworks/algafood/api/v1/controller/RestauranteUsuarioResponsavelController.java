package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.UsuarioModelAssembler;
import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.dto.UsuarioModel;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/v1/restaurantes/{id}/responsaveis")
public class RestauranteUsuarioResponsavelController {

    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;
    @Autowired
    private UsuarioModelAssembler usuarioModelAssembler;
    @Autowired
    private AlgaLinks algaLinks;
    @Autowired
    private AlgaSecurity algaSecurity;


    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @GetMapping
    public CollectionModel<UsuarioModel> listar(@PathVariable Long restauranteId) {
        Restaurante restaurante = cadastroRestauranteService.buscar(restauranteId);

        CollectionModel<UsuarioModel> usuariosModel = usuarioModelAssembler
                .toCollectionModel(restaurante.getResponsaveis())
                .removeLinks();

        usuariosModel.add(algaLinks.linkToRestauranteResponsaveis(restauranteId));

        if (algaSecurity.podeGerenciarCadastroRestaurantes()) {
            usuariosModel.add(algaLinks.linkToRestauranteResponsavelAssociacao(restauranteId, "associar"));

            usuariosModel.getContent().stream().forEach(usuarioModel -> {
                usuarioModel.add(algaLinks.linkToRestauranteResponsavelDesassociacao(
                        restauranteId, usuarioModel.getId(), "desassociar"));
            });
        }

        return usuariosModel;
    }


    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping("/{responsavelId}")
    public ResponseEntity<Void> associar(@PathVariable Long id, @PathVariable Long responsavelId) {
        cadastroRestauranteService.associarResponsavel(id, responsavelId);
        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @DeleteMapping("/{responsavelId}")
    public ResponseEntity<Void> desassociar(@PathVariable Long id, @PathVariable Long responsavelId) {
        cadastroRestauranteService.desassociarResponsavel(id, responsavelId);
        return ResponseEntity.noContent().build();
    }
}
