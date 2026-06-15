package br.com.habitoplus.service;

import br.com.habitoplus.dto.LoginRequest;
import br.com.habitoplus.dto.TokenResponse;
import br.com.habitoplus.model.Credencial;
import br.com.habitoplus.repository.CredencialRepository;
import br.com.habitoplus.security.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável pela autenticação de colaboradores.
 * Encapsula a lógica de login, delegando ao AuthenticationManager do Spring Security.
 */
@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private CredencialRepository credencialRepository;

    /**
     * Autentica o colaborador e retorna um token JWT.
     *
     * @param request credenciais de login (email + senha)
     * @return VO com o token JWT e dados do colaborador
     */
    public TokenResponse login(LoginRequest request) {
        // Delega autenticação ao Spring Security (valida senha com BCrypt)
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
        String token = jwtService.gerarToken(userDetails);

        // Busca informações adicionais do colaborador
        Credencial credencial = credencialRepository.findByEmail(request.getEmail())
                .orElseThrow();

        return TokenResponse.builder()
                .token(token)
                .tipo("Bearer")
                .colaboradorId(credencial.getColaboradorId())
                .email(credencial.getEmail())
                .role(credencial.getRole())
                .build();
    }
}
