package br.com.habitoplus.service;

import br.com.habitoplus.dto.UsuarioRequest;
import br.com.habitoplus.dto.UsuarioResponse;
import br.com.habitoplus.exception.RecursoNaoEncontradoException;
import br.com.habitoplus.model.Usuario;
import br.com.habitoplus.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas regras de negócio de colaboradores.
 */
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    /**
     * Cadastra um novo colaborador.
     */
    public UsuarioResponse cadastrar(UsuarioRequest request) {
        Usuario usuario = new Usuario();
        usuario.setColaboradorId(request.getColaboradorId());
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setDataCadastro(LocalDateTime.now());
        return toResponse(repository.save(usuario));
    }

    /**
     * Retorna todos os colaboradores cadastrados.
     */
    public List<UsuarioResponse> listarTodos() {
        return repository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca um colaborador pelo colaboradorId.
     */
    public UsuarioResponse buscarPorColaboradorId(String colaboradorId) {
        Usuario usuario = repository.findByColaboradorId(colaboradorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Colaborador com ID '" + colaboradorId + "' não encontrado."));
        return toResponse(usuario);
    }

    /**
     * Remove um colaborador e todos os seus hábitos (cascade).
     */
    public void deletar(String colaboradorId) {
        Usuario usuario = repository.findByColaboradorId(colaboradorId)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Colaborador com ID '" + colaboradorId + "' não encontrado."));
        repository.delete(usuario);
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return UsuarioResponse.builder()
                .id(usuario.getId())
                .colaboradorId(usuario.getColaboradorId())
                .nome(usuario.getNome())
                .email(usuario.getEmail())
                .dataCadastro(usuario.getDataCadastro())
                .build();
    }
}
