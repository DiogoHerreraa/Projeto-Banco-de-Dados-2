package com.lojaeletronicos.DAO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JOptionPane;

import com.lojaeletronicos.model.ClienteEspecial;
import com.lojaeletronicos.model.Funcionario;
import com.lojaeletronicos.model.Produto;

public class SellDAO {

    public static void cadastroProdutos(String nome, int quantidade, String descricao, BigDecimal valor) {
        String sql = "INSERT INTO produto (nome, quantidade, descricao, valor) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setInt(2, quantidade);
            stmt.setString(3, descricao);
            stmt.setBigDecimal(4, valor);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Produto cadastrado com sucesso!");
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, "Erro ao realizar cadastro do produto: " + e.getMessage());
        }
    }

    public void cadastroFuncionario(String nome, int idade, String sexo, String cargo, BigDecimal salario, Date nascimento) {
        String sql = "INSERT INTO funcionario (nome, idade, sexo, cargo, salario, nascimento) VALUES (?, ?, ?, ?, ?, ?)";
        
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nome);
            stmt.setInt(2, idade);
            stmt.setString(3, sexo.toUpperCase());
            stmt.setString(4, cargo.toUpperCase());
            stmt.setBigDecimal(5, salario);
            stmt.setDate(6, nascimento);
            stmt.executeUpdate();
            JOptionPane.showMessageDialog(null, "Funcionário cadastrado com sucesso!");
        } catch (SQLException e) { 
            JOptionPane.showMessageDialog(null, "Erro ao realizar cadastro do funcionário: " + e.getMessage());
        }
    }

    public void registrarVenda(int idVendedor, int idCliente, int idProduto, int quantidade) {
        String sqlGetDetails = "SELECT f.nome AS nome_funcionario, c.nome AS nome_cliente, p.nome AS nome_produto, p.valor AS valor_unitario " +
                               "FROM funcionario f, cliente c, produto p " +
                               "WHERE f.id = ? AND c.id = ? AND p.id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmtDetails = conn.prepareStatement(sqlGetDetails)) {
            stmtDetails.setInt(1, idVendedor);
            stmtDetails.setInt(2, idCliente);
            stmtDetails.setInt(3, idProduto);
            ResultSet rs = stmtDetails.executeQuery();

            if (rs.next()) {
                String nomeFuncionario = rs.getString("nome_funcionario");
                String nomeCliente = rs.getString("nome_cliente");
                String nomeProduto = rs.getString("nome_produto");
                BigDecimal valorUnitario = rs.getBigDecimal("valor_unitario");

                String sqlCallFunction = "SELECT registrar_venda(?, ?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement stmtVenda = conn.prepareStatement(sqlCallFunction)) {
                    stmtVenda.setDate(1, new Date(System.currentTimeMillis()));
                    stmtVenda.setInt(2, idVendedor);
                    stmtVenda.setString(3, nomeFuncionario);
                    stmtVenda.setInt(4, idCliente);
                    stmtVenda.setString(5, nomeCliente);
                    stmtVenda.setInt(6, idProduto);
                    stmtVenda.setString(7, nomeProduto);
                    stmtVenda.setInt(8, quantidade);
                    stmtVenda.setBigDecimal(9, valorUnitario);
                    stmtVenda.execute();
                    JOptionPane.showMessageDialog(null, "Venda registrada com sucesso!");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Dados inválidos para vendedor, cliente ou produto!");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao registrar venda: " + e.getMessage());
        }
    }

    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sqlLista = "SELECT * FROM view_produtos_quantidade";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sqlLista)) {
            while (rs.next()) {
                produtos.add(new Produto(0, rs.getString("nome"), rs.getInt("quantidade"), "", BigDecimal.ZERO));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar produtos: " + e.getMessage());
        }
        return produtos;
    }

    public List<String> listarVendasPorFuncionario() {
        List<String> vendas = new ArrayList<>();
        String sql = "SELECT * FROM view_vendas_funcionario";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                vendas.add(rs.getString("vendedor") + ": " + rs.getInt("total_vendas") + " vendas");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar vendas: " + e.getMessage());
        }
        return vendas;
    }

    public void reajustarSalario(String categoria, double percentual) {
        String sql = "SELECT reajustar_salario(?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.toUpperCase());
            stmt.setDouble(2, percentual);
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Reajuste de " + percentual + "% aplicado com sucesso à categoria " + categoria + "!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao realizar reajuste: " + e.getMessage());
        }
    }

    public ClienteEspecial realizarSorteio() {
        String sql = "SELECT realizar_sorteio()";
        List<ClienteEspecial> clientesEspeciais = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);

            String sqlFetch = "SELECT * FROM cliente_especial";
            try (ResultSet rs = stmt.executeQuery(sqlFetch)) {
                while (rs.next()) {
                    clientesEspeciais.add(new ClienteEspecial(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("sexo"),
                        rs.getInt("idade"),
                        rs.getInt("id_cliente"),
                        rs.getBigDecimal("cashback")
                    ));
                }
            }

            if (clientesEspeciais.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Nenhum cliente especial disponível para o sorteio!");
                return null;
            }

            Random random = new Random();
            int indiceSorteado = random.nextInt(clientesEspeciais.size());
            ClienteEspecial clienteSorteado = clientesEspeciais.get(indiceSorteado);
            JOptionPane.showMessageDialog(null, "Cliente sorteado: " + clienteSorteado.getNome() + " (ID: " + clienteSorteado.getIdCliente() + ")");
            return clienteSorteado;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao realizar sorteio: " + e.getMessage());
            return null;
        }
    }

    public List<Funcionario> listarFuncionarios() {
        List<Funcionario> funcionarios = new ArrayList<>();
        String sql = "SELECT id, nome, idade, sexo, cargo, salario, nascimento FROM funcionario";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                funcionarios.add(new Funcionario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getInt("idade"),
                    rs.getString("sexo"),
                    rs.getString("cargo"),
                    rs.getBigDecimal("salario"),
                    rs.getDate("nascimento")
                ));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao listar funcionários: " + e.getMessage());
        }
        return funcionarios;
    }

    public String obterEstatisticas() {
        StringBuilder estatisticas = new StringBuilder();
        String sql = "SELECT * FROM calcular_estatisticas()";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            if (rs.next()) {
                estatisticas.append("Produto mais vendido: ").append(rs.getString("produto_mais_vendido")).append("\n");
                estatisticas.append("Vendedor associado: ").append(rs.getString("vendedor_mais_vendido")).append("\n");
                estatisticas.append("Produto menos vendido: ").append(rs.getString("produto_menos_vendido")).append("\n");
                estatisticas.append("Valor ganho com o mais vendido: R$ ").append(rs.getBigDecimal("valor_mais_vendido")).append("\n");
                estatisticas.append("Mês de maior venda (mais vendido): ").append(rs.getInt("mes_maior_venda_mais_vendido")).append("\n");
                estatisticas.append("Mês de menor venda (mais vendido): ").append(rs.getInt("mes_menor_venda_mais_vendido")).append("\n");
                estatisticas.append("Valor ganho com o menos vendido: R$ ").append(rs.getBigDecimal("valor_menos_vendido")).append("\n");
                estatisticas.append("Mês de maior venda (menos vendido): ").append(rs.getInt("mes_maior_venda_menos_vendido")).append("\n");
                estatisticas.append("Mês de menor venda (menos vendido): ").append(rs.getInt("mes_menor_venda_menos_vendido")).append("\n");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Erro ao obter estatísticas: " + e.getMessage());
        }
        return estatisticas.toString();
    }
}