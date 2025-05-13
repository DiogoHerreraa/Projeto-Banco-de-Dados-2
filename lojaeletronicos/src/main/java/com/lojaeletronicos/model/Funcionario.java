package com.lojaeletronicos.model;

import java.math.BigDecimal;
import java.util.Date;

public class Funcionario {
    private int id;
    private String nome;
    private int idade;
    private String sexo;
    private String cargo;
    private BigDecimal salario;
    private Date nascimento;

    public Funcionario(int id, String nome, int idade, String sexo, String cargo, BigDecimal salario, Date nascimento) {
        this.id = id;
        this.nome = nome;
        this.idade = idade;
        this.sexo = sexo;
        this.cargo = cargo;
        this.salario = salario;
        this.nascimento = nascimento;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public BigDecimal getSalario() {
        return salario;
    }
}