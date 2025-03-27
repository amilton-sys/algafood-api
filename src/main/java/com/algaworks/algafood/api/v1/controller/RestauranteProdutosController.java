package com.algaworks.algafood.api.v1.controller;

import com.algaworks.algafood.api.v1.assembler.ProdutoModelAssembler;
import com.algaworks.algafood.api.v1.assembler.ProdutoInputDisassembler;
import com.algaworks.algafood.api.v1.AlgaLinks;
import com.algaworks.algafood.api.v1.model.dto.ProdutoModel;
import com.algaworks.algafood.api.v1.model.dto.input.ProdutoInput;
import com.algaworks.algafood.core.security.CheckSecurity;
import com.algaworks.algafood.domain.model.Produto;
import com.algaworks.algafood.domain.model.Restaurante;
import com.algaworks.algafood.domain.repository.ProdutoRepository;
import com.algaworks.algafood.domain.service.CadastroProdutoService;
import com.algaworks.algafood.domain.service.CadastroRestauranteService;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/restaurantes/{id}/produtos")
public class RestauranteProdutosController {
    @Autowired
    private ProdutoRepository produtoRepository;
    @Autowired
    private CadastroRestauranteService cadastroRestauranteService;
    @Autowired
    private ProdutoModelAssembler produtoModelAssembler;
    @Autowired
    private ProdutoInputDisassembler produtoInputDisassembler;
    @Autowired
    private CadastroProdutoService cadastroProdutoService;
    @Autowired
    private AlgaLinks algaLinks;

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping
    public CollectionModel<ProdutoModel> listar(@PathVariable Long id, @RequestParam(required = false) Boolean incluirInativos) {
        Restaurante restaurante = cadastroRestauranteService.buscar(id);
        List<Produto> todosProdutos;
        if (incluirInativos) {
            todosProdutos = restaurante.getProdutos();
        } else {
            todosProdutos = produtoRepository.findAtivoByRestaurante(restaurante);
        }
        return produtoModelAssembler.toCollectionModel(todosProdutos)
                .add(algaLinks.linkToProdutos(id));
    }

    @CheckSecurity.Restaurantes.PodeConsultar
    @GetMapping("/{produtoId}")
    public ProdutoModel buscar(@PathVariable Long id, @PathVariable Long produtoId) {
        Produto produto = cadastroProdutoService.buscar(id, produtoId);
        return produtoModelAssembler.toModel(produto);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProdutoModel adicionar(@PathVariable Long id,
                                  @RequestBody @Valid ProdutoInput produtoInput) {
        Restaurante restaurante = cadastroRestauranteService.buscar(id);

        Produto produto = produtoInputDisassembler.toDomainObject(produtoInput);
        produto.setRestaurante(restaurante);

        produto = cadastroProdutoService.salvar(produto);

        return produtoModelAssembler.toModel(produto);
    }

    @CheckSecurity.Restaurantes.PodeGerenciarFuncionamento
    @PutMapping("/{produtoId}")
    public ProdutoModel atualizar(@PathVariable Long id, @PathVariable Long produtoId,
                                  @RequestBody @Valid ProdutoInput produtoInput) {
        Produto produtoAtual = cadastroProdutoService.buscar(id, produtoId);

        produtoInputDisassembler.copyToDomainObject(produtoInput, produtoAtual);

        produtoAtual = cadastroProdutoService.salvar(produtoAtual);

        return produtoModelAssembler.toModel(produtoAtual);
    }
}
