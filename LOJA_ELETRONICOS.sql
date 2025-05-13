-- Create the database (already created as per your input, but included for completeness)
CREATE DATABASE LOJA_ELETRONICOS;
\c LOJA_ELETRONICOS;

-- Create a custom type for Sexo (replacing ENUM)
CREATE TYPE sexo_type AS ENUM ('M', 'F', 'O');

-- Create a custom type for Cargo (replacing ENUM)
CREATE TYPE cargo_type AS ENUM ('VENDEDOR', 'GERENTE', 'CEO', 'FINANCEIRO', 'ATENDENTE DE SUPORTE AO CLIENTE', 'GERENTE DE OPERACOES');

-- Create Tables
CREATE TABLE cliente (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    sexo sexo_type NOT NULL,
    idade INT,
    nascimento DATE
);

CREATE TABLE cliente_especial (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    sexo sexo_type,
    idade INT,
    id_cliente INT,
    cashback DECIMAL(10,2),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);

CREATE TABLE funcionario (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    idade INT,
    sexo sexo_type NOT NULL,
    cargo cargo_type NOT NULL,
    salario DECIMAL(10,2),
    nascimento DATE
);

CREATE TABLE funcionario_especial (
    id_funcionario INT PRIMARY KEY,
    nome VARCHAR(100),
    bonus_total DECIMAL(10,2),
    FOREIGN KEY (id_funcionario) REFERENCES funcionario(id)
);

CREATE TABLE produto (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100),
    quantidade INT,
    descricao VARCHAR(255),
    valor DECIMAL(10,2)
);

CREATE TABLE venda (
    id SERIAL PRIMARY KEY,
    id_vendedor INT,
    nome_funcionario VARCHAR(100),
    id_cliente INT,
    nome_cliente VARCHAR(100),
    data_venda DATE,
    FOREIGN KEY (id_vendedor) REFERENCES funcionario(id),
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);

CREATE TABLE item_venda (
    id SERIAL PRIMARY KEY,
    id_venda INT,
    id_produto INT,
    nome_produto_vendido VARCHAR(100),
    quantidade INT,
    valor_unitario DECIMAL(10,2),
    valor_total DECIMAL(10,2),
    FOREIGN KEY (id_venda) REFERENCES venda(id),
    FOREIGN KEY (id_produto) REFERENCES produto(id)
);

