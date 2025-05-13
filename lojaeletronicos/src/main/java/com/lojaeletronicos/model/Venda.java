package com.lojaeletronicos.model;

import java.util.Date;

public class Venda {
    private int id;
    private int idVendedor;
    private int idCliente;
    private Date data;

    public Venda(int id, int idVendedor, int idCliente, Date data) {
        this.id = id;
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
        this.data = data;
    }
}