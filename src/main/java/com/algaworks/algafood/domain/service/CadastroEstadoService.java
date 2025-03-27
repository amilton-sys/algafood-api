package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.EstadoNaoEncontradoException;
import com.algaworks.algafood.domain.model.Estado;
import com.algaworks.algafood.domain.repository.EstadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class CadastroEstadoService {
    @Autowired
    private EstadoRepository estadoRepository;
    private static final String MSG_ESTADO_EM_USO = "Estado de código %d não pode ser removida, pois está em uso.";

    public Estado buscar(Long id) {
        return estadoRepository.findById(id).orElseThrow(() -> new EstadoNaoEncontradoException(id));
    }

    @Transactional
    public Estado salvar(Estado estado) {
        return estadoRepository.save(estado);
    }

    @Transactional
    public void excluir(Long id) {
        try {
            estadoRepository.deleteById(id);
            estadoRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_ESTADO_EM_USO, id));
        }
    }
}
