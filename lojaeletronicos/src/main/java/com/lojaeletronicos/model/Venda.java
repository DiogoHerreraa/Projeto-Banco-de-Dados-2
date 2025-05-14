package com.lojaeletronicos.model;

import java.math.BigDecimal;
import java.util.Date;


public class Venda {

    private int id;
    private int idVendedor;
    private int idCliente;
    private Date data;

    private String nomeProduto;
    private int quantidade;
    private BigDecimal valorUnitario;
    private BigDecimal valorTotal;

    public Venda(int id, int idVendedor, int idCliente, Date data) {
        this(id, idVendedor, idCliente, data, null, 0, null, null);
    }

    public Venda(int id,
                 int idVendedor,
                 int idCliente,
                 Date data,
                 String nomeProduto,
                 int quantidade,
                 BigDecimal valorUnitario,
                 BigDecimal valorTotal) {
        this.id = id;
        this.idVendedor = idVendedor;
        this.idCliente = idCliente;
        this.data = data;
        this.nomeProduto   = nomeProduto;
        this.quantidade    = quantidade;
        this.valorUnitario = valorUnitario;
        this.valorTotal    = valorTotal;
    }

    public int getId()           { return id; }
    public int getIdVendedor()   { return idVendedor; }
    public int getIdCliente()    { return idCliente; }
    public Date getData()        { return data; }
    public String getNomeProduto()      { return nomeProduto; }
    public int getQuantidade()          { return quantidade; }
    public BigDecimal getValorUnitario(){ return valorUnitario; }
    public BigDecimal getValorTotal()   { return valorTotal; }
}
