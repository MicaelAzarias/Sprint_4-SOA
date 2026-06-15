package br.com.habitoplus.service;

import br.com.habitoplus.dto.LoginRequest;
import br.com.habitoplus.dto.TokenResponse;
import br.com.habitoplus.model.Credencial;
import br.com.habitoplus.repository.CredencialRepository;
import br.com.habitoplus.security.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Testes unitários para AuthService.
 * Verifica o fluxo de autenticação e geração de token JWT.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("AuthService - Testes Unitários")
class AuthServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private CredencialRepository credencialRepository;

    @InjectMocks
    private AuthService authService;

    private LoginRequest loginRequest;
    private Credencial credencial;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        loginRequest = new LoginRequest();
        loginRequest.setEmail("ana.silva@empresa.com");
        loginRequest.setSenha("senha123");

        credencial = new Credencial();
        credencial.setColaboradorId("COL-001");
        credencial.setEmail("ana.silva@empresa.com");
        credencial.setSenha("$2a$10$hash");
        credencial.setRole("ROLE_USER");

        userDetails = User.withUsername("ana.silva@empresa.com")
                .password("$2a$10$hash")
                .authorities("ROLE_USER")
                .build();
    }

    @Test
    @DisplayName("Deve autenticar e retornar token JWT com sucesso")
    void deveAutenticarERetornarToken() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(null);
        when(userDetailsService.loadUserByUsername("ana.silva@empresa.com"))
                .thenReturn(userDetails);
        when(jwtService.gerarToken(userDetails))
                .thenReturn("mocked-jwt-token");
        when(credencialRepository.findByEmail("ana.silva@empresa.com"))
                .thenReturn(Optional.of(credencial));

        TokenResponse response = authService.login(loginRequest);

        assertThat(response).isNotNull();
        assertThat(response.getToken()).isEqualTo("mocked-jwt-token");
        assertThat(response.getTipo()).isEqualTo("Bearer");
        assertThat(response.getColaboradorId()).isEqualTo("COL-001");
        assertThat(response.getEmail()).isEqualTo("ana.silva@empresa.com");
        assertThat(response.getRole()).isEqualTo("ROLE_USER");
    }

    @Test
    @DisplayName("Deve lançar exceção com credenciais inválidas")
    void deveLancarExcecaoComCredenciaisInvalidas() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Credenciais inválidas"));

        assertThatThrownBy(() -> authService.login(loginRequest))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Credenciais inválidas");

        verify(jwtService, never()).gerarToken(any());
    }

    @Test
    @DisplayName("Deve chamar AuthenticationManager com email e senha corretos")
    void deveChamarAuthManagerComDadosCorretos() {
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);
        when(jwtService.gerarToken(any())).thenReturn("token");
        when(credencialRepository.findByEmail(any())).thenReturn(Optional.of(credencial));

        authService.login(loginRequest);

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("ana.silva@empresa.com", "senha123")
        );
    }
}
