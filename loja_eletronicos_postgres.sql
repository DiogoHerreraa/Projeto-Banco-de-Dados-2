DROP VIEW IF EXISTS view_vendas_funcionario;
CREATE OR REPLACE VIEW view_vendas_funcionario AS
SELECT   f.nome                AS vendedor,
         COUNT(v.id_venda)     AS total_vendas
FROM     venda v
JOIN     funcionario f ON f.id_funcionario = v.id_vendedor
GROUP BY f.nome
ORDER BY total_vendas DESC;

DROP FUNCTION IF EXISTS registrar_venda(
    DATE, INT, TEXT, INT, TEXT, INT, TEXT, INT, NUMERIC
);

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
    v_id_venda  INT;
BEGIN
    -- 1) lança a venda
    INSERT INTO venda (data_venda, id_vendedor, id_cliente)
    VALUES (p_data, p_id_vendedor, p_id_cliente)
    RETURNING id_venda INTO v_id_venda;

    -- 2) lança o(s) item(ns)
    INSERT INTO item_venda (id_venda, id_produto,
                            nome_produto_vendido, quantidade,
                            valor_unitario, valor_total)
    VALUES ( v_id_venda,
             p_id_produto,
             p_nome_produto,
             p_quantidade,
             p_valor_unitario,
             p_valor_unitario * p_quantidade );

    -- 3) baixa o estoque
    UPDATE produto
       SET quantidade_produto = quantidade_produto - p_quantidade
     WHERE idproduto = p_id_produto;
END;
$$;

DROP FUNCTION IF EXISTS reajustar_salario(TEXT, NUMERIC);
CREATE OR REPLACE FUNCTION reajustar_salario(
    p_cargo       TEXT,
    p_percentual  NUMERIC
) RETURNS VOID
LANGUAGE plpgsql AS
$$
BEGIN
    UPDATE funcionario
       SET salario = salario * (1 + p_percentual/100.0)
     WHERE UPPER(cargo) = UPPER(p_cargo);
END;
$$;

DROP FUNCTION IF EXISTS realizar_sorteio();
CREATE OR REPLACE FUNCTION realizar_sorteio() RETURNS VOID
LANGUAGE plpgsql AS
$$
DECLARE
    v_cliente RECORD;
BEGIN
    SELECT *
      INTO v_cliente
      FROM cliente_padrao c
     WHERE NOT EXISTS (SELECT 1
                         FROM cliente_especial ce
                        WHERE ce.id_cliente = c.id_cpadrao)
  ORDER BY random()
     LIMIT 1;

    IF NOT FOUND THEN
        RAISE NOTICE 'Todos os clientes já são especiais.';
        RETURN;
    END IF;

    INSERT INTO cliente_especial (nome_cespecial, sexo, idade,
                                  cashback, id_cliente)
    VALUES (v_cliente.nome_cpadrao,
            v_cliente.sexo,
            v_cliente.idade,
            0.05 * (SELECT COALESCE(SUM(iv.valor_total),0)
                      FROM venda v
                      JOIN item_venda iv USING(id_venda)
                     WHERE v.id_cliente = v_cliente.id_cpadrao),
            v_cliente.id_cpadrao);
END;
$$;

DROP FUNCTION IF EXISTS calcular_estatisticas();
CREATE OR REPLACE FUNCTION calcular_estatisticas()
RETURNS TABLE (
    produto_mais_vendido      TEXT,
    vendedor_mais_vendido     TEXT,
    produto_menos_vendido     TEXT,
    valor_mais_vendido        NUMERIC,
    valor_menos_vendido       NUMERIC
) LANGUAGE plpgsql AS
$$
BEGIN
    RETURN QUERY
    WITH resumo AS (
        SELECT iv.nome_produto_vendido AS produto,
               SUM(iv.quantidade)      AS qtd_total,
               SUM(iv.valor_total)     AS valor_total
        FROM   item_venda iv
        GROUP  BY iv.nome_produto_vendido
    ),
    ranking AS (
        SELECT produto,
               qtd_total,
               valor_total,
               RANK() OVER (ORDER BY qtd_total DESC) AS rk_desc,
               RANK() OVER (ORDER BY qtd_total ASC)  AS rk_asc
        FROM   resumo
    )
    SELECT  (SELECT produto      FROM ranking WHERE rk_desc = 1) AS produto_mais_vendido,
            (SELECT f.nome
               FROM venda v
               JOIN funcionario f ON f.id_funcionario = v.id_vendedor
               JOIN item_venda iv USING(id_venda)
              GROUP BY f.nome
              ORDER BY SUM(iv.quantidade) DESC
              LIMIT 1) AS vendedor_mais_vendido,
            (SELECT produto      FROM ranking WHERE rk_asc  = 1) AS produto_menos_vendido,
            (SELECT valor_total  FROM ranking WHERE rk_desc = 1) AS valor_mais_vendido,
            (SELECT valor_total  FROM ranking WHERE rk_asc  = 1) AS valor_menos_vendido;
END;
$$;
