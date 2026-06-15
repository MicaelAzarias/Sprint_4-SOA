package br.com.habitoplus.repository;

import br.com.habitoplus.model.RegistroHabito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Repositório JPA para operações de acesso a dados de RegistroHabito.
 */
public interface HabitoRepository extends JpaRepository<RegistroHabito, Long> {

    /** Retorna todos os hábitos de um colaborador específico */
    List<RegistroHabito> findByColaboradorId(String colaboradorId);

    /** Retorna a soma de pontos de um colaborador */
    @Query("SELECT COALESCE(SUM(r.pontosGerados), 0) FROM RegistroHabito r WHERE r.colaboradorId = :colaboradorId")
    int somarPontosPorColaborador(@Param("colaboradorId") String colaboradorId);
}
