-- V3__adicionar_autenticacao.sql
-- Tabela de credenciais de autenticação dos colaboradores

CREATE TABLE IF NOT EXISTS credencial (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    colaborador_id  VARCHAR(50)  NOT NULL UNIQUE,
    email           VARCHAR(150) NOT NULL UNIQUE,
    senha           VARCHAR(255) NOT NULL,
    role            VARCHAR(30)  NOT NULL DEFAULT 'ROLE_USER',
    CONSTRAINT fk_cred_colaborador FOREIGN KEY (colaborador_id)
        REFERENCES usuario (colaborador_id)
        ON DELETE CASCADE
);

-- Inserir credenciais de exemplo (senhas criptografadas com BCrypt de "senha123")
INSERT INTO credencial (colaborador_id, email, senha, role) VALUES
    ('COL-001', 'ana.silva@empresa.com',    '$2a$10$XutzKe3EBQMxFEjy5./X.ejA8oFcGIIR.1kORrRFaMmvzsMpJbECS', 'ROLE_USER'),
    ('COL-002', 'carlos.lima@empresa.com',  '$2a$10$XutzKe3EBQMxFEjy5./X.ejA8oFcGIIR.1kORrRFaMmvzsMpJbECS', 'ROLE_USER'),
    ('COL-003', 'mariana.souza@empresa.com','$2a$10$XutzKe3EBQMxFEjy5./X.ejA8oFcGIIR.1kORrRFaMmvzsMpJbECS', 'ROLE_ADMIN');
