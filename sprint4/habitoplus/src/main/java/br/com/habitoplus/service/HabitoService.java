package br.com.habitoplus.service;

import br.com.habitoplus.dto.HabitoRequest;
import br.com.habitoplus.dto.HabitoResponse;
import br.com.habitoplus.exception.RecursoNaoEncontradoException;
import br.com.habitoplus.model.RegistroHabito;
import br.com.habitoplus.repository.HabitoRepository;
import br.com.habitoplus.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pelas regras de negócio relacionadas aos hábitos saudáveis.
 * Encapsula a lógica de pontuação e as operações CRUD sobre os registros.
 */
@Service
public class HabitoService {

    @Autowired
    private HabitoRepository habitoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    /**
     * Registra um novo hábito para um colaborador e calcula os pontos gerados.
     *
     * @param request dados da atividade realizada
     * @return VO com os dados do registro criado
     */
    public HabitoResponse registrarHabito(HabitoRequest request) {
        // Valida se o colaborador existe
        if (!usuarioRepository.existsByColaboradorId(request.getColaboradorId())) {
            throw new RecursoNaoEncontradoException(
                    "Colaborador com ID '" + request.getColaboradorId() + "' não encontrado.");
        }

        int pontos = calcularPontos(request);

        RegistroHabito habito = new RegistroHabito();
        habito.setColaboradorId(request.getColaboradorId());
        habito.setTipoAtividade(request.getTipoAtividade());
        habito.setDescricao(request.getDescricao());
        habito.setMinutosDuracao(request.getMinutosDuracao());
        habito.setPontosGerados(pontos);
        habito.setDataRegistro(LocalDateTime.now());

        RegistroHabito salvo = habitoRepository.save(habito);
        return toResponse(salvo);
    }

    /**
     * Retorna todos os hábitos registrados no sistema.
     */
    public List<HabitoResponse> listarTodos() {
        return habitoRepository.findAll()
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Busca um hábito específico pelo seu ID.
     *
     * @param id identificador do registro
     * @return VO com os dados do hábito
     */
    public HabitoResponse buscarPorId(Long id) {
        RegistroHabito habito = habitoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Hábito com ID " + id + " não encontrado."));
        return toResponse(habito);
    }

    /**
     * Retorna todos os hábitos de um colaborador específico.
     *
     * @param colaboradorId ID do colaborador
     * @return lista de VOs
     */
    public List<HabitoResponse> listarPorColaborador(String colaboradorId) {
        if (!usuarioRepository.existsByColaboradorId(colaboradorId)) {
            throw new RecursoNaoEncontradoException(
                    "Colaborador com ID '" + colaboradorId + "' não encontrado.");
        }
        return habitoRepository.findByColaboradorId(colaboradorId)
                .stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    /**
     * Atualiza os dados de um hábito já registrado.
     *
     * @param id      ID do registro a atualizar
     * @param request novos dados
     * @return VO atualizado
     */
    public HabitoResponse atualizarHabito(Long id, HabitoRequest request) {
        RegistroHabito habito = habitoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(
                        "Hábito com ID " + id + " não encontrado."));

        habito.setTipoAtividade(request.getTipoAtividade());
        habito.setDescricao(request.getDescricao());
        habito.setMinutosDuracao(request.getMinutosDuracao());
        habito.setPontosGerados(calcularPontos(request));

        return toResponse(habitoRepository.save(habito));
    }

    /**
     * Remove um hábito pelo seu ID.
     *
     * @param id ID do registro a remover
     */
    public void deletarHabito(Long id) {
        if (!habitoRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Hábito com ID " + id + " não encontrado.");
        }
        habitoRepository.deleteById(id);
    }

    /**
     * Calcula os pontos gerados com base no tipo e duração da atividade.
     * A regra utiliza o multiplicador definido no enum TipoAtividade.
     */
    private int calcularPontos(HabitoRequest request) {
        return request.getMinutosDuracao() * request.getTipoAtividade().getMultiplicadorPontos();
    }

    /** Converte a entidade para o VO de resposta. */
    private HabitoResponse toResponse(RegistroHabito habito) {
        return HabitoResponse.builder()
                .id(habito.getId())
                .colaboradorId(habito.getColaboradorId())
                .tipoAtividade(habito.getTipoAtividade())
                .descricaoAtividade(habito.getTipoAtividade().getDescricao())
                .descricao(habito.getDescricao())
                .minutosDuracao(habito.getMinutosDuracao())
                .pontosGerados(habito.getPontosGerados())
                .dataRegistro(habito.getDataRegistro())
                .build();
    }
}