-- Insert 100 Clients (as per your teammate's data, abbreviated for brevity)
INSERT INTO cliente (nome, sexo, idade, nascimento) VALUES
('ANTONIO CASTELÃO', 'M', 24, '2001-05-10'),
('DIOGO', 'M', 21, '2003-08-14'),
('ANDERSON NEIFF', 'M', 21, '2003-05-12'),
('FELIPE MORAES', 'M', 21, '2003-01-10'),
('FERNANDA', 'F', 26, '1976-12-24'),
('LARISSA', 'F', 43, '1981-03-08'),
('FELIPE', 'M', 27, '1954-08-05'),
('GABRIELA', 'F', 27, '1959-06-09'),
('RAFAEL', 'M', 53, '1996-06-15'),
('NEDSON', 'M', 21, '2003-08-23'),
('RODRIGO', 'M', 33, '2005-04-06'),
('JOANA', 'F', 76, '1952-09-17'),
('BRUNO', 'O', 74, '1970-07-24'),
('DANIELA', 'F', 56, '1990-04-28'),
('FELIPE', 'M', 55, '1966-08-16'),
('PRISCILA', 'F', 79, '1950-01-04'),
('DIEGO', 'M', 30, '1971-06-20'),
('BEATRIZ', 'F', 45, '1961-01-09'),
('DIEGO', 'M', 34, '1988-08-27'),
('KARLA', 'F', 68, '1956-06-26'),
('VINICIUS', 'M', 42, '1991-06-23'),
('JOANA', 'F', 60, '1966-10-01'),
('FERNANDO', 'M', 41, '1983-03-03'),
('NATHALIA', 'F', 31, '1976-07-01'),
('MAURICIO', 'M', 50, '1991-11-27'),
('MARCIA', 'F', 58, '1957-09-22'),
('ANDRÉ', 'M', 77, '1971-07-09'),
('SABRINA', 'F', 65, '1976-09-02'),
('FELIPE', 'M', 32, '1975-04-30'),
('YARA', 'F', 28, '1972-02-14'),
('LEONARDO', 'M', 53, '1962-12-29'),
('WILLIAM', 'M', 33, '1998-11-03'),
('XIMENA', 'F', 55, '1999-05-10'),
('PAULA', 'F', 46, '1971-04-17'),
('MATEUS', 'M', 79, '1985-09-15'),
('CAMILA', 'F', 45, '1991-10-05'),
('LUIZ', 'O', 64, '1973-05-25'),
('ANA', 'F', 31, '1963-02-04'),
('DANIEL', 'M', 75, '1972-06-04'),
('SANDRA', 'F', 61, '1957-07-20'),
('CAIO', 'M', 54, '1970-03-19'),
('BRUNA', 'F', 76, '1960-08-21'),
('ANTONIO', 'M', 64, '1966-02-17'),
('JAQUELINE', 'F', 37, '1973-08-12'),
('JORGE', 'M', 56, '1995-10-09'),
('CARLA', 'F', 71, '1990-05-05'),
('GUSTAVO', 'M', 23, '1980-01-06'),
('HELENA', 'F', 21, '1993-03-29'),
('RAIMUNDO', 'M', 50, '1959-12-02'),
('DAVI', 'O', 27, '2003-07-17'),
('ALINE', 'F', 43, '1997-08-04'),
('ANDRÉ', 'M', 38, '1964-09-19'),
('DANIEL', 'M', 52, '1965-11-28'),
('MIRELA', 'F', 42, '1992-04-10'),
('ERNESTO', 'O', 74, '1984-08-31'),
('ANA', 'F', 46, '1983-10-16'),
('GUSTAVO', 'M', 79, '1976-10-30'),
('YARA', 'F', 28, '1961-11-21'),
('FÁBIO', 'M', 22, '1990-06-13'),
('GABRIELA', 'F', 36, '1995-12-01'),
('OSVALDO', 'M', 38, '1987-10-27'),
('VIVIANE', 'F', 36, '1988-09-04'),
('PAULO', 'M', 26, '1972-03-25'),
('XIMENA', 'F', 21, '1980-12-30'),
('WILLIAM', 'O', 32, '1953-01-05'),
('MARCIA', 'F', 65, '1998-01-24'),
('VINICIUS', 'M', 45, '1994-04-14'),
('PATRICIA', 'F', 24, '1997-03-16'),
('TIAGO', 'M', 70, '1972-12-07'),
('RAQUEL', 'F', 49, '1961-08-08'),
('MURILO', 'M', 40, '1985-09-06'),
('JOANA', 'O', 68, '1979-04-22'),
('HENRIQUE', 'M', 28, '1980-03-10'),
('HELENA', 'F', 43, '1991-07-29'),
('LUIZ', 'M', 62, '1990-02-12'),
('NATHALIA', 'F', 61, '1999-10-01'),
('RAFAEL', 'M', 37, '1957-06-06'),
('TATIANA', 'F', 34, '2003-09-09'),
('RODRIGO', 'M', 76, '1964-01-01'),
('ZILDA', 'F', 53, '1989-06-21'),
('LEONARDO', 'M', 32, '1969-04-06'),
('LARISSA', 'F', 34, '1996-05-03'),
('JULIO', 'M', 61, '1970-01-11'),
('GISELE', 'F', 52, '1962-02-01'),
('JORGE', 'M', 57, '1979-05-20'),
('BEATRIZ', 'F', 23, '1985-10-17'),
('IGOR', 'M', 65, '1986-05-13'),
('CAMILA', 'F', 45, '1977-03-02'),
('ANTONIO', 'M', 31, '1998-07-30'),
('EDNA', 'O', 22, '1960-08-26'),
('CAIO', 'M', 64, '1977-02-17'),
('LETICIA', 'F', 69, '1957-11-15'),
('RAIMUNDO', 'M', 65, '1952-06-18'),
('URSULA', 'F', 24, '2007-08-11'),
('VANIA', 'F', 51, '1971-07-26'),
('PATRICIA', 'F', 62, '1985-03-16'),
('BRUNO', 'M', 69, '1980-05-28'),
('ROBERTO', 'M', 64, '1955-01-21'),
('ALFREDO JOSUE', 'M', 23, '2002-03-04'),
('LUCAS MORAES', 'O', 25, '2000-11-24');

-- Insert 20 Products (as per your teammate's data)
INSERT INTO produto (nome, quantidade, descricao, valor) VALUES
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

-- Insert 5 Employees (as per your teammate's data, adjusted for cargo_type)
INSERT INTO funcionario (nome, idade, sexo, cargo, salario, nascimento) VALUES
('VIVIANE', 59, 'F', 'CEO', 22127.00, '1964-01-08'),
('ALAN', 49, 'M', 'FINANCEIRO', 5367.00, '1991-08-21'),
('WANDA', 45, 'F', 'VENDEDOR', 4476.00, '1961-03-25'),
('WILLIAM', 50, 'M', 'VENDEDOR', 6986.00, '1973-03-08'),
('KELLY', 36, 'F', 'VENDEDOR', 6601.00, '1981-12-13');

-- Insert Initial Cliente Especial (as per your teammate's data)
INSERT INTO cliente_especial (nome, sexo, idade, id_cliente, cashback) VALUES
('ANTONIO CASTELÃO', 'M', 24, 1, 500.00),
('DIOGO', 'M', 21, 2, 700.00),
('FELIPE MORAES', 'M', 21, 4, 100.00),
('NEDSON', 'M', 21, 10, 700.00),
('SABRINA', 'F', 65, 28, 200.00),
('LUIZ', 'O', 64, 37, 340.00),
('CARLA', 'F', 71, 46, 90.00);

-- Create Views (3 views with JOINs and GROUP BY as required)
-- View 1: Clients and their cashback
CREATE VIEW view_cashback_cliente AS
SELECT c.nome, ce.cashback
FROM cliente c
JOIN cliente_especial ce ON c.id = ce.id_cliente
GROUP BY c.nome, ce.cashback;

-- View 2: Product quantities
CREATE VIEW view_produtos_quantidade AS
SELECT p.nome, p.quantidade
FROM produto p
GROUP BY p.nome, p.quantidade;

-- View 3: Sales by employee
CREATE VIEW view_vendas_funcionario AS
SELECT f.nome AS vendedor, COUNT(v.id) AS total_vendas
FROM funcionario f
JOIN venda v ON f.id = v.id_vendedor
GROUP BY f.nome;

-- Create Functions and Triggers
-- Function for registering a sale (replaces MySQL procedure)
CREATE OR REPLACE FUNCTION registrar_venda(
    p_data_venda DATE,
    p_id_vendedor INT,
    p_nome_funcionario VARCHAR,
    p_id_cliente INT,
    p_nome_cliente VARCHAR,
    p_id_produto INT,
    p_nome_produto VARCHAR,
    p_quantidade INT,
    p_valor_unitario DECIMAL
) RETURNS VOID AS $$
DECLARE
    v_id_venda INT;
    v_valor_total DECIMAL;
BEGIN
    -- Insert into venda table
    INSERT INTO venda (data_venda, id_vendedor, nome_funcionario, id_cliente, nome_cliente)
    VALUES (p_data_venda, p_id_vendedor, p_nome_funcionario, p_id_cliente, p_nome_cliente)
    RETURNING id INTO v_id_venda;

    -- Calculate total value
    v_valor_total := p_quantidade * p_valor_unitario;

    -- Insert into item_venda table
    INSERT INTO item_venda (id_venda, id_produto, nome_produto_vendido, quantidade, valor_unitario, valor_total)
    VALUES (v_id_venda, p_id_produto, p_nome_produto, p_quantidade, p_valor_unitario, v_valor_total);

    -- Update product quantity
    UPDATE produto
    SET quantidade = quantidade - p_quantidade
    WHERE id = p_id_produto;
END;
$$ LANGUAGE plpgsql;

-- Function for salary adjustment
CREATE OR REPLACE FUNCTION reajustar_salario(
    p_categoria cargo_type,
    p_percentual DECIMAL
) RETURNS VOID AS $$
BEGIN
    UPDATE funcionario
    SET salario = salario + (salario * (p_percentual / 100))
    WHERE cargo = p_categoria;
END;
$$ LANGUAGE plpgsql;

-- Function for random draw (sorteio)
CREATE OR REPLACE FUNCTION realizar_sorteio() RETURNS VOID AS $$
DECLARE
    v_id_cliente INT;
    v_nome_cliente VARCHAR;
    v_sexo sexo_type;
    v_idade INT;
BEGIN
    SELECT id, nome, sexo, idade
    INTO v_id_cliente, v_nome_cliente, v_sexo, v_idade
    FROM cliente
    ORDER BY RANDOM()
    LIMIT 1;

    IF EXISTS (SELECT 1 FROM cliente_especial WHERE id_cliente = v_id_cliente) THEN
        UPDATE cliente_especial
        SET cashback = 200
        WHERE id_cliente = v_id_cliente;
    ELSE
        INSERT INTO cliente_especial (nome, sexo, idade, id_cliente, cashback)
        VALUES (v_nome_cliente, v_sexo, v_idade, v_id_cliente, 100);
    END IF;
END;
$$ LANGUAGE plpgsql;

-- Function to calculate statistics
CREATE OR REPLACE FUNCTION calcular_estatisticas() RETURNS TABLE (
    produto_mais_vendido VARCHAR,
    vendedor_mais_vendido VARCHAR,
    produto_menos_vendido VARCHAR,
    valor_mais_vendido DECIMAL,
    mes_maior_venda_mais_vendido INT,
    mes_menor_venda_mais_vendido INT,
    valor_menos_vendido DECIMAL,
    mes_maior_venda_menos_vendido INT,
    mes_menor_venda_menos_vendido INT
) AS $$
DECLARE
    v_produto_mais_vendido_id INT;
    v_produto_menos_vendido_id INT;
BEGIN
    -- Produto mais vendido
    SELECT p.id, p.nome INTO v_produto_mais_vendido_id, produto_mais_vendido
    FROM item_venda i
    JOIN produto p ON i.id_produto = p.id
    GROUP BY p.id, p.nome
    ORDER BY SUM(i.quantidade) DESC
    LIMIT 1;

    -- Vendedor associado ao produto mais vendido
    SELECT f.nome INTO vendedor_mais_vendido
    FROM item_venda i
    JOIN venda v ON i.id_venda = v.id
    JOIN funcionario f ON v.id_vendedor = f.id
    WHERE i.id_produto = v_produto_mais_vendido_id
    GROUP BY f.nome
    ORDER BY COUNT(*) DESC
    LIMIT 1;

    -- Produto menos vendido
    SELECT p.id, p.nome INTO v_produto_menos_vendido_id, produto_menos_vendido
    FROM item_venda i
    JOIN produto p ON i.id_produto = p.id
    GROUP BY p.id, p.nome
    ORDER BY SUM(i.quantidade) ASC
    LIMIT 1;

    -- Valor ganho com o produto mais vendido
    SELECT SUM(i.valor_total) INTO valor_mais_vendido
    FROM item_venda i
    WHERE i.id_produto = v_produto_mais_vendido_id;

    -- Mês de maior venda do produto mais vendido
    SELECT EXTRACT(MONTH FROM v.data_venda) INTO mes_maior_venda_mais_vendido
    FROM item_venda i
    JOIN venda v ON i.id_venda = v.id
    WHERE i.id_produto = v_produto_mais_vendido_id
    GROUP BY EXTRACT(MONTH FROM v.data_venda)
    ORDER BY SUM(i.quantidade) DESC
    LIMIT 1;

    -- Mês de menor venda do produto mais vendido
    SELECT EXTRACT(MONTH FROM v.data_venda) INTO mes_menor_venda_mais_vendido
    FROM item_venda i
    JOIN venda v ON i.id_venda = v.id
    WHERE i.id_produto = v_produto_mais_vendido_id
    GROUP BY EXTRACT(MONTH FROM v.data_venda)
    ORDER BY SUM(i.quantidade) ASC
    LIMIT 1;

    -- Valor ganho com o produto menos vendido
    SELECT SUM(i.valor_total) INTO valor_menos_vendido
    FROM item_venda i
    WHERE i.id_produto = v_produto_menos_vendido_id;

    -- Mês de maior venda do produto menos vendido
    SELECT EXTRACT(MONTH FROM v.data_venda) INTO mes_maior_venda_menos_vendido
    FROM item_venda i
    JOIN venda v ON i.id_venda = v.id
    WHERE i.id_produto = v_produto_menos_vendido_id
    GROUP BY EXTRACT(MONTH FROM v.data_venda)
    ORDER BY SUM(i.quantidade) DESC
    LIMIT 1;

    -- Mês de menor venda do produto menos vendido
    SELECT EXTRACT(MONTH FROM v.data_venda) INTO mes_menor_venda_menos_vendido
    FROM item_venda i
    JOIN venda v ON i.id_venda = v.id
    WHERE i.id_produto = v_produto_menos_vendido_id
    GROUP BY EXTRACT(MONTH FROM v.data_venda)
    ORDER BY SUM(i.quantidade) ASC
    LIMIT 1;

    RETURN NEXT;
END;
$$ LANGUAGE plpgsql;

-- Trigger Function for adding/removing funcionario_especial
CREATE OR REPLACE FUNCTION trigger_funcionario_bonus() RETURNS TRIGGER AS $$
DECLARE
    v_id_func INT;
    v_nome_func VARCHAR;
    v_bonus DECIMAL;
BEGIN
    SELECT id_vendedor INTO v_id_func FROM venda WHERE id = NEW.id_venda;
    SELECT nome INTO v_nome_func FROM funcionario WHERE id = v_id_func;

    IF NEW.valor_total > 1000 THEN
        v_bonus := NEW.valor_total * 0.05;

        IF EXISTS (SELECT 1 FROM funcionario_especial WHERE id_funcionario = v_id_func) THEN
            UPDATE funcionario_especial
            SET bonus_total = bonus_total + v_bonus
            WHERE id_funcionario = v_id_func;
        ELSE
            INSERT INTO funcionario_especial (id_funcionario, nome, bonus_total)
            VALUES (v_id_func, v_nome_func, v_bonus);
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_funcionario_bonus
AFTER INSERT ON item_venda
FOR EACH ROW
EXECUTE FUNCTION trigger_funcionario_bonus();

-- Trigger Function for displaying total bonus
CREATE OR REPLACE FUNCTION trigger_mensagem_bonus() RETURNS TRIGGER AS $$
DECLARE
    v_total DECIMAL;
BEGIN
    SELECT SUM(bonus_total) INTO v_total FROM funcionario_especial;
    RAISE NOTICE '🔔 Total necessário para custear todos os bônus: R$ %', v_total;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_mensagem_total_bonus
AFTER UPDATE ON funcionario_especial
FOR EACH ROW
EXECUTE FUNCTION trigger_mensagem_bonus();

-- Trigger Function for adding/removing cliente_especial
CREATE OR REPLACE FUNCTION trigger_cashback_cliente() RETURNS TRIGGER AS $$
DECLARE
    v_id_cliente INT;
    v_nome VARCHAR;
    v_sexo sexo_type;
    v_idade INT;
    v_cashback DECIMAL;
BEGIN
    SELECT id_cliente INTO v_id_cliente FROM venda WHERE id = NEW.id_venda;
    SELECT nome, sexo, idade INTO v_nome, v_sexo, v_idade
    FROM cliente WHERE id = v_id_cliente;

    IF NEW.valor_total > 500 THEN
        v_cashback := NEW.valor_total * 0.02;

        IF EXISTS (SELECT 1 FROM cliente_especial WHERE id_cliente = v_id_cliente) THEN
            UPDATE cliente_especial
            SET cashback = cashback + v_cashback
            WHERE id_cliente = v_id_cliente;
        ELSE
            INSERT INTO cliente_especial (nome, sexo, idade, id_cliente, cashback)
            VALUES (v_nome, v_sexo, v_idade, v_id_cliente, v_cashback);
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_cashback_cliente
AFTER INSERT ON item_venda
FOR EACH ROW
EXECUTE FUNCTION trigger_cashback_cliente();

-- Trigger Function for displaying total cashback
CREATE OR REPLACE FUNCTION trigger_mensagem_cashback() RETURNS TRIGGER AS $$
DECLARE
    v_total_cashback DECIMAL;
BEGIN
    SELECT SUM(cashback) INTO v_total_cashback FROM cliente_especial;
    RAISE NOTICE '🔔 Valor necessário para custear todos os cashbacks: R$ %', v_total_cashback;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_mensagem_total_cashback
AFTER UPDATE ON cliente_especial
FOR EACH ROW
EXECUTE FUNCTION trigger_mensagem_cashback();

-- Trigger Function for removing cliente_especial with zero cashback
CREATE OR REPLACE FUNCTION trigger_remover_cliente_cashback_zero() RETURNS TRIGGER AS $$
BEGIN
    IF NEW.cashback = 0 THEN
        DELETE FROM cliente_especial WHERE id_cliente = NEW.id_cliente;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_remover_cliente_cashback_zero
AFTER UPDATE ON cliente_especial
FOR EACH ROW
EXECUTE FUNCTION trigger_remover_cliente_cashback_zero();

-- Create Users with Permissions
CREATE ROLE administrador;
CREATE ROLE gerente;
CREATE ROLE funcionario;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO administrador;

GRANT SELECT, UPDATE, DELETE ON ALL TABLES IN SCHEMA public TO gerente;

GRANT SELECT, INSERT ON venda, item_venda TO funcionario;
GRANT SELECT ON produto, cliente TO funcionario;

CREATE USER admin_user WITH PASSWORD 'admin123';
CREATE USER gerente_user WITH PASSWORD 'gerente123';
CREATE USER funcionario_user WITH PASSWORD 'func123';

GRANT administrador TO admin_user;
GRANT gerente TO gerente_user;
GRANT funcionario TO funcionario_user;

-- Test Data Insertion (as per your teammate's data)
SELECT registrar_venda('2025-07-01', 3, 'WANDA', 15, 'FELIPE MORAES', 2, 'Teclado Mecânico', 1, 276.99);
SELECT registrar_venda('2025-07-01', 4, 'WILLIAM', 18, 'FERNANDA', 5, 'Smartphone Android', 1, 2195.61);
SELECT registrar_venda('2025-07-01', 5, 'KELLY', 21, 'RAFAEL', 10, 'Fone de Ouvido', 3, 189.94);
SELECT registrar_venda('2025-07-01', 3, 'WANDA', 26, 'XIMENA', 4, 'Notebook i5', 1, 2064.47);
SELECT registrar_venda('2025-07-01', 4, 'WILLIAM', 30, 'NEDSON', 12, 'Cabo HDMI 2.0', 2, 86.11);

-- Command to drop the database if needed
-- DROP DATABASE LOJA_ELETRONICOS;