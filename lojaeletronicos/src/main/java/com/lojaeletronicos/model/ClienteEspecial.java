package com.lojaeletronicos.model;

import java.math.BigDecimal;

public class ClienteEspecial {
    private int id;
    private String nome;
    private String sexo;
    private int idade;
    private int idCliente;
    private BigDecimal cashback;

    public ClienteEspecial(int id, String nome, String sexo, int idade, int idCliente, BigDecimal cashback) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.idade = idade;
        this.idCliente = idCliente;
        this.cashback = cashback;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getCashback() {
        return cashback;
    }

    public void setCashback(BigDecimal cashback) {
        this.cashback = cashback; 
    }
}