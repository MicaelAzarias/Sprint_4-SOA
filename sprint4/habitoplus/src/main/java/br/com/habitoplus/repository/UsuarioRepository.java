package br.com.habitoplus.repository;

import br.com.habitoplus.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório JPA para operações de acesso a dados de Usuario.
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByColaboradorId(String colaboradorId);

    boolean existsByColaboradorId(String colaboradorId);
}
