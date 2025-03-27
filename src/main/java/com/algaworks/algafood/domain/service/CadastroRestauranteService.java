package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.RestauranteNaoEncontradoException;
import com.algaworks.algafood.domain.model.*;
import com.algaworks.algafood.domain.repository.RestauranteRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CadastroRestauranteService {
    @Autowired
    private RestauranteRepository restauranteRepository;
    @Autowired
    private CadastroCozinhaService cadastroCozinhaService;
    @Autowired
    private CadastroCidadeService cadastroCidadeService;
    @Autowired
    private CadastroFormaPagamentoService cadastroFormaPagamentoService;
    @Autowired
    private CadastroUsuarioService cadastroUsuarioService;

    private static final String MSG_RESTAURANTE_EM_USO = "Restaurante de código %d não pode ser removida, pois está em uso.";

    public Restaurante buscar(Long id) {
        return restauranteRepository.findById(id).orElseThrow(
                () -> new RestauranteNaoEncontradoException(id));
    }

    @Transactional
    public void ativar(Long id) {
        Restaurante restaurante = buscar(id);
        restaurante.ativar();
    }

    @Transactional
    public void inativar(Long id) {
        Restaurante restaurante = buscar(id);
        restaurante.inativar();
    }

    @Transactional
    public void ativar(List<Long> ids) {
        ids.forEach(this::ativar);
    }

    @Transactional
    public void inativar(List<Long> ids) {
        ids.forEach(this::inativar);
    }

    @Transactional
    public void abrir(Long id) {
        Restaurante restaurante = buscar(id);
        restaurante.abrir();
    }

    @Transactional
    public void fechar(Long id) {
        Restaurante restaurante = buscar(id);
        restaurante.fechar();
    }

    @Transactional
    public void desassociarFormaPagamento(Long id, Long formaPagamentoId) {
        Restaurante restaurante = buscar(id);
        FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscar(formaPagamentoId);

        restaurante.desassociarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void associarFormaPagamento(Long id, Long formaPagamentoId) {
        Restaurante restaurante = buscar(id);
        FormaPagamento formaPagamento = cadastroFormaPagamentoService.buscar(formaPagamentoId);

        restaurante.associarFormaPagamento(formaPagamento);
    }

    @Transactional
    public void associarResponsavel(Long id, Long responsavelId) {
        Restaurante restaurante = buscar(id);
        Usuario usuario = cadastroUsuarioService.buscar(responsavelId);

        restaurante.associarResponsavel(usuario);
    }

    @Transactional
    public void desassociarResponsavel(Long id, Long responsavelId) {
        Restaurante restaurante = buscar(id);
        Usuario usuario = cadastroUsuarioService.buscar(responsavelId);

        restaurante.desassociarResponsavel(usuario);
    }

    @Transactional
    public Restaurante salvar(Restaurante restaurante) {
        Long cozinhaId = restaurante.getCozinha().getId();
        Long cidadeId = restaurante.getEndereco().getCidade().getId();

        Cozinha cozinha = cadastroCozinhaService.buscar(cozinhaId);
        Cidade cidade = cadastroCidadeService.buscar(cidadeId);

        restaurante.setCozinha(cozinha);
        restaurante.getEndereco().setCidade(cidade);

        return restauranteRepository.save(restaurante);
    }

    @Transactional
    public void excluir(Long id) {
        try {
            restauranteRepository.deleteById(id);
            restauranteRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_RESTAURANTE_EM_USO, id));
        }
    }
}
