package br.com.habitoplus.dto;

import br.com.habitoplus.enums.TipoAtividade;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * DTO de entrada para o registro de um hábito saudável.
 * Contém as validações de entrada para evitar dados inválidos.
 */
@Data
public class HabitoRequest {

    @NotBlank(message = "O ID do colaborador é obrigatório")
    private String colaboradorId;

    @NotNull(message = "O tipo de atividade é obrigatório")
    private TipoAtividade tipoAtividade;

    private String descricao;

    @NotNull(message = "A duração é obrigatória")
    @Min(value = 10, message = "A atividade deve ter no mínimo 10 minutos")
    private Integer minutosDuracao;
}
