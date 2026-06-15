package br.com.habitoplus.model;

import br.com.habitoplus.enums.TipoAtividade;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entidade que representa um registro de atividade saudável de um colaborador.
 * Cada registro gera pontos ("Milhas de Saúde") com base no tipo e duração da atividade.
 */
@Entity
@Data
@Table(name = "registro_habito")
public class RegistroHabito {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** ID do colaborador que realizou a atividade */
    @Column(name = "colaborador_id", nullable = false, length = 50)
    private String colaboradorId;

    /** Tipo da atividade realizada */
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_atividade", nullable = false, length = 50)
    private TipoAtividade tipoAtividade;

    /** Descrição opcional da atividade */
    @Column(length = 255)
    private String descricao;

    /** Duração da atividade em minutos */
    @Column(name = "minutos_duracao", nullable = false)
    private Integer minutosDuracao;

    /** Pontos calculados com base no tipo e duração */
    @Column(name = "pontos_gerados", nullable = false)
    private Integer pontosGerados;

    /** Momento do registro */
    @Column(name = "data_registro", nullable = false)
    private LocalDateTime dataRegistro;
}
