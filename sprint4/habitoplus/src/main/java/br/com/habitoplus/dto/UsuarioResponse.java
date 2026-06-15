package br.com.habitoplus.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Value Object de saída com os dados públicos de um colaborador.
 */
@Data
@Builder
public class UsuarioResponse {

    private Long id;
    private String colaboradorId;
    private String nome;
    private String email;
    private LocalDateTime dataCadastro;
}
