package br.com.habitoplus.service;

import br.com.habitoplus.dto.HabitoRequest;
import br.com.habitoplus.dto.HabitoResponse;
import br.com.habitoplus.enums.TipoAtividade;
import br.com.habitoplus.exception.RecursoNaoEncontradoException;
import br.com.habitoplus.model.RegistroHabito;
import br.com.habitoplus.repository.HabitoRepository;
import br.com.habitoplus.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para HabitoService.
 * Todas as dependências são mockadas com Mockito para isolar a lógica de negócio.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("HabitoService - Testes Unitários")
class HabitoServiceTest {

    @Mock
    private HabitoRepository habitoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private HabitoService habitoService;

    private HabitoRequest requestValido;
    private RegistroHabito habitoSalvo;

    @BeforeEach
    void setUp() {
        requestValido = new HabitoRequest();
        requestValido.setColaboradorId("COL-001");
        requestValido.setTipoAtividade(TipoAtividade.CORRIDA);
        requestValido.setDescricao("Corrida matinal");
        requestValido.setMinutosDuracao(30);

        habitoSalvo = new RegistroHabito();
        habitoSalvo.setId(1L);
        habitoSalvo.setColaboradorId("COL-001");
        habitoSalvo.setTipoAtividade(TipoAtividade.CORRIDA);
        habitoSalvo.setDescricao("Corrida matinal");
        habitoSalvo.setMinutosDuracao(30);
        habitoSalvo.setPontosGerados(300); // 30 * 10 (multiplicador CORRIDA)
        habitoSalvo.setDataRegistro(LocalDateTime.now());
    }

    // ──────────────────────────────────────────────────────────
    // registrarHabito
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve registrar hábito e calcular pontos corretamente")
    void deveRegistrarHabitoComSucesso() {
        when(usuarioRepository.existsByColaboradorId("COL-001")).thenReturn(true);
        when(habitoRepository.save(any(RegistroHabito.class))).thenReturn(habitoSalvo);

        HabitoResponse response = habitoService.registrarHabito(requestValido);

        assertThat(response).isNotNull();
        assertThat(response.getColaboradorId()).isEqualTo("COL-001");
        assertThat(response.getTipoAtividade()).isEqualTo(TipoAtividade.CORRIDA);
        assertThat(response.getPontosGerados()).isEqualTo(300); // 30min × 10
        verify(habitoRepository, times(1)).save(any(RegistroHabito.class));
    }

    @Test
    @DisplayName("Deve lançar exceção ao registrar hábito para colaborador inexistente")
    void deveLancarExcecaoQuandoColaboradorNaoExiste() {
        when(usuarioRepository.existsByColaboradorId("COL-999")).thenReturn(false);
        requestValido.setColaboradorId("COL-999");

        assertThatThrownBy(() -> habitoService.registrarHabito(requestValido))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("COL-999");

        verify(habitoRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve calcular pontos corretamente para diferentes atividades")
    void deveCalcularPontosPorTipoDeAtividade() {
        // YOGA = 6x multiplicador
        requestValido.setTipoAtividade(TipoAtividade.YOGA);
        requestValido.setMinutosDuracao(60);

        RegistroHabito habitoYoga = new RegistroHabito();
        habitoYoga.setId(2L);
        habitoYoga.setColaboradorId("COL-001");
        habitoYoga.setTipoAtividade(TipoAtividade.YOGA);
        habitoYoga.setMinutosDuracao(60);
        habitoYoga.setPontosGerados(360); // 60 * 6
        habitoYoga.setDataRegistro(LocalDateTime.now());

        when(usuarioRepository.existsByColaboradorId("COL-001")).thenReturn(true);
        when(habitoRepository.save(any(RegistroHabito.class))).thenReturn(habitoYoga);

        HabitoResponse response = habitoService.registrarHabito(requestValido);

        assertThat(response.getPontosGerados()).isEqualTo(360);
    }

    // ──────────────────────────────────────────────────────────
    // buscarPorId
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve retornar hábito ao buscar por ID existente")
    void deveBuscarHabitoPorId() {
        when(habitoRepository.findById(1L)).thenReturn(Optional.of(habitoSalvo));

        HabitoResponse response = habitoService.buscarPorId(1L);

        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar por ID inexistente")
    void deveLancarExcecaoAoBuscarIdInexistente() {
        when(habitoRepository.findById(999L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> habitoService.buscarPorId(999L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("999");
    }

    // ──────────────────────────────────────────────────────────
    // listarTodos
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve retornar lista de hábitos")
    void deveListarTodosOsHabitos() {
        when(habitoRepository.findAll()).thenReturn(List.of(habitoSalvo));

        List<HabitoResponse> lista = habitoService.listarTodos();

        assertThat(lista).hasSize(1);
        assertThat(lista.get(0).getColaboradorId()).isEqualTo("COL-001");
    }

    // ──────────────────────────────────────────────────────────
    // deletarHabito
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve deletar hábito existente sem lançar exceção")
    void deveDeletarHabitoComSucesso() {
        when(habitoRepository.existsById(1L)).thenReturn(true);
        doNothing().when(habitoRepository).deleteById(1L);

        assertThatCode(() -> habitoService.deletarHabito(1L)).doesNotThrowAnyException();
        verify(habitoRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar ID inexistente")
    void deveLancarExcecaoAoDeletarInexistente() {
        when(habitoRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> habitoService.deletarHabito(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class);

        verify(habitoRepository, never()).deleteById(any());
    }
}
