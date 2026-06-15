package br.com.habitoplus.controller;

import br.com.habitoplus.dto.HabitoRequest;
import br.com.habitoplus.enums.TipoAtividade;
import br.com.habitoplus.model.Credencial;
import br.com.habitoplus.model.RegistroHabito;
import br.com.habitoplus.model.Usuario;
import br.com.habitoplus.repository.CredencialRepository;
import br.com.habitoplus.repository.HabitoRepository;
import br.com.habitoplus.repository.UsuarioRepository;
import br.com.habitoplus.security.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

/**
 * Testes de integração para HabitoController.
 * Utiliza banco H2 em memória (profile "test") e MockMvc para simular requisições HTTP reais.
 * Autentica via JWT gerado para os testes.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("HabitoController - Testes de Integração")
class HabitoControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private HabitoRepository habitoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CredencialRepository credencialRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String jwtToken;
    private String jwtAdminToken;

    @BeforeEach
    void setUp() {
        habitoRepository.deleteAll();
        credencialRepository.deleteAll();
        usuarioRepository.deleteAll();

        // Cria colaborador de teste
        Usuario usuario = new Usuario();
        usuario.setColaboradorId("COL-TEST");
        usuario.setNome("Colaborador Teste");
        usuario.setEmail("teste@empresa.com");
        usuario.setDataCadastro(LocalDateTime.now());
        usuarioRepository.save(usuario);

        // Cria credencial USER
        Credencial credencial = new Credencial();
        credencial.setColaboradorId("COL-TEST");
        credencial.setEmail("teste@empresa.com");
        credencial.setSenha(passwordEncoder.encode("senha123"));
        credencial.setRole("ROLE_USER");
        credencialRepository.save(credencial);

        // Cria credencial ADMIN
        Usuario admin = new Usuario();
        admin.setColaboradorId("COL-ADMIN");
        admin.setNome("Administrador");
        admin.setEmail("admin@empresa.com");
        admin.setDataCadastro(LocalDateTime.now());
        usuarioRepository.save(admin);

        Credencial credAdmin = new Credencial();
        credAdmin.setColaboradorId("COL-ADMIN");
        credAdmin.setEmail("admin@empresa.com");
        credAdmin.setSenha(passwordEncoder.encode("admin123"));
        credAdmin.setRole("ROLE_ADMIN");
        credencialRepository.save(credAdmin);

        // Gera tokens JWT para os testes
        UserDetails userUser = User.withUsername("teste@empresa.com")
                .password("").authorities("ROLE_USER").build();
        jwtToken = jwtService.gerarToken(userUser);

        UserDetails userAdmin = User.withUsername("admin@empresa.com")
                .password("").authorities("ROLE_ADMIN").build();
        jwtAdminToken = jwtService.gerarToken(userAdmin);
    }

    // ──────────────────────────────────────────────────────────
    // POST /api/v1/habitos
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("POST /habitos - deve criar hábito e retornar 201")
    void deveCriarHabitoComSucesso() throws Exception {
        HabitoRequest request = new HabitoRequest();
        request.setColaboradorId("COL-TEST");
        request.setTipoAtividade(TipoAtividade.CORRIDA);
        request.setDescricao("Corrida de integração");
        request.setMinutosDuracao(30);

        mockMvc.perform(post("/api/v1/habitos")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.colaboradorId").value("COL-TEST"))
                .andExpect(jsonPath("$.pontosGerados").value(300))
                .andExpect(jsonPath("$.tipoAtividade").value("CORRIDA"));
    }

    @Test
    @DisplayName("POST /habitos - deve retornar 400 com dados inválidos")
    void deveRetornar400ComDadosInvalidos() throws Exception {
        HabitoRequest request = new HabitoRequest();
        request.setColaboradorId("COL-TEST");
        request.setTipoAtividade(TipoAtividade.CORRIDA);
        request.setMinutosDuracao(5); // Abaixo do mínimo de 10

        mockMvc.perform(post("/api/v1/habitos")
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.erro").value("Erro de Validação"));
    }

    @Test
    @DisplayName("POST /habitos - deve retornar 401 sem token")
    void deveRetornar401SemToken() throws Exception {
        mockMvc.perform(post("/api/v1/habitos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isUnauthorized());
    }

    // ──────────────────────────────────────────────────────────
    // GET /api/v1/habitos
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /habitos - deve listar todos os hábitos")
    void deveListarHabitosComSucesso() throws Exception {
        salvarHabitoTeste("COL-TEST", TipoAtividade.YOGA, 45, 270);

        mockMvc.perform(get("/api/v1/habitos")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))));
    }

    // ──────────────────────────────────────────────────────────
    // GET /api/v1/habitos/{id}
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /habitos/{id} - deve retornar hábito existente")
    void deveBuscarHabitoPorId() throws Exception {
        RegistroHabito salvo = salvarHabitoTeste("COL-TEST", TipoAtividade.MUSCULACAO, 60, 480);

        mockMvc.perform(get("/api/v1/habitos/" + salvo.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(salvo.getId()))
                .andExpect(jsonPath("$.tipoAtividade").value("MUSCULACAO"));
    }

    @Test
    @DisplayName("GET /habitos/{id} - deve retornar 404 para ID inexistente")
    void deveRetornar404ParaIdInexistente() throws Exception {
        mockMvc.perform(get("/api/v1/habitos/9999")
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    // ──────────────────────────────────────────────────────────
    // DELETE /api/v1/habitos/{id}
    // ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("DELETE /habitos/{id} - ADMIN deve deletar com sucesso (204)")
    void adminDeveDeletarHabito() throws Exception {
        RegistroHabito salvo = salvarHabitoTeste("COL-TEST", TipoAtividade.CAMINHADA, 20, 100);

        mockMvc.perform(delete("/api/v1/habitos/" + salvo.getId())
                        .header("Authorization", "Bearer " + jwtAdminToken))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /habitos/{id} - USER sem permissão deve receber 403")
    void userNaoDeveConseguirDeletar() throws Exception {
        RegistroHabito salvo = salvarHabitoTeste("COL-TEST", TipoAtividade.CAMINHADA, 20, 100);

        mockMvc.perform(delete("/api/v1/habitos/" + salvo.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isForbidden());
    }

    // ──────────────────────────────────────────────────────────
    // Helpers
    // ──────────────────────────────────────────────────────────

    private RegistroHabito salvarHabitoTeste(String colaboradorId, TipoAtividade tipo,
                                              int minutos, int pontos) {
        RegistroHabito habito = new RegistroHabito();
        habito.setColaboradorId(colaboradorId);
        habito.setTipoAtividade(tipo);
        habito.setDescricao("Hábito de teste");
        habito.setMinutosDuracao(minutos);
        habito.setPontosGerados(pontos);
        habito.setDataRegistro(LocalDateTime.now());
        return habitoRepository.save(habito);
    }
}
