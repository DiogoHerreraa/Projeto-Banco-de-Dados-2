DROP TABLE    IF EXISTS item_venda, venda, funcionario_especial,
                       funcionario, cliente_especial, cliente, produto CASCADE;
DROP VIEW     IF EXISTS view_produtos_quantidade,
                       view_vendas_funcionario              CASCADE;
DROP FUNCTION IF EXISTS registrar_venda(DATE, INT, TEXT, INT, TEXT, INT,
                                        TEXT, INT, NUMERIC);
DROP FUNCTION IF EXISTS reajustar_salario(TEXT, NUMERIC);
DROP FUNCTION IF EXISTS realizar_sorteio();
DROP FUNCTION IF EXISTS calcular_estatisticas();


CREATE TABLE cliente (
    id          SERIAL PRIMARY KEY,
    nome        VARCHAR(50) NOT NULL,
    sexo        CHAR(1)     CHECK (sexo IN ('M','F','O')) NOT NULL,
    idade       INT,
    nascimento  DATE
);

CREATE TABLE cliente_especial (
    id              SERIAL PRIMARY KEY,
    nome            VARCHAR(50),
    sexo            CHAR(1) CHECK (sexo IN ('M','F','O')),
    idade           INT,
    cashback        NUMERIC(10,2),
    id_cliente      INT REFERENCES cliente(id)
);

CREATE TABLE funcionario (
    id          SERIAL PRIMARY KEY,
    nome        VARCHAR(50) NOT NULL,
    idade       INT,
    sexo        CHAR(1) CHECK (sexo IN ('M','F','O')),
    cargo       VARCHAR(50) CHECK (cargo IN ('CEO','FINANCEIRO','VENDEDOR',
                                             'ATENDENTE DE SUPORTE AO CLIENTE',
                                             'GERENTE DE OPERAÇÕES')),
    salario     NUMERIC(10,2),
    nascimento  DATE
);

CREATE TABLE venda (
    id              SERIAL PRIMARY KEY,
    data_venda      DATE,
    id_vendedor     INT REFERENCES funcionario(id),
    nome_funcionario VARCHAR(50),
    id_cliente      INT REFERENCES cliente(id),
    nome_cliente    VARCHAR(50)
);

CREATE TABLE produto (
    id          SERIAL PRIMARY KEY,
    nome        VARCHAR(50),
    quantidade  INT,
    descricao   VARCHAR(100),
    valor       NUMERIC(10,2)
);

CREATE TABLE item_venda (
    id                  SERIAL PRIMARY KEY,
    id_venda            INT REFERENCES venda(id),
    id_produto          INT REFERENCES produto(id),
    nome_produto_vendido VARCHAR(50),
    quantidade          INT,
    valor_unitario      NUMERIC(10,2),
    valor_total         NUMERIC(10,2)
);

CREATE TABLE funcionario_especial (
    id_funcionario  INT PRIMARY KEY REFERENCES funcionario(id),
    nome            VARCHAR(50),
    bonus_total     NUMERIC(10,2)
);

INSERT INTO produto (nome, quantidade, descricao, valor) VALUES
('Mouse Gamer', 44, 'Alta performance para jogos', 409.95),
('Teclado Mecânico', 63, 'Ideal para digitação e jogos', 276.99),
('Monitor LED 24"', 32, 'Imagem Full HD nítida', 1643.77),
('Notebook i5', 41, 'Equipamento leve e rápido', 2064.47),
('Smartphone Android', 61, 'Sistema atualizado com boa câmera', 2195.61),
('Carregador USB-C', 57, 'Compatível com diversos modelos', 87.66),
('Caixa de Som Bluetooth', 94, 'Som potente e portátil', 380.96),
('HD Externo 1 TB', 15, 'Armazene seus dados com segurança', 316.31),
('SSD 512 GB', 37, 'Alta velocidade de leitura', 437.12),
('Fone de Ouvido', 91, 'Confortável e potente', 189.94),
('Webcam Full HD', 22, 'Imagem clara para videochamadas', 272.42),
('Cabo HDMI 2.0', 78, 'Transmissão em alta resolução', 86.11),
('Placa de Vídeo', 40, 'Desempenho gráfico avançado', 2439.73),
('Fonte 600 W', 95, 'Energia estável e eficiente', 378.49),
('Memória RAM 8 GB', 46, 'Velocidade e estabilidade', 286.55),
('Hub USB 3.0', 73, 'Expanda suas conexões USB', 129.37),
('Tablet 10"', 52, 'Ideal para estudos e vídeos', 781.23),
('Controle Bluetooth', 38, 'Conexão rápida e sem fio', 239.98),
('Microfone Condensador', 17, 'Captação de áudio profissional', 312.84),
('Roteador Wi-Fi 5', 29, 'Sinal forte e estável', 258.64);

CREATE OR REPLACE VIEW view_produtos_quantidade AS
SELECT nome, quantidade
FROM   produto;

CREATE OR REPLACE VIEW view_vendas_funcionario AS
SELECT f.nome                        AS vendedor,
       COUNT(v.id)                   AS total_vendas
FROM   venda v
JOIN   funcionario f ON f.id = v.id_vendedor
GROUP  BY f.nome
ORDER  BY total_vendas DESC;

CREATE OR REPLACE FUNCTION registrar_venda(
    p_data            DATE,
    p_id_vendedor     INT,
    p_nome_vendedor   TEXT,
    p_id_cliente      INT,
    p_nome_cliente    TEXT,
    p_id_produto      INT,
    p_nome_produto    TEXT,
    p_quantidade      INT,
    p_valor_unitario  NUMERIC
) RETURNS VOID
LANGUAGE plpgsql AS
$$
DECLARE
    v_id_venda INT;
BEGIN
    INSERT INTO venda (data_venda,
                       id_vendedor,  nome_funcionario,
                       id_cliente,   nome_cliente)
    VALUES (p_data,
            p_id_vendedor, p_nome_vendedor,
            p_id_cliente,  p_nome_cliente)
    RETURNING id INTO v_id_venda;

    INSERT INTO item_venda (id_venda, id_produto,
                            nome_produto_vendido, quantidade,
                            valor_unitario, valor_total)
    VALUES (v_id_venda, p_id_produto, p_nome_produto,
            p_quantidade, p_valor_unitario,
            p_valor_unitario * p_quantidade);

    UPDATE produto
       SET quantidade = quantidade - p_quantidade
     WHERE id = p_id_produto;
END;
$$;

CREATE OR REPLACE FUNCTION reajustar_salario(
    p_cargo      TEXT,
    p_percentual NUMERIC
) RETURNS VOID
LANGUAGE plpgsql AS
$$
BEGIN
    UPDATE funcionario
       SET salario = salario * (1 + p_percentual/100.0)
     WHERE UPPER(cargo) = UPPER(p_cargo);
END;
$$;

CREATE OR REPLACE FUNCTION realizar_sorteio() RETURNS VOID
LANGUAGE plpgsql AS
$$
DECLARE
    v_cli RECORD;
BEGIN
    SELECT *
      INTO v_cli
      FROM cliente c
     WHERE NOT EXISTS (SELECT 1
                         FROM cliente_especial ce
                        WHERE ce.id_cliente = c.id)
  ORDER BY random()
     LIMIT 1;

    IF NOT FOUND THEN
        RAISE NOTICE 'Todos os clientes já são especiais.';
        RETURN;
    END IF;

    INSERT INTO cliente_especial (nome, sexo, idade,
                                  cashback, id_cliente)
    VALUES (v_cli.nome,
            v_cli.sexo,
            v_cli.idade,
            COALESCE(
              0.05 * (SELECT SUM(iv.valor_total)
                         FROM venda v
                         JOIN item_venda iv ON iv.id_venda = v.id
                        WHERE v.id_cliente = v_cli.id), 0),
            v_cli.id);
END;
$$;

CREATE OR REPLACE FUNCTION calcular_estatisticas()
RETURNS TABLE (
    produto_mais_vendido  TEXT,
    vendedor_mais_vendido TEXT,
    produto_menos_vendido TEXT,
    valor_mais_vendido    NUMERIC,
    valor_menos_vendido   NUMERIC
) LANGUAGE plpgsql AS
$$
BEGIN
    RETURN QUERY
    WITH resumo_prod AS (
        SELECT iv.nome_produto_vendido AS produto,
               SUM(iv.quantidade)      AS qtd,
               SUM(iv.valor_total)     AS valor
          FROM item_venda iv
      GROUP BY iv.nome_produto_vendido
    ),
    rank_prod AS (
        SELECT produto, qtd, valor,
               RANK() OVER (ORDER BY qtd DESC) AS rk_desc,
               RANK() OVER (ORDER BY qtd ASC)  AS rk_asc
          FROM resumo_prod
    )
    SELECT
        (SELECT produto     FROM rank_prod WHERE rk_desc = 1),
        (SELECT f.nome
           FROM venda v
           JOIN funcionario f ON f.id = v.id_vendedor
           JOIN item_venda iv ON iv.id_venda = v.id
       GROUP BY f.nome
       ORDER BY SUM(iv.quantidade) DESC
          LIMIT 1),
        (SELECT produto     FROM rank_prod WHERE rk_asc  = 1),
        (SELECT valor       FROM rank_prod WHERE rk_desc = 1),
        (SELECT valor       FROM rank_prod WHERE rk_asc  = 1);
END;
$$;

