-- V1__criar_tabelas_iniciais.sql
-- Migração inicial: criação das tabelas do sistema HábitoPlus

CREATE TABLE IF NOT EXISTS usuario (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    colaborador_id VARCHAR(50)  NOT NULL UNIQUE,
    nome        VARCHAR(150) NOT NULL,
    email       VARCHAR(150) NOT NULL UNIQUE,
    data_cadastro DATETIME    NOT NULL
);

CREATE TABLE IF NOT EXISTS registro_habito (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    colaborador_id  VARCHAR(50)  NOT NULL,
    tipo_atividade  VARCHAR(50)  NOT NULL,
    descricao       VARCHAR(255),
    minutos_duracao INT          NOT NULL,
    pontos_gerados  INT          NOT NULL,
    data_registro   DATETIME     NOT NULL,
    CONSTRAINT fk_habito_usuario FOREIGN KEY (colaborador_id)
        REFERENCES usuario (colaborador_id)
        ON DELETE CASCADE
);
