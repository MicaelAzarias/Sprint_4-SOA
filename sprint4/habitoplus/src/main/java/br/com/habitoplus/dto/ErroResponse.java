package br.com.habitoplus.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Value Object para respostas de erro padronizadas.
 */
@Data
@Builder
public class ErroResponse {

    private int status;
    private String erro;
    private String mensagem;
    private LocalDateTime timestamp;
}
