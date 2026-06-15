-- V2__inserir_dados_iniciais.sql
-- Dados de exemplo para facilitar testes iniciais

INSERT INTO usuario (colaborador_id, nome, email, data_cadastro) VALUES
    ('COL-001', 'Ana Beatriz Silva',   'ana.silva@empresa.com',   NOW()),
    ('COL-002', 'Carlos Eduardo Lima', 'carlos.lima@empresa.com',  NOW()),
    ('COL-003', 'Mariana Souza',       'mariana.souza@empresa.com', NOW());
