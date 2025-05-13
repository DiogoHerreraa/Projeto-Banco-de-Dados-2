package com.lojaeletronicos.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import com.lojaeletronicos.DAO.SellDAO;
import com.lojaeletronicos.model.ClienteEspecial;
import com.lojaeletronicos.model.Funcionario;

public class SistemaVendas {
    private JFrame frame;
    private SellDAO vendaDAO;

    public SistemaVendas() {
        vendaDAO = new SellDAO();
        initialize();
    }

    private void initialize() {
        frame = new JFrame("Sistema de Vendas - Eletrônicos");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);

        JTabbedPane tabbedPane = new JTabbedPane();

        // Vendas Panel
        JPanel vendasPanel = new JPanel();
        vendasPanel.setLayout(new GridLayout(5, 2));
        JLabel lblVendedor = new JLabel("Vendedor ID:");
        JTextField txtVendedor = new JTextField();
        JLabel lblCliente = new JLabel("Cliente ID:");
        JTextField txtCliente = new JTextField();
        JLabel lblProduto = new JLabel("Produto ID:");
        JTextField txtProduto = new JTextField();
        JLabel lblQuantidade = new JLabel("Quantidade:");
        JTextField txtQuantidade = new JTextField();
        JButton btnRegistrar = new JButton("Registrar Venda");
        btnRegistrar.addActionListener(e -> {
            try {
                int idVendedor = Integer.parseInt(txtVendedor.getText());
                int idCliente = Integer.parseInt(txtCliente.getText());
                int idProduto = Integer.parseInt(txtProduto.getText());
                int quantidade = Integer.parseInt(txtQuantidade.getText());
                vendaDAO.registrarVenda(idVendedor, idCliente, idProduto, quantidade);
                txtVendedor.setText("");
                txtCliente.setText("");
                txtProduto.setText("");
                txtQuantidade.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, insira valores válidos.");
            }
        });
        vendasPanel.add(lblVendedor);
        vendasPanel.add(txtVendedor);
        vendasPanel.add(lblCliente);
        vendasPanel.add(txtCliente);
        vendasPanel.add(lblProduto);
        vendasPanel.add(txtProduto);
        vendasPanel.add(lblQuantidade);
        vendasPanel.add(txtQuantidade);
        vendasPanel.add(new JLabel());
        vendasPanel.add(btnRegistrar);
        tabbedPane.addTab("Vendas", vendasPanel);

