package br.com.habitoplus.dto;

import lombok.Builder;
import lombok.Data;

/**
 * VO de resposta após autenticação bem-sucedida.
 * Retorna o token JWT e informações básicas do colaborador.
 */
@Data
@Builder
public class TokenResponse {

    private String token;
    private String tipo;
    private String colaboradorId;
    private String email;
    private String role;
}
