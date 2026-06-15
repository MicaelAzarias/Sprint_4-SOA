package br.com.habitoplus.repository;

import br.com.habitoplus.model.Credencial;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositório JPA para operações de credenciais de autenticação.
 */
public interface CredencialRepository extends JpaRepository<Credencial, Long> {

    Optional<Credencial> findByEmail(String email);
}
