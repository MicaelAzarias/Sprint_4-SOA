package br.com.habitoplus.dto;

import br.com.habitoplus.enums.TipoAtividade;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Value Object de saída para os dados de um hábito registrado.
 * Garante que apenas os campos relevantes sejam expostos na resposta.
 */
@Data
@Builder
public class HabitoResponse {

    private Long id;
    private String colaboradorId;
    private TipoAtividade tipoAtividade;
    private String descricaoAtividade;
    private String descricao;
    private Integer minutosDuracao;
    private Integer pontosGerados;
    private LocalDateTime dataRegistro;
}
