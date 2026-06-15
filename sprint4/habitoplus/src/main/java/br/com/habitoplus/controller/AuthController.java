package br.com.habitoplus.controller;

import br.com.habitoplus.dto.ErroResponse;
import br.com.habitoplus.dto.LoginRequest;
import br.com.habitoplus.dto.TokenResponse;
import br.com.habitoplus.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller REST para autenticação de colaboradores.
 * Expõe endpoints públicos de login e retorna token JWT.
 */
@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Autenticação", description = "Endpoints de login e geração de token JWT")
@SecurityRequirement(name = "")  // Sem auth (endpoints públicos)
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * POST /api/v1/auth/login
     * Autentica o colaborador e retorna um token JWT Bearer.
     */
    @Operation(
            summary = "Login do colaborador",
            description = "Autentica com e-mail e senha. Retorna token JWT válido por 24h."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Autenticação bem-sucedida",
                    content = @Content(schema = @Schema(implementation = TokenResponse.class),
                            examples = @ExampleObject(value = """
                                    {
                                      "token": "eyJhbGciOiJIUzI1NiJ9...",
                                      "tipo": "Bearer",
                                      "colaboradorId": "COL-001",
                                      "email": "ana.silva@empresa.com",
                                      "role": "ROLE_USER"
                                    }"""))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(schema = @Schema(implementation = ErroResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}
