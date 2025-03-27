package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.CidadeNaoEncontradaException;
import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.model.Cidade;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CadastroCidadeService {
    @Autowired
    private CidadeRepository cidadeRepository;
    @Autowired
    private CadastroEstadoService cadastroEstadoService;
    private static final String MSG_CIDADE_EM_USO = "Cidade de código %d não pode ser removida, pois está em uso.";

    public Cidade buscar(Long id) {
        return cidadeRepository.findById(id).orElseThrow(() -> new CidadeNaoEncontradaException(id));
    }

    @Transactional
    public Cidade salvar(Cidade cidade) {
        Long estadoId = cidade.getEstado().getId();
        Estado estado = cadastroEstadoService.buscar(estadoId);
        cidade.setEstado(estado);
        return cidadeRepository.save(cidade);
    }

    @Transactional
    public void excluir(Long id) {
        try {
            cidadeRepository.deleteById(id);
            cidadeRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_CIDADE_EM_USO, id));
        }
    }
}
