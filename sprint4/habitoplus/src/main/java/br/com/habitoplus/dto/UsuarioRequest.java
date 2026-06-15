package br.com.habitoplus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * DTO de entrada para cadastro de colaboradores.
 */
@Data
public class UsuarioRequest {

    @NotBlank(message = "O ID do colaborador é obrigatório")
    private String colaboradorId;

    @NotBlank(message = "O nome é obrigatório")
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    private String email;
}
