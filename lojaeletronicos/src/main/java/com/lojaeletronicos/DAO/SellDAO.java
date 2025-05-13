package com.lojaeletronicos.DAO;

import com.lojaeletronicos.model.ClienteEspecial;
import com.lojaeletronicos.model.Funcionario;
import com.lojaeletronicos.model.Produto;

import javax.swing.JOptionPane;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SellDAO {

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void cadastrarProduto(String nome, int quantidade, String descricao, BigDecimal valor) {
        String sql = "INSERT INTO produto (nome, quantidade, descricao, valor) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nome);
            stmt.setInt(2, quantidade);
            stmt.setString(3, descricao);
            stmt.setBigDecimal(4, valor);
            stmt.executeUpdate();
            showMessage("Produto cadastrado com sucesso!");

        } catch (SQLException e) {
            showMessage("Erro ao cadastrar produto: " + e.getMessage());
        }
    }

    public void cadastrarFuncionario(String nome, int idade, String sexo, String cargo, BigDecimal salario, Date nascimento) {
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
            showMessage("Funcionário cadastrado com sucesso!");

        } catch (SQLException e) {
            showMessage("Erro ao cadastrar funcionário: " + e.getMessage());
        }
    }

    public void registrarVenda(int idVendedor, int idCliente, int idProduto, int quantidade) {
        String sqlConsulta = """
            SELECT f.nome AS nome_funcionario, c.nome AS nome_cliente, p.nome AS nome_produto, p.valor AS valor_unitario
            FROM funcionario f, cliente c, produto p
            WHERE f.id = ? AND c.id = ? AND p.id = ?
            """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement consulta = conn.prepareStatement(sqlConsulta)) {

            consulta.setInt(1, idVendedor);
            consulta.setInt(2, idCliente);
            consulta.setInt(3, idProduto);

            ResultSet rs = consulta.executeQuery();

            if (rs.next()) {
                registrarVendaInterna(conn, idVendedor, idCliente, idProduto, quantidade, rs);
            } else {
                showMessage("Vendedor, cliente ou produto inválido.");
            }

        } catch (SQLException e) {
            showMessage("Erro ao registrar venda: " + e.getMessage());
        }
    }

    private void registrarVendaInterna(Connection conn, int idVendedor, int idCliente, int idProduto, int quantidade, ResultSet rs) throws SQLException {
        String sql = "SELECT registrar_venda(?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, new Date(System.currentTimeMillis()));
            stmt.setInt(2, idVendedor);
            stmt.setString(3, rs.getString("nome_funcionario"));
            stmt.setInt(4, idCliente);
            stmt.setString(5, rs.getString("nome_cliente"));
            stmt.setInt(6, idProduto);
            stmt.setString(7, rs.getString("nome_produto"));
            stmt.setInt(8, quantidade);
            stmt.setBigDecimal(9, rs.getBigDecimal("valor_unitario"));
            stmt.execute();
            showMessage("Venda registrada com sucesso!");
        }
    }

    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = "SELECT * FROM view_produtos_quantidade";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                produtos.add(new Produto(0, rs.getString("nome"), rs.getInt("quantidade"), "", BigDecimal.ZERO));
            }

        } catch (SQLException e) {
            showMessage("Erro ao listar produtos: " + e.getMessage());
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
            showMessage("Erro ao listar vendas por funcionário: " + e.getMessage());
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
            showMessage("Reajuste de " + percentual + "% aplicado à categoria " + categoria);

        } catch (SQLException e) {
            showMessage("Erro ao reajustar salário: " + e.getMessage());
        }
    }

    public ClienteEspecial realizarSorteio() {
        List<ClienteEspecial> clientesEspeciais = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute("SELECT realizar_sorteio()");
            ResultSet rs = stmt.executeQuery("SELECT * FROM cliente_especial");

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

            if (clientesEspeciais.isEmpty()) {
                showMessage("Nenhum cliente especial disponível.");
                return null;
            }

            ClienteEspecial sorteado = clientesEspeciais.get(new Random().nextInt(clientesEspeciais.size()));
            showMessage("Cliente sorteado: " + sorteado.getNome() + " (ID: " + sorteado.getIdCliente() + ")");
            return sorteado;

        } catch (SQLException e) {
            showMessage("Erro ao realizar sorteio: " + e.getMessage());
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
            showMessage("Erro ao listar funcionários: " + e.getMessage());
        }
        return funcionarios;
    }

    public String obterEstatisticas() {
        StringBuilder sb = new StringBuilder();
        String sql = "SELECT * FROM calcular_estatisticas()";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                sb.append("Produto mais vendido: ").append(rs.getString("produto_mais_vendido")).append("\n")
                  .append("Vendedor associado: ").append(rs.getString("vendedor_mais_vendido")).append("\n")
                  .append("Produto menos vendido: ").append(rs.getString("produto_menos_vendido")).append("\n")
                  .append("Valor ganho com o mais vendido: R$ ").append(rs.getBigDecimal("valor_mais_vendido")).append("\n")
                  .append("Mês de maior venda (mais vendido): ").append(rs.getInt("mes_maior_venda_mais_vendido")).append("\n")
                  .append("Mês de menor venda (mais vendido): ").append(rs.getInt("mes_menor_venda_mais_vendido")).append("\n")
                  .append("Valor ganho com o menos vendido: R$ ").append(rs.getBigDecimal("valor_menos_vendido")).append("\n")
                  .append("Mês de maior venda (menos vendido): ").append(rs.getInt("mes_maior_venda_menos_vendido")).append("\n")
                  .append("Mês de menor venda (menos vendido): ").append(rs.getInt("mes_menor_venda_menos_vendido")).append("\n");
            }

        } catch (SQLException e) {
            showMessage("Erro ao obter estatísticas: " + e.getMessage());
        }
        return sb.toString();
    }
}
