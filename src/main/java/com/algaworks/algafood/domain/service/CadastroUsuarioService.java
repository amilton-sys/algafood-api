package com.algaworks.algafood.domain.service;

import com.algaworks.algafood.domain.exception.EntidadeEmUsoException;
import com.algaworks.algafood.domain.exception.NegocioException;
import com.algaworks.algafood.domain.exception.UsuarioNaoEncontradoException;
import com.algaworks.algafood.domain.model.Grupo;
import com.algaworks.algafood.domain.model.Usuario;
import com.algaworks.algafood.domain.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class CadastroUsuarioService {
    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private CadastroGrupoService cadastroGrupoService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static final String MSG_USUARIO_EM_USO = "Usuario de código %d não pode ser removido, pois está em uso.";

    public Usuario buscar(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new UsuarioNaoEncontradoException(id));
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        usuarioRepository.detach(usuario);
        Optional<Usuario> usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());
        if (usuarioExistente.isPresent() && !usuarioExistente.get().equals(usuario)) {
            throw new NegocioException(String.format("Já existe um usuário com o e-mail: %s", usuario.getEmail()));
        }
        if (usuario.isNovo()) {
            usuario.setSenha(passwordEncoder.encode(usuario.getSenha()));
        }
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void alterarSenha(Long id, String senhaAtual, String novaSenha) {
        Usuario usuario = buscar(id);
        if (!senhaAtual.matches(usuario.getSenha())) {
            throw new NegocioException("Senha atual informada não coincide com a senha do usuário.");
        }
        usuario.setSenha(passwordEncoder.encode(novaSenha));
    }

    @Transactional
    public void excluir(Long id) {
        try {
            usuarioRepository.deleteById(id);
            usuarioRepository.flush();
        } catch (DataIntegrityViolationException e) {
            throw new EntidadeEmUsoException(
                    String.format(MSG_USUARIO_EM_USO, id));
        }
    }

    @Transactional
    public void associarGrupo(Long id, Long grupoId) {
        Usuario usuario = buscar(id);
        Grupo grupo = cadastroGrupoService.buscar(grupoId);

        usuario.associarGrupo(grupo);
    }

    @Transactional
    public void desassociarGrupo(Long id, Long grupoId) {
        Usuario usuario = buscar(id);
        Grupo grupo = cadastroGrupoService.buscar(grupoId);

        usuario.desassociarGrupo(grupo);
    }
}
