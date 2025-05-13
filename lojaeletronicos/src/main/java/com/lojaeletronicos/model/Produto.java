package com.lojaeletronicos.model;

import java.math.BigDecimal;

public class Produto {
    private int id;
    private String nome;
    private int quantidade;
    private String descricao;
    private BigDecimal valor;

    public Produto(int id, String nome, int quantidade, String descricao, BigDecimal valor) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.descricao = descricao;
        this.valor = valor;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValor() {
        return valor;
    }
}