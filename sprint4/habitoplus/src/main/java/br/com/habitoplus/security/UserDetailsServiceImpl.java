package br.com.habitoplus.security;

import br.com.habitoplus.model.Credencial;
import br.com.habitoplus.repository.CredencialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação de UserDetailsService que carrega as credenciais do banco de dados.
 * Integrada ao mecanismo de autenticação do Spring Security.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CredencialRepository credencialRepository;

    /**
     * Carrega o usuário pelo e-mail para autenticação.
     *
     * @param email e-mail do colaborador
     * @return UserDetails com credenciais e roles
     */
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Credencial credencial = credencialRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Colaborador com e-mail '" + email + "' não encontrado."));

        return new User(
                credencial.getEmail(),
                credencial.getSenha(),
                List.of(new SimpleGrantedAuthority(credencial.getRole()))
        );
    }
}
