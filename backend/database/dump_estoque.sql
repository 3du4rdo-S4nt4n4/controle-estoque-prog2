-- ============================================================
-- Sistema de Controle de Estoque - Varejo Tradicional
-- Banco de Dados: PostgreSQL
-- Disciplina: Programação II - UEMG Unidade Passos
-- ============================================================

-- Criação do banco (executar separadamente, fora de uma transação, se necessário)
-- CREATE DATABASE estoque_db;
-- \c estoque_db

-- ============================================================
-- TABELA: categoria
-- ============================================================
DROP TABLE IF EXISTS movimentacao_estoque CASCADE;
DROP TABLE IF EXISTS produto CASCADE;
DROP TABLE IF EXISTS fornecedor CASCADE;
DROP TABLE IF EXISTS categoria CASCADE;

CREATE TABLE categoria (
    id          SERIAL PRIMARY KEY,
    nome        VARCHAR(100) NOT NULL UNIQUE,
    descricao   VARCHAR(255)
);

-- ============================================================
-- TABELA: fornecedor
-- ============================================================
CREATE TABLE fornecedor (
    id          SERIAL PRIMARY KEY,
    nome        VARCHAR(150) NOT NULL,
    cnpj        VARCHAR(18) UNIQUE,
    telefone    VARCHAR(20),
    email       VARCHAR(150),
    endereco    VARCHAR(255)
);

-- ============================================================
-- TABELA: produto
-- ============================================================
CREATE TABLE produto (
    id                  SERIAL PRIMARY KEY,
    nome                VARCHAR(150) NOT NULL,
    descricao           VARCHAR(255),
    codigo_barras       VARCHAR(50) UNIQUE,
    categoria_id        INTEGER REFERENCES categoria(id),
    fornecedor_id       INTEGER REFERENCES fornecedor(id),
    preco_custo         NUMERIC(10,2) NOT NULL DEFAULT 0,
    preco_venda         NUMERIC(10,2) NOT NULL DEFAULT 0,
    quantidade_estoque  INTEGER NOT NULL DEFAULT 0,
    quantidade_minima   INTEGER NOT NULL DEFAULT 0,
    unidade_medida      VARCHAR(10) NOT NULL DEFAULT 'UN',
    data_cadastro       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ativo               BOOLEAN NOT NULL DEFAULT TRUE
);

-- ============================================================
-- TABELA: movimentacao_estoque
-- Registra toda entrada/saída, dando rastreabilidade ao estoque
-- ============================================================
CREATE TABLE movimentacao_estoque (
    id                   SERIAL PRIMARY KEY,
    produto_id           INTEGER NOT NULL REFERENCES produto(id),
    tipo                 VARCHAR(10) NOT NULL CHECK (tipo IN ('ENTRADA', 'SAIDA')),
    quantidade           INTEGER NOT NULL CHECK (quantidade > 0),
    motivo               VARCHAR(255),
    data_movimentacao    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- ÍNDICES úteis para consultas frequentes
-- ============================================================
CREATE INDEX idx_produto_categoria ON produto(categoria_id);
CREATE INDEX idx_produto_fornecedor ON produto(fornecedor_id);
CREATE INDEX idx_movimentacao_produto ON movimentacao_estoque(produto_id);

-- ============================================================
-- POPULAÇÃO INICIAL (dados de exemplo para testes/demonstração)
-- ============================================================

INSERT INTO categoria (nome, descricao) VALUES
('Alimentos', 'Produtos alimentícios em geral'),
('Limpeza', 'Produtos de limpeza doméstica'),
('Bebidas', 'Bebidas diversas'),
('Higiene Pessoal', 'Produtos de cuidado pessoal');

INSERT INTO fornecedor (nome, cnpj, telefone, email, endereco) VALUES
('Distribuidora Passos Ltda', '12.345.678/0001-90', '(35) 3521-1234', 'contato@distribuidorapassos.com.br', 'Av. Brasil, 500 - Passos/MG'),
('Atacadão Sul de Minas', '98.765.432/0001-10', '(35) 3522-5678', 'vendas@atacadaosulmg.com.br', 'Rua das Indústrias, 120 - Passos/MG');

INSERT INTO produto (nome, descricao, codigo_barras, categoria_id, fornecedor_id, preco_custo, preco_venda, quantidade_estoque, quantidade_minima, unidade_medida) VALUES
('Arroz Branco 5kg', 'Arroz tipo 1, pacote de 5kg', '7891000100103', 1, 1, 18.50, 24.90, 40, 10, 'UN'),
('Detergente Neutro 500ml', 'Detergente líquido para louças', '7891000200104', 2, 1, 1.80, 3.50, 60, 15, 'UN'),
('Refrigerante Cola 2L', 'Refrigerante sabor cola, garrafa 2L', '7891000300105', 3, 2, 5.20, 8.90, 30, 12, 'UN'),
('Sabonete em Barra 90g', 'Sabonete neutro, unidade', '7891000400106', 4, 2, 0.90, 1.99, 100, 20, 'UN');

INSERT INTO movimentacao_estoque (produto_id, tipo, quantidade, motivo) VALUES
(1, 'ENTRADA', 40, 'Compra inicial de estoque'),
(2, 'ENTRADA', 60, 'Compra inicial de estoque'),
(3, 'ENTRADA', 30, 'Compra inicial de estoque'),
(4, 'ENTRADA', 100, 'Compra inicial de estoque'),
(1, 'SAIDA', 5, 'Venda balcão');
