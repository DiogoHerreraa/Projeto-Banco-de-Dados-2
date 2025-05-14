-- Script PostgreSQL convertido de MySQL
-- Banco: LOJA_ELETRONICOS

DROP TABLE IF EXISTS item_venda, venda, funcionario_especial, funcionario, cliente_especial, cliente_padrao, produto CASCADE;

-- Criação de Tabelas
CREATE TABLE cliente_padrao (
    id_cpadrao SERIAL PRIMARY KEY,
    nome_cpadrao VARCHAR(50) NOT NULL,
    sexo CHAR(1) CHECK (sexo IN ('M','F','O')) NOT NULL,
    idade INT,
    nascimento DATE
);

CREATE TABLE cliente_especial (
    id_cespecial SERIAL PRIMARY KEY,
    nome_cespecial VARCHAR(50),
    sexo CHAR(1) CHECK (sexo IN ('M','F','O')),
    idade INT,
    cashback NUMERIC(10,2),
    id_cliente INT REFERENCES cliente_padrao(id_cpadrao)
);

CREATE TABLE funcionario (
    id_funcionario SERIAL PRIMARY KEY,
    nome VARCHAR(50) NOT NULL,
    idade INT,
    nascimento DATE,
    sexo CHAR(1) CHECK (sexo IN ('M','F','O')),
    cargo VARCHAR(50) CHECK (cargo IN ('CEO','FINANCEIRO','VENDEDOR','ATENDENTE DE SUPORTE AO CLIENTE','GERENTE DE OPERAÇÕES')),
    salario NUMERIC(10,2)
);

CREATE TABLE venda (
    id_venda SERIAL PRIMARY KEY,
    data_venda DATE,
    id_vendedor INT REFERENCES funcionario(id_funcionario),
    id_cliente INT REFERENCES cliente_padrao(id_cpadrao)
);

CREATE TABLE produto (
    idproduto SERIAL PRIMARY KEY,
    nome_produto VARCHAR(50),
    quantidade_produto INT,
    descricao_produto VARCHAR(100),
    valor_produto NUMERIC(10,2)
);

CREATE TABLE item_venda (
    id_item SERIAL PRIMARY KEY,
    id_venda INT REFERENCES venda(id_venda),
    id_produto INT REFERENCES produto(idproduto),
    nome_produto_vendido VARCHAR(50),
    quantidade INT,
    valor_unitario NUMERIC(10,2),
    valor_total NUMERIC(10,2)
);

CREATE TABLE funcionario_especial (
    id_funcionario INT PRIMARY KEY,
    nome VARCHAR(50),
    bonus_total NUMERIC(10,2)
);



-- INSERTS DE PRODUTOS
INSERT INTO produto (nome_produto, quantidade_produto, descricao_produto, valor_produto) VALUES
('Mouse Gamer', 44, 'Alta performance para jogos', 409.95),
('Teclado Mecânico', 63, 'Ideal para digitação e jogos', 276.99),
('Monitor LED 24"', 32, 'Imagem Full HD nítida', 1643.77),
('Notebook i5', 41, 'Equipamento leve e rápido', 2064.47),
('Smartphone Android', 61, 'Sistema atualizado com boa câmera', 2195.61),
('Carregador USB-C', 57, 'Compatível com diversos modelos', 87.66),
('Caixa de Som Bluetooth', 94, 'Som potente e portátil', 380.96),
('HD Externo 1TB', 15, 'Armazene seus dados com segurança', 316.31),
('SSD 512GB', 37, 'Alta velocidade de leitura', 437.12),
('Fone de Ouvido', 91, 'Confortável e potente', 189.94),
('Webcam Full HD', 22, 'Imagem clara para videochamadas', 272.42),
('Cabo HDMI 2.0', 78, 'Transmissão em alta resolução', 86.11),
('Placa de Vídeo', 40, 'Desempenho gráfico avançado', 2439.73),
('Fonte 600W', 95, 'Energia estável e eficiente', 378.49),
('Memória RAM 8GB', 46, 'Velocidade e estabilidade', 286.55),
('Hub USB 3.0', 73, 'Expanda suas conexões USB', 129.37),
('Tablet 10"', 52, 'Ideal para estudos e vídeos', 781.23),
('Controle Bluetooth', 38, 'Conexão rápida e sem fio', 239.98),
('Microfone Condensador', 17, 'Captação de áudio profissional', 312.84),
('Roteador Wi-Fi 5', 29, 'Sinal forte e estável', 258.64);


DROP VIEW IF EXISTS view_produtos_quantidade;

CREATE OR REPLACE VIEW view_produtos_quantidade AS
SELECT 
    nome_produto AS nome,
    quantidade_produto AS quantidade
FROM produto;