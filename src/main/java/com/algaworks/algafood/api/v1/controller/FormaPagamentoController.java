package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.FormaPagamentoInputDisassembler;
import com.algaworks.algafood.api.v1.assembler.FormaPagamentoModelAssembler;
import com.algaworks.algafood.api.helpers.ResourceUriHelper;
import com.algaworks.algafood.api.v1.model.dto.FormaPagamentoModel;
import com.algaworks.algafood.api.v1.model.dto.input.FormaPagamentoInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import com.algaworks.algafood.domain.service.CadastroFormaPagamentoService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.filter.ShallowEtagHeaderFilter;

import java.net.URI;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/v1/formas-pagamento")
public class FormaPagamentoController {
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;
    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamentoService;
    @Autowired
    private FormaPagamentoModelAssembler formaPagamentoModelAssembler;
    @Autowired
    private FormaPagamentoInputDisassembler formaPagamentoInputDisassembler;
    
    @CheckSecurity.FormasPagamento.PodeConsultar
    @GetMapping
    public ResponseEntity<CollectionModel<FormaPagamentoModel>> listar(ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";

        OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataUltimaAtualizacao();

        if (dataUltimaAtualizacao != null) {
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        List<FormaPagamento> formaPagamentos = formaPagamentoRepository.findAll();
        CollectionModel<FormaPagamentoModel> formaPagamentoModels = formaPagamentoModelAssembler.toCollectionModel(formaPagamentos);

        return ResponseEntity.ok()
//                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
//                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePrivate())
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS).cachePublic())
                .eTag(eTag)
//                .cacheControl(CacheControl.noCache())
//                .cacheControl(CacheControl.noStore())
                .body(formaPagamentoModels);
    }

    @CheckSecurity.FormasPagamento.PodeConsultar
    @GetMapping("/{id}")
    public ResponseEntity<FormaPagamentoModel> buscar(@PathVariable Long id, ServletWebRequest request) {
        ShallowEtagHeaderFilter.disableContentCaching(request.getRequest());

        String eTag = "0";

        OffsetDateTime dataUltimaAtualizacao = formaPagamentoRepository.getDataAtualizacaoById(id);

        if (dataUltimaAtualizacao != null) {
            eTag = String.valueOf(dataUltimaAtualizacao.toEpochSecond());
        }

        if (request.checkNotModified(eTag)) {
            return null;
        }

        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(10, TimeUnit.SECONDS))
                .eTag(eTag)
                .body(formaPagamentoModelAssembler.toModel(cadastroFormaPagamentoService.buscar(id)));
    }

    @CheckSecurity.FormasPagamento.PodeEditar
    @PostMapping
    public ResponseEntity<FormaPagamentoModel> salvar(@RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        try {
            FormaPagamento formaPagamento = formaPagamentoInputDisassembler.toDomainObject(formaPagamentoInput);

            FormaPagamento formaPagamentoDb = cadastroFormaPagamentoService.salvar(formaPagamento);

            URI uri = ResourceUriHelper.buildUri(formaPagamentoDb.getId());

            return ResponseEntity.created(uri).body(formaPagamentoModelAssembler.toModel(formaPagamentoDb));

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    @CheckSecurity.FormasPagamento.PodeEditar
    @PutMapping("/{id}")
    public FormaPagamentoModel atualizar(@PathVariable Long id, @RequestBody @Valid FormaPagamentoInput formaPagamentoInput) {
        try {
            FormaPagamento formaPagamentoAtual = cadastroFormaPagamentoService.buscar(id);

            formaPagamentoInputDisassembler.copyToDomainObject(formaPagamentoInput, formaPagamentoAtual);

            return formaPagamentoModelAssembler.toModel(cadastroFormaPagamentoService.salvar(formaPagamentoAtual));

        } catch (EstadoNaoEncontradoException e) {
            throw new NegocioException(e.getMessage(), e.getCause());
        }
    }

    @CheckSecurity.FormasPagamento.PodeEditar
    @DeleteMapping("/{id}")
    public void remover(@PathVariable Long id) {
        cadastroFormaPagamentoService.excluir(id);
    }
}
