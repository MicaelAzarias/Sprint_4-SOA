package br.com.habitoplus.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

/**
 * Configuração da documentação automática via SpringDoc + Swagger/OpenAPI.
 * Disponível em: http://localhost:8080/swagger-ui.html
 *
 * Define o esquema de segurança Bearer JWT para que os endpoints
 * protegidos possam ser testados diretamente pelo Swagger UI.
 */
@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "HábitoPlus API",
                version = "4.0",
                description = "API REST para registro e gestão de hábitos saudáveis corporativos. " +
                              "Utiliza autenticação JWT (Bearer Token). " +
                              "Faça login em /api/v1/auth/login para obter o token.",
                contact = @Contact(
                        name = "Equipe HábitoPlus - FIAP",
                        email = "habitoplus@empresa.com"
                )
        ),
        servers = @Server(url = "http://localhost:8080", description = "Servidor local"),
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        description = "Token JWT obtido via POST /api/v1/auth/login. " +
                      "Insira apenas o token, sem o prefixo 'Bearer '.",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}
