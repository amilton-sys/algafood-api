package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.PedidoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.PedidoResumoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.PedidoInputDisassembler;
import com.algaworks.algafood.api.v1.model.dto.PedidoModel;
import com.algaworks.algafood.api.v1.model.dto.PedidoResumoModel;
import com.algaworks.algafood.api.v1.model.dto.input.PedidoInput;
import com.algaworks.algafood.core.data.PageWrapper;
import com.algaworks.algafood.core.data.PageableTranslator;
import com.algaworks.algafood.core.security.AlgaSecurity;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.exception.EntidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.filter.PedidoFilter;
import com.algaworks.algafood.domain.model.Pedido;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.PedidoRepository;
import com.algaworks.algafood.domain.service.EmissaoPedidoService;
import com.algaworks.algafood.infra.repository.spec.PedidoSpecs;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(value = "/v1/pedidos")
public class PedidoController {

    @Autowired
    private PedidoRepository pedidoRepository;
    @Autowired
    private EmissaoPedidoService emissaoPedidoService;
    @Autowired
    private PedidoModelAssembler pedidoModelAssembler;
    @Autowired
    private PedidoResumoModelAssembler pedidoResumoModelAssembler;
    @Autowired
    private PedidoInputDisassembler pedidoInputDisassembler;
    @Autowired
    private PagedResourcesAssembler<Pedido> pagedResourcesAssembler;
    @Autowired
    private AlgaSecurity algaSecurity;
    
    @CheckSecurity.Pedidos.PodePesquisar
    @GetMapping
    public PagedModel<PedidoResumoModel> pesquisar(PedidoFilter filtro, Pageable pageable) {
        Pageable pageableTraduzido = traduzirPageable(pageable);

        Page<Pedido> pedidosPage = pedidoRepository.findAll(PedidoSpecs.usandoFiltro(filtro), pageableTraduzido);

        pedidosPage = new PageWrapper<>(pedidosPage, pageable);

        return pagedResourcesAssembler.toModel(pedidosPage, pedidoResumoModelAssembler);
    }

//    @GetMapping
//    public MappingJacksonValue listar(@RequestParam(required = false) String campos) {
//        List<Pedido> pedidos = pedidoRepository.findAll();
//
//        List<PedidoResumoModel> pedidoResumoModels = pedidoResumoModelAssembler.toCollectionModel(pedidos);
//        MappingJacksonValue pedidoResumoWrapper = new MappingJacksonValue(pedidoResumoModels);
//
//        SimpleFilterProvider filterProvider = new SimpleFilterProvider();
//
//        filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.serializeAll());
//
//        if (StringUtils.isNotBlank(campos)) {
//            filterProvider.addFilter("pedidoFilter", SimpleBeanPropertyFilter.filterOutAllExcept(campos.split(",")));
//        }
//
//        pedidoResumoWrapper.setFilters(filterProvider);
//
//        return pedidoResumoWrapper;
//    }

//    @GetMapping
//    public List<PedidoResumoModel> listar() {
//        List<Pedido> todosPedidos = pedidoRepository.findAll();
//
//        return pedidoResumoModelAssembler.toCollectionModel(todosPedidos);
//    }
    
    @CheckSecurity.Pedidos.PodeBuscar
    @GetMapping("/{codigo}")
    public PedidoModel buscar(@PathVariable String codigo) {
        Pedido pedido = emissaoPedidoService.buscar(codigo);
        return pedidoModelAssembler.toModel(pedido);
    }
    
    @CheckSecurity.Pedidos.PodeCriar
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PedidoModel adicionar(@Valid @RequestBody PedidoInput pedidoInput) {
        try {
            Pedido novoPedido = pedidoInputDisassembler.toDomainObject(pedidoInput);

            novoPedido.setCliente(new Usuario());
            novoPedido.getCliente().setId(algaSecurity.getUsuarioId());

            novoPedido = emissaoPedidoService.emitir(novoPedido);

            return pedidoModelAssembler.toModel(novoPedido);
        } catch (EntidadeNaoEncontradaException e) {
            throw new NegocioException(e.getMessage(), e);
        }
    }

    private Pageable traduzirPageable(Pageable pageable) {
        var mapeamento = Map.of(
                "codigo", "codigo",
                "cliente.nome", "cliente.nome",
                "restaurante.nome", "restaurante.nome",
                "valorTotal", "valorTotal"
        );

        return PageableTranslator.translate(pageable, mapeamento);
    }
}  