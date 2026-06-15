package br.com.habitoplus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Classe principal da aplicação HábitoPlus — Sprint 4.
 * Ponto de entrada do Spring Boot com segurança JWT e Swagger habilitados.
 */
@SpringBootApplication
public class HabitoplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(HabitoplusApplication.class, args);
    }
}
