# Sprint_4-SOA

# 🏃‍♂️ HábitoPlus — Sprint 4: Segurança, Swagger e Testes

**Integrantes:**
- Micael Santos Azarias | RM552699
- Felipe Megumi Nakama | RM552821
- Carolina Cavalli Machado | RM552925
- Nathan da Silveira Uflacker | RM553264

---

## 📋 Descrição do Projeto

O **HábitoPlus** é o backend de um módulo de **Saúde e Bem-estar Corporativo**, que incentiva e recompensa hábitos saudáveis com um sistema de pontuação (**Milhas de Saúde**).

Este sprint adiciona sobre a base do Sprint 3:
- **Autenticação e autorização segura** com JWT e Spring Security
- **Senhas criptografadas** com BCryptPasswordEncoder
- **Documentação automática** da API com SpringDoc + Swagger/OpenAPI
- **Testes automatizados** unitários e de integração
- **Controle de acesso por roles** (USER / ADMIN)

---

## 🚀 Tecnologias Utilizadas

| Tecnologia | Versão | Finalidade |
|---|---|---|
| Java | 21 (LTS) | Linguagem principal |
| Spring Boot | 3.2.3 | Framework web |
| Spring Security | 3.2.3 | Autenticação e autorização |
| JJWT | 0.12.3 | Geração e validação de tokens JWT |
| BCryptPasswordEncoder | — | Criptografia de senhas |
| SpringDoc OpenAPI | 2.3.0 | Documentação automática Swagger |
| Spring Data JPA | 3.2.3 | Persistência e ORM |
| Flyway | — | Migrações de banco de dados |
| MySQL | 8+ | Banco de dados relacional |
| H2 (in-memory) | — | Banco de dados para testes |
| JUnit 5 + Mockito | — | Testes unitários e de integração |
| MockMvc | — | Testes de endpoints HTTP |
| Lombok | — | Redução de boilerplate |
| Maven | — | Gerenciamento de dependências |

---

## 📂 Estrutura do Projeto

```
src/main/java/br/com/habitoplus/
├── config/
│   ├── SecurityConfig.java         # Configuração Spring Security (stateless JWT)
│   └── OpenApiConfig.java          # Configuração Swagger/OpenAPI com Bearer JWT
├── controller/
│   ├── AuthController.java         # POST /auth/login → retorna token JWT
│   └── HabitoController.java       # CRUD de hábitos (com anotações Swagger)
├── dto/
│   ├── HabitoRequest.java          # DTO de entrada com validações
│   ├── HabitoResponse.java         # VO de saída
│   ├── ErroResponse.java           # VO padronizado de erros
│   ├── LoginRequest.java           # DTO de login
│   └── TokenResponse.java          # VO com token JWT
├── enums/
│   └── TipoAtividade.java          # Enum com multiplicadores de pontos
├── exception/
│   ├── GlobalExceptionHandler.java # @RestControllerAdvice global
│   └── RecursoNaoEncontradoException.java
├── model/
│   ├── Credencial.java             # Entidade de autenticação
│   ├── RegistroHabito.java         # Entidade principal
│   └── Usuario.java                # Entidade de colaborador
├── repository/
│   ├── CredencialRepository.java
│   ├── HabitoRepository.java
│   └── UsuarioRepository.java
├── security/
│   ├── JwtAuthFilter.java          # Filtro JWT (OncePerRequestFilter)
│   ├── JwtService.java             # Geração e validação de tokens
│   └── UserDetailsServiceImpl.java # Carrega usuário do banco
└── service/
    ├── AuthService.java            # Lógica de autenticação
    └── HabitoService.java          # Regras de negócio e pontuação

src/test/java/br/com/habitoplus/
├── controller/
│   └── HabitoControllerIntegrationTest.java  # Testes de integração (MockMvc + H2)
└── service/
    └── HabitoServiceTest.java                # Testes unitários (Mockito)
```

---

## 🗄️ Configuração e Execução

### 1. Pré-requisitos
- Java 21, MySQL 8+, Maven

### 2. Criar banco de dados
```sql
CREATE DATABASE habitoplus;
```

### 3. Configurar credenciais
Em `src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=SUA_SENHA_AQUI
jwt.secret=habitoplus-super-secret-key-2025-soa-sprint4-fiap
```

### 4. Executar a aplicação
```bash
./mvnw spring-boot:run
```
Acesse: `http://localhost:8080`

O Flyway executará as migrações V1, V2 e V3 automaticamente.

---

## 🔐 Autenticação JWT

### Fluxo de autenticação