        // Estatísticas Panel
        JPanel estatisticasPanel = new JPanel();
        estatisticasPanel.setLayout(new BorderLayout());
        JTextArea txtEstatisticas = new JTextArea();
        txtEstatisticas.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtEstatisticas);
        JButton btnAtualizarEstatisticas = new JButton("Atualizar Estatísticas");
        btnAtualizarEstatisticas.addActionListener(e -> {
            txtEstatisticas.setText(vendaDAO.obterEstatisticas());
        });
        estatisticasPanel.add(scrollPane, BorderLayout.CENTER);
        estatisticasPanel.add(btnAtualizarEstatisticas, BorderLayout.SOUTH);
        tabbedPane.addTab("Estatísticas", estatisticasPanel);

        // Cadastrar Produto Panel
        JPanel cadastrarProdutoPanel = new JPanel();
        cadastrarProdutoPanel.setLayout(new GridLayout(6, 2));
        JLabel lblNomeProduto = new JLabel("Nome:");
        JTextField txtNomeProduto = new JTextField();
        JLabel lblQuantidadeProduto = new JLabel("Quantidade:");
        JTextField txtQuantidadeProduto = new JTextField();
        JLabel lblDescricaoProduto = new JLabel("Descrição:");
        JTextField txtDescricaoProduto = new JTextField();
        JLabel lblValorProduto = new JLabel("Valor:");
        JTextField txtValorProduto = new JTextField();
        JButton btnCadastrarProduto = new JButton("Cadastrar Produto");
        btnCadastrarProduto.addActionListener(e -> {
            try {
                String nome = txtNomeProduto.getText();
                int quantidade = Integer.parseInt(txtQuantidadeProduto.getText());
                String descricao = txtDescricaoProduto.getText();
                BigDecimal valor = new BigDecimal(txtValorProduto.getText());
                vendaDAO.cadastroProdutos(nome, quantidade, descricao, valor);
                txtNomeProduto.setText("");
                txtQuantidadeProduto.setText("");
                txtDescricaoProduto.setText("");
                txtValorProduto.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Quantidade e Valor devem ser números válidos.");
            }
        });
        cadastrarProdutoPanel.add(lblNomeProduto);
        cadastrarProdutoPanel.add(txtNomeProduto);
        cadastrarProdutoPanel.add(lblQuantidadeProduto);
        cadastrarProdutoPanel.add(txtQuantidadeProduto);
        cadastrarProdutoPanel.add(lblDescricaoProduto);
        cadastrarProdutoPanel.add(txtDescricaoProduto);
        cadastrarProdutoPanel.add(lblValorProduto);
        cadastrarProdutoPanel.add(txtValorProduto);
        cadastrarProdutoPanel.add(new JLabel());
        cadastrarProdutoPanel.add(btnCadastrarProduto);
        tabbedPane.addTab("Cadastrar Produto", cadastrarProdutoPanel);

        // Cadastrar Funcionário Panel
        JPanel cadastrarFuncionarioPanel = new JPanel();
        cadastrarFuncionarioPanel.setLayout(new GridLayout(7, 2));
        JLabel lblNomeFuncionario = new JLabel("Nome:");
        JTextField txtNomeFuncionario = new JTextField();
        JLabel lblIdadeFuncionario = new JLabel("Idade:");
        JTextField txtIdadeFuncionario = new JTextField();
        JLabel lblSexoFuncionario = new JLabel("Sexo (M/F/O):");
        JTextField txtSexoFuncionario = new JTextField();
        JLabel lblCargoFuncionario = new JLabel("Cargo:");
        JTextField txtCargoFuncionario = new JTextField();
        JLabel lblSalarioFuncionario = new JLabel("Salário:");
        JTextField txtSalarioFuncionario = new JTextField();
        JLabel lblNascimentoFuncionario = new JLabel("Nascimento (yyyy-MM-dd):");
        JTextField txtNascimentoFuncionario = new JTextField();
        JButton btnCadastrarFuncionario = new JButton("Cadastrar Funcionário");
        btnCadastrarFuncionario.addActionListener(e -> {
            try {
                String nome = txtNomeFuncionario.getText();
                int idade = Integer.parseInt(txtIdadeFuncionario.getText());
                String sexo = txtSexoFuncionario.getText();
                String cargo = txtCargoFuncionario.getText();
                BigDecimal salario = new BigDecimal(txtSalarioFuncionario.getText());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date nascimentoUtil = sdf.parse(txtNascimentoFuncionario.getText());
                java.sql.Date nascimentoSql = new java.sql.Date(nascimentoUtil.getTime());
                vendaDAO.cadastroFuncionario(nome, idade, sexo, cargo, salario, nascimentoSql);
                txtNomeFuncionario.setText("");
                txtIdadeFuncionario.setText("");
                txtSexoFuncionario.setText("");
                txtCargoFuncionario.setText("");
                txtSalarioFuncionario.setText("");
                txtNascimentoFuncionario.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Idade e Salário devem ser números válidos.");
            } catch (ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Data inválida. Use o formato yyyy-MM-dd.");
            }
        });
        cadastrarFuncionarioPanel.add(lblNomeFuncionario);
        cadastrarFuncionarioPanel.add(txtNomeFuncionario);
        cadastrarFuncionarioPanel.add(lblIdadeFuncionario);
        cadastrarFuncionarioPanel.add(txtIdadeFuncionario);
        cadastrarFuncionarioPanel.add(lblSexoFuncionario);
        cadastrarFuncionarioPanel.add(txtSexoFuncionario);
        cadastrarFuncionarioPanel.add(lblCargoFuncionario);
        cadastrarFuncionarioPanel.add(txtCargoFuncionario);
        cadastrarFuncionarioPanel.add(lblSalarioFuncionario);
        cadastrarFuncionarioPanel.add(txtSalarioFuncionario);
        cadastrarFuncionarioPanel.add(lblNascimentoFuncionario);
        cadastrarFuncionarioPanel.add(txtNascimentoFuncionario);
        cadastrarFuncionarioPanel.add(new JLabel());
        cadastrarFuncionarioPanel.add(btnCadastrarFuncionario);
        tabbedPane.addTab("Cadastrar Funcionário", cadastrarFuncionarioPanel);

        // Sorteio Panel
        JPanel sorteioPanel = new JPanel();
        sorteioPanel.setLayout(new BorderLayout());
        JTextArea txtSorteio = new JTextArea();
        txtSorteio.setEditable(false);
        JScrollPane scrollSorteio = new JScrollPane(txtSorteio);
        JButton btnRealizarSorteio = new JButton("Realizar Sorteio");
        btnRealizarSorteio.addActionListener(e -> {
            txtSorteio.setText("");
            ClienteEspecial clienteSorteado = vendaDAO.realizarSorteio();
            if (clienteSorteado != null) {
                txtSorteio.append("Cliente Sorteado:\n");
                txtSorteio.append("ID: " + clienteSorteado.getIdCliente() + "\n");
                txtSorteio.append("Nome: " + clienteSorteado.getNome() + "\n");
            }
        });
        sorteioPanel.add(scrollSorteio, BorderLayout.CENTER);
        sorteioPanel.add(btnRealizarSorteio, BorderLayout.SOUTH);
        tabbedPane.addTab("Sorteio", sorteioPanel);

        // Reajuste Panel
        JPanel reajustePanel = new JPanel();
        reajustePanel.setLayout(new GridLayout(3, 2));
        JLabel lblCategoria = new JLabel("Categoria:");
        JTextField txtCategoria = new JTextField();
        JLabel lblPercentual = new JLabel("Percentual (%):");
        JTextField txtPercentual = new JTextField();
        JButton btnReajustar = new JButton("Aplicar Reajuste");
        btnReajustar.addActionListener(e -> {
            try {
                String categoria = txtCategoria.getText();
                double percentual = Double.parseDouble(txtPercentual.getText());
                if (percentual < 0) {
                    JOptionPane.showMessageDialog(frame, "O percentual deve ser um número positivo!");
                    return;
                }
                vendaDAO.reajustarSalario(categoria, percentual);
                txtCategoria.setText("");
                txtPercentual.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "O percentual deve ser um número válido!");
            }
        });
        reajustePanel.add(lblCategoria);
        reajustePanel.add(txtCategoria);
        reajustePanel.add(lblPercentual);
        reajustePanel.add(txtPercentual);
        reajustePanel.add(new JLabel());
        reajustePanel.add(btnReajustar);
        tabbedPane.addTab("Reajuste", reajustePanel);

        // Listar Funcionários Panel
        JPanel listarFuncionariosPanel = new JPanel();
        listarFuncionariosPanel.setLayout(new BorderLayout());
        JTextArea txtFuncionarios = new JTextArea();
        txtFuncionarios.setEditable(false);
        JScrollPane scrollFuncionarios = new JScrollPane(txtFuncionarios);
        JButton btnAtualizarFuncionarios = new JButton("Atualizar Lista de Funcionários");
        btnAtualizarFuncionarios.addActionListener(e -> {
            txtFuncionarios.setText("");
            List<Funcionario> funcionarios = vendaDAO.listarFuncionarios();
            for (Funcionario funcionario : funcionarios) {
                txtFuncionarios.append("ID: " + funcionario.getId() + ", Nome: " + funcionario.getNome() +
                                      ", Salário: " + funcionario.getSalario() + "\n");
            }
        });
        listarFuncionariosPanel.add(scrollFuncionarios, BorderLayout.CENTER);
        listarFuncionariosPanel.add(btnAtualizarFuncionarios, BorderLayout.SOUTH);
        tabbedPane.addTab("Listar Funcionários", listarFuncionariosPanel);

        frame.add(tabbedPane);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SistemaVendas::new);
    }
}