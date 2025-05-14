package com.lojaeletronicos.DAO;

import com.lojaeletronicos.model.*;

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
import java.util.logging.Level;
import java.util.logging.Logger;

public class OperacaoComercial {

    private static final Logger logger = Logger.getLogger(OperacaoComercial.class.getName());

    public void cadastrarProduto(String nome, int quantidade,
                                 String descricao, BigDecimal valor) {

        String sql = "INSERT INTO produto (nome, quantidade, descricao, valor) VALUES (?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            ps.setString     (1, nome);
            ps.setInt        (2, quantidade);
            ps.setString     (3, descricao);
            ps.setBigDecimal (4, valor);
            ps.executeUpdate();
            conn.commit();
            logger.info("Produto cadastrado: " + nome);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao cadastrar produto", e);
        }
    }

    public void cadastrarFuncionario(String nome, int idade, String sexo,
                                     String cargo, BigDecimal salario,
                                     Date nascimento) {

        String sql = "INSERT INTO funcionario (nome, idade, sexo, cargo, salario, nascimento) "
                   + "VALUES (?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            ps.setString     (1, nome);
            ps.setInt        (2, idade);
            ps.setString     (3, sexo.toUpperCase());
            ps.setString     (4, cargo.toUpperCase());
            ps.setBigDecimal (5, salario);
            ps.setDate       (6, nascimento);
            ps.executeUpdate();
            conn.commit();
            logger.info("Funcionário cadastrado: " + nome);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao cadastrar funcionário", e);
        }
    }

    public void registrarVenda(int idVendedor, int idCliente,
                               int idProduto, int quantidade) {

        String sqlConsulta = """
            SELECT f.nome  AS nome_funcionario,
                   c.nome  AS nome_cliente,
                   p.nome AS nome_produto,
                   p.valor AS valor_unitario
            FROM   funcionario f,
                   cliente c,
                   produto p
            WHERE  f.id = ?
              AND  c.id = ?
              AND  p.id = ?
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sqlConsulta)) {

            conn.setAutoCommit(false);

            ps.setInt(1, idVendedor);
            ps.setInt(2, idCliente);
            ps.setInt(3, idProduto);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                registrarVendaInterna(conn, idVendedor, idCliente, idProduto, quantidade, rs);
                conn.commit();
                logger.info("Venda registrada.");
            } else {
                conn.rollback();
                logger.warning("IDs inválidos na venda.");
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao registrar venda", e);
        }
    }

    private void registrarVendaInterna(Connection conn,
                                       int idVendedor, int idCliente,
                                       int idProduto, int quantidade,
                                       ResultSet rs) throws SQLException {

        String sql = "SELECT registrar_venda(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDate      (1, new Date(System.currentTimeMillis()));   // <- qualificado
            ps.setInt       (2, idVendedor);
            ps.setString    (3, rs.getString("nome_funcionario"));
            ps.setInt       (4, idCliente);
            ps.setString    (5, rs.getString("nome_cliente"));
            ps.setInt       (6, idProduto);
            ps.setString    (7, rs.getString("nome_produto"));
            ps.setInt       (8, quantidade);
            ps.setBigDecimal(9, rs.getBigDecimal("valor_unitario"));
            ps.execute();
        }
    }

    public List<Produto> listarProdutos() {
        List<Produto> produtos = new ArrayList<>();
        String sql = """
            SELECT id, nome, quantidade,
                   descricao, valor
            FROM   produto
            ORDER  BY nome
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery(sql)) {

            while (rs.next()) {
                produtos.add(new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("quantidade"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("valor")));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao listar produtos", e);
        }
        return produtos;
    }

    public List<Funcionario> listarFuncionarios() {
        List<Funcionario> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery("SELECT * FROM funcionario")) {

            while (rs.next()) {
                lista.add(new Funcionario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("idade"),
                        rs.getString("sexo"),
                        rs.getString("cargo"),
                        rs.getBigDecimal("salario"),
                        rs.getDate("nascimento")));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao listar funcionários", e);
        }
        return lista;
    }

    public List<String> listarVendasPorFuncionario() {
        List<String> lista = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery("SELECT * FROM view_vendas_funcionario")) {

            while (rs.next()) {
                lista.add(rs.getString("vendedor") + ": "
                        + rs.getInt("total_vendas") + " vendas");
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao listar vendas/funcionário", e);
        }
        return lista;
    }

    /* ---------- filtro de produtos ---------- */
    public List<Produto> filtrarProdutos(String nomeParcial,
                                         BigDecimal precoMin,
                                         BigDecimal precoMax) {

        List<Produto> produtos = new ArrayList<>();
        String sql = """
            SELECT id, nome, quantidade,
                   descricao, valor
            FROM   produto
            WHERE  ( ? IS NULL OR LOWER(nome) LIKE LOWER(?))
              AND  ( ? IS NULL OR valor >= ?)
              AND  ( ? IS NULL OR valor <= ?)
            ORDER  BY nome
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString    (1, nomeParcial == null ? null : "%" + nomeParcial + "%");
            ps.setString    (2, nomeParcial == null ? null : "%" + nomeParcial + "%");
            ps.setBigDecimal(3, precoMin);
            ps.setBigDecimal(4, precoMin);
            ps.setBigDecimal(5, precoMax);
            ps.setBigDecimal(6, precoMax);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                produtos.add(new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("quantidade"),
                        rs.getString("descricao"),
                        rs.getBigDecimal("valor")));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro no filtro de produtos", e);
        }
        return produtos;
    }

    /* ---------- vendas por cliente ---------- */
    public List<Venda> listarVendasPorCliente(int clienteId) {

        List<Venda> vendas = new ArrayList<>();
        String sql = """
            SELECT v.id, v.id_vendedor, v.id_cliente, v.data_venda,
                   iv.nome_produto_vendido, iv.quantidade,
                   iv.valor_unitario, iv.valor_total
            FROM   venda v
            JOIN   item_venda iv ON iv.id = v.id
            WHERE  v.id_cliente = ?
            ORDER  BY v.data_venda DESC, v.id
        """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, clienteId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                vendas.add(new Venda(
                        rs.getInt("id"),
                        rs.getInt("id_vendedor"),
                        rs.getInt("id_cliente"),
                        rs.getDate("data_venda"),
                        rs.getString("nome_produto_vendido"),
                        rs.getInt("quantidade"),
                        rs.getBigDecimal("valor_unitario"),
                        rs.getBigDecimal("valor_total")));
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao listar vendas por cliente", e);
        }
        return vendas;
    }

    public String obterEstatisticas() {
        StringBuilder sb = new StringBuilder();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement st   = conn.createStatement();
             ResultSet rs   = st.executeQuery("SELECT * FROM calcular_estatisticas()")) {

            if (rs.next()) {
                sb.append("Produto mais vendido: ").append(rs.getString("produto_mais_vendido")).append('\n')
                  .append("Vendedor associado: ").append(rs.getString("vendedor_mais_vendido")).append('\n')
                  .append("Produto menos vendido: ").append(rs.getString("produto_menos_vendido")).append('\n')
                  .append("Valor (mais vendido): R$ ").append(rs.getBigDecimal("valor_mais_vendido")).append('\n')
                  .append("Valor (menos vendido): R$ ").append(rs.getBigDecimal("valor_menos_vendido")).append('\n');
            }
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro em estatísticas", e);
        }
        return sb.toString();
    }

    public ClienteEspecial realizarSorteio() {
        List<ClienteEspecial> lista = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             Statement st   = conn.createStatement()) {

            conn.setAutoCommit(false);
            st.execute("SELECT realizar_sorteio()");
            ResultSet rs = st.executeQuery("SELECT * FROM cliente_especial");

            while (rs.next()) {
                lista.add(new ClienteEspecial(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("sexo"),
                        rs.getInt("idade"),
                        rs.getInt("id_cliente"),
                        rs.getBigDecimal("cashback")));
            }

            if (lista.isEmpty()) {
                conn.rollback();
                logger.warning("Sem clientes especiais.");
                return null;
            }

            ClienteEspecial sorteado = lista.get(new Random().nextInt(lista.size()));
            conn.commit();
            logger.info("Sorteado: " + sorteado.getNome());
            return sorteado;

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro no sorteio", e);
            return null;
        }
    }

    public void reajustarSalario(String categoria, double percentual) {
        String sql = "SELECT reajustar_salario(?,?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            conn.setAutoCommit(false);
            ps.setString(1, categoria.toUpperCase());
             ps.setBigDecimal(2, BigDecimal.valueOf(percentual));
            ps.execute();
            conn.commit();
            logger.info("Reajuste aplicado à categoria " + categoria);

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Erro ao reajustar salário", e);
        }
    }
}