```
1. POST /api/v1/auth/login  →  recebe { email, senha }
2. Servidor valida credenciais (BCrypt) e retorna token JWT
3. Cliente inclui token em todas as requisições: Authorization: Bearer <token>
4. JwtAuthFilter intercepta, valida e autentica no SecurityContext
```

### Credenciais de exemplo (inseridas pela migração V3)

| Colaborador | E-mail | Senha | Role |
|---|---|---|---|
| COL-001 | ana.silva@empresa.com | senha123 | ROLE_USER |
| COL-002 | carlos.lima@empresa.com | senha123 | ROLE_USER |
| COL-003 | mariana.souza@empresa.com | senha123 | ROLE_ADMIN |

### Exemplo de login
```bash
curl -X POST http://localhost:8080/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email": "ana.silva@empresa.com", "senha": "senha123"}'
```
Resposta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "colaboradorId": "COL-001",
  "email": "ana.silva@empresa.com",
  "role": "ROLE_USER"
}
```

### Exemplo de uso com token
```bash
curl http://localhost:8080/api/v1/habitos \
  -H "Authorization: Bearer eyJhbGciOiJIUzI1NiJ9..."
```

### Controle de acesso por roles

| Endpoint | ROLE_USER | ROLE_ADMIN |
|---|---|---|
| POST /habitos | ✅ | ✅ |
| GET /habitos | ✅ | ✅ |
| PUT /habitos/{id} | ✅ | ✅ |
| DELETE /habitos/{id} | ❌ | ✅ |

---

## 📖 Documentação Swagger

Após iniciar a aplicação, acesse:

**`http://localhost:8080/swagger-ui.html`**

O Swagger UI permite:
- Visualizar todos os endpoints com descrições, exemplos e schemas
- Autenticar com token JWT clicando no botão **"Authorize"** e inserindo o token
- Testar as requisições diretamente pela interface

---

## 🧪 Como Rodar os Testes

```bash
# Todos os testes (unitários + integração)
./mvnw test

# Somente testes unitários
./mvnw test -Dtest=HabitoServiceTest

# Somente testes de integração
./mvnw test -Dtest=HabitoControllerIntegrationTest
```

Os testes de integração usam **H2 em memória** (perfil `test`) — não é necessário MySQL para rodá-los.

### Cobertura dos testes

**Testes Unitários (`HabitoServiceTest`):**
- ✅ Registro de hábito com cálculo de pontos correto
- ✅ Exceção ao registrar para colaborador inexistente
- ✅ Cálculo de pontos por tipo de atividade
- ✅ Busca por ID existente e inexistente
- ✅ Listagem de hábitos
- ✅ Deleção com sucesso e com ID inexistente

**Testes de Integração (`HabitoControllerIntegrationTest`):**
- ✅ POST cria hábito e retorna 201
- ✅ POST retorna 400 com dados inválidos
- ✅ POST retorna 401 sem token JWT
- ✅ GET lista todos os hábitos
- ✅ GET busca por ID existente e retorna 404 para inexistente
- ✅ DELETE como ADMIN retorna 204
- ✅ DELETE como USER retorna 403

---

## 🏗️ Diagrama de Arquitetura

```
Cliente (Postman / Swagger UI)
        │
        ▼
 [ JwtAuthFilter ]     ← Intercepta TODAS as requisições, valida Bearer token
        │
        ▼
  [ Controller ]        ← Recebe requisição autenticada, valida DTOs
        │
        ▼
  [   Service  ]        ← Regras de negócio, cálculo de pontos
        │
        ▼
  [ Repository ]        ← Acesso ao banco via JPA
        │
        ▼
  [   MySQL    ]        ← Persistência gerenciada pelo Flyway
```

## 🗃️ Diagrama de Entidades (ER)

```
┌──────────────────────┐       ┌──────────────────────────────┐
│       USUARIO         │       │       REGISTRO_HABITO         │
├──────────────────────┤       ├──────────────────────────────┤
│ id (PK)              │       │ id (PK)                      │
│ colaborador_id (UNQ) │◄──────│ colaborador_id (FK)          │
│ nome                 │       │ tipo_atividade (ENUM)        │
│ email (UNQ)          │       │ descricao                    │
│ data_cadastro        │       │ minutos_duracao              │
└──────────────────────┘       │ pontos_gerados               │
         ▲                     │ data_registro                │
         │                     └──────────────────────────────┘
┌──────────────────────┐
│      CREDENCIAL       │
├──────────────────────┤
│ id (PK)              │
│ colaborador_id (FK)  │
│ email (UNQ)          │
│ senha (BCrypt)       │
│ role                 │
└──────────────────────┘
```
