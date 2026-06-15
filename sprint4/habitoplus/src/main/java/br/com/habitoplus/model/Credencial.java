package br.com.habitoplus.model;

import jakarta.persistence.*;
import lombok.Data;

/**
 * Entidade que representa as credenciais de autenticação de um colaborador.
 * Armazena email, senha (BCrypt) e role para controle de acesso.
 */
@Entity
@Data
@Table(name = "credencial")
public class Credencial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "colaborador_id", nullable = false, unique = true, length = 50)
    private String colaboradorId;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    /** Senha criptografada com BCryptPasswordEncoder */
    @Column(nullable = false, length = 255)
    private String senha;

    /** Role do colaborador: ROLE_USER ou ROLE_ADMIN */
    @Column(nullable = false, length = 30)
    private String role;
}
