package br.com.habitoplus.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade que representa um colaborador cadastrado no sistema HábitoPlus.
 */
@Entity
@Data
@Table(name = "usuario")
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identificador único do colaborador (ex: COL-001) */
    @Column(name = "colaborador_id", nullable = false, unique = true, length = 50)
    private String colaboradorId;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 150)
    private String email;

    @Column(name = "data_cadastro", nullable = false)
    private LocalDateTime dataCadastro;

    /** Relacionamento: um colaborador pode ter vários registros de hábito */
    @OneToMany(mappedBy = "colaboradorId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RegistroHabito> habitos;
}
