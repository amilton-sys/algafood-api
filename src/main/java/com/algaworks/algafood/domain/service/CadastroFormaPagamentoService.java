package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.FormaPagamentoNaoEncontradaException;
import com.algaworks.algafood.domain.model.FormaPagamento;
import com.algaworks.algafood.domain.repository.FormaPagamentoRepository;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class CadastroFormaPagamentoService {
    @Autowired
    private FormaPagamentoRepository formaPagamentoRepository;
    private static final String MSG_RESTAURANTE_EM_USO = "FormaPagamento de código %d não pode ser removida, pois está em uso.";
    
    public FormaPagamento buscar(Long id) {
        return formaPagamentoRepository.findById(id).orElseThrow(
                () -> new FormaPagamentoNaoEncontradaException(id));
    }


    @Transactional
    public FormaPagamento salvar(FormaPagamento formaPagamento) {
        return formaPagamentoRepository.save(formaPagamento);
    }

    @Transactional
    public void excluir(Long id) {
        try {
            formaPagamentoRepository.deleteById(id);
            formaPagamentoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_RESTAURANTE_EM_USO, id));
        }
    }
}
