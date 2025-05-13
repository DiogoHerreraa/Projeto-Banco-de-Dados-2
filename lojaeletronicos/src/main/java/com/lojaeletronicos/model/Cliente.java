package com.lojaeletronicos.model;

import java.util.Date;

public class Cliente {
    private int id;
    private String nome;
    private String sexo;
    private int idade;
    private Date nascimento;

    public Cliente(int id, String nome, String sexo, int idade, Date nascimento) {
        this.id = id;
        this.nome = nome;
        this.sexo = sexo;
        this.idade = idade;
        this.nascimento = nascimento;
    }

    public int getId() { 
        return id; 
    }
    
    public String getNome() { 
        return nome; 
    }
   
    public String getSexo() { 
        return sexo; 
    }
    
    public int getIdade() {
        return idade; 
    }
    
    public Date getNascimento() {
        return nascimento; 
    }
}