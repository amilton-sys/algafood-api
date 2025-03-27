package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.RestauranteBasicoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteModelAssembler;
import com.algaworks.algafood.api.v1.assembler.RestauranteInputDisassembler;
import com.algaworks.algafood.api.v1.model.dto.RestauranteBasicoModel;
import com.algaworks.algafood.api.v1.model.dto.RestauranteModel;
import com.algaworks.algafood.api.v1.model.dto.input.RestauranteInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/restaurantes")
@Validated
public class RestauranteController {
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;

    @Autowired
    private RestauranteModelAssembler restauranteModelAssembler;

    @Autowired
    private RestauranteBasicoModelAssembler restauranteBasicoModelAssembler;

    @Autowired
    private RestauranteInputDisassembler restauranteInputDisassembler;
    
    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping
    public ResponseEntity<CollectionModel<RestauranteBasicoModel>> listar() {
        CollectionModel<RestauranteBasicoModel> restauranteModels = restauranteBasicoModelAssembler.toCollectionModel(restauranteRepository.findAll());
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                .body(restauranteModels);
    }

//    @GetMapping
//    public MappingJacksonValue listar(@RequestParam(required = false) String projecao) {
//        List<Restaurante> restaurantes = restauranteRepository.findAll();
//        List<RestauranteModel> restauranteModels = restauranteModelAssembler.toCollectionModel(restaurantes);
//
//
//        MappingJacksonValue restauranteWrapper = new MappingJacksonValue(restauranteModels);
//        restauranteWrapper.setSerializationView(RestauranteView.Resumo.class);
//
//        if ("apenas-nome".equals(projecao)) {
//            restauranteWrapper.setSerializationView(RestauranteView.ApenasNome.class);
//        } else if ("completo".equals(projecao)) {
//            restauranteWrapper.setSerializationView(null);
//        }
//
//        return restauranteWrapper;
//    }

//    @JsonView(RestauranteView.Resumo.class)
//    @GetMapping(params = "projecao=resumo")
//    public List<RestauranteModel> listarResumido() {
//       return listar();
//    }
//
//    @JsonView(RestauranteView.ApenasNome.class)
//    @GetMapping(params = "projecao=apenas-nome")
//    public List<RestauranteModel> apenasNome() {
//        return listar();
//    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping("/{id}")
    public RestauranteModel buscar(@PathVariable Long id) {
        Restaurante entity = cadastroRestauranteService.buscar(id);
        return restauranteModelAssembler.toModel(entity);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping("/{id}/ativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        cadastroRestauranteService.ativar(id);

        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @DeleteMapping("/{id}/inativar")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> inativar(@PathVariable Long id) {
        cadastroRestauranteService.inativar(id);

        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping("/ativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ativarEmMassa(@RequestBody @NotEmpty @NotNull List<Long> ids) {
        try {
            cadastroRestauranteService.ativar(ids);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @DeleteMapping("/inativacoes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void inativarEmMassa(@RequestBody @NotEmpty @NotNull List<Long> ids) {
        try {
            cadastroRestauranteService.inativar(ids);
        } catch (RestauranteNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @PutMapping("/{id}/abertura")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> abrir(@PathVariable Long id) {
        cadastroRestauranteService.abrir(id);

        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @PutMapping("/{id}/fechamento")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> fechar(@PathVariable Long id) {
        cadastroRestauranteService.fechar(id);

        return ResponseEntity.noContent().build();
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PostMapping
    public ResponseEntity<?> salvar(@RequestBody @Valid RestauranteInput restauranteInput) {
        try {
            Restaurante entity = restauranteInputDisassembler.toDomainObject(restauranteInput);
            Restaurante savedRestaurante = cadastroRestauranteService.salvar(entity);
            URI uri = UriComponentsBuilder.fromPath("restaurantes/{id}").buildAndExpand(savedRestaurante.getId())
                    .toUri();
            return ResponseEntity.created(uri).body(restauranteModelAssembler.toModel(savedRestaurante));
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @PutMapping("/{id}")
    public RestauranteModel atualizar(@PathVariable Long id, @RequestBody @Valid RestauranteInput restauranteInput) {
        try {
            Restaurante restauranteAtual = cadastroRestauranteService.buscar(id);
            restauranteInputDisassembler.copyToDomainObject(restauranteInput, restauranteAtual);
            return restauranteModelAssembler.toModel(cadastroRestauranteService.salvar(restauranteAtual));
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.Restaurantes.PodeGerenciarCadastro
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        cadastroRestauranteService.excluir(id);
    }
}
