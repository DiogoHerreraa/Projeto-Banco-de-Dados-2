package com.lojaeletronicos.view;

import com.lojaeletronicos.DAO.OperacaoComercial;
import com.lojaeletronicos.model.*;

import javax.swing.*;
import java.awt.*;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class SistemaVendas {

    private final JFrame frame = new JFrame("Sistema de Vendas - Eletrônicos");
    private final OperacaoComercial dao = new OperacaoComercial();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(SistemaVendas::new);
    }

    public SistemaVendas() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(750, 480);

        JTabbedPane tabs = new JTabbedPane();
        tabs.add("Vendas",              abaRegistrarVenda());
        tabs.add("Produtos (filtro)",   abaFiltroProdutos());
        tabs.add("Vendas por Cliente",  abaVendasCliente());
        tabs.add("Estatísticas",        abaEstatisticas());
        tabs.add("Cadastrar Produto",   abaCadastrarProduto());
        tabs.add("Cadastrar Funcionário", abaCadastrarFuncionario());
        tabs.add("Listar Produtos",     abaListarProdutos());
        tabs.add("Listar Funcionários", abaListarFuncionarios());
        tabs.add("Sorteio",             abaSorteio());
        tabs.add("Reajuste",            abaReajuste());

        frame.add(tabs);
        frame.setVisible(true);
    }

    private JPanel abaRegistrarVenda() {
        JPanel p = new JPanel(new GridLayout(5, 2));

        JTextField v  = new JTextField();
        JTextField c  = new JTextField();
        JTextField pr = new JTextField();
        JTextField q  = new JTextField();

        p.add(new JLabel("Vendedor ID:")); p.add(v);
        p.add(new JLabel("Cliente ID:"));  p.add(c);
        p.add(new JLabel("Produto ID:"));  p.add(pr);
        p.add(new JLabel("Quantidade:"));  p.add(q);

        JButton btn = new JButton("Registrar");
        btn.addActionListener(e -> {
            try {
                dao.registrarVenda(Integer.parseInt(v.getText()),
                                   Integer.parseInt(c.getText()),
                                   Integer.parseInt(pr.getText()),
                                   Integer.parseInt(q.getText()));
                v.setText(""); c.setText(""); pr.setText(""); q.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Dados inválidos.");
            }
        });
        p.add(new JLabel()); p.add(btn);
        return p;
    }

    /* ---------------- filtro de produtos ---------------- */
    private JPanel abaFiltroProdutos() {

        JPanel root = new JPanel(new BorderLayout());
        JPanel filtros = new JPanel(new GridLayout(2, 4));

        JTextField nome = new JTextField();
        JTextField min  = new JTextField();
        JTextField max  = new JTextField();
        JTextArea  res  = new JTextArea(); res.setEditable(false);

        filtros.add(new JLabel("Nome contém:")); filtros.add(nome);
        filtros.add(new JLabel("Preço mín:"));   filtros.add(min);
        filtros.add(new JLabel("Preço máx:"));   filtros.add(max);

        JButton buscar = new JButton("Buscar");
        buscar.addActionListener(e -> {
            try {
                BigDecimal pMin = min.getText().isBlank() ? null : new BigDecimal(min.getText());
                BigDecimal pMax = max.getText().isBlank() ? null : new BigDecimal(max.getText());

                List<Produto> lista = dao.filtrarProdutos(
                        nome.getText().isBlank() ? null : nome.getText(),
                        pMin, pMax);

                res.setText("");
                if (lista.isEmpty()) {
                    res.append("Nenhum produto encontrado.");
                } else {
                    for (Produto p : lista) {
                        res.append(String.format("ID%-4d %-25s  R$ %8.2f  Qtd:%d%n",
                                p.getId(), p.getNome(), p.getValor(), p.getQuantidade()));
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Preços inválidos.");
            }
        });
        filtros.add(new JLabel()); filtros.add(buscar);

        root.add(filtros, BorderLayout.NORTH);
        root.add(new JScrollPane(res), BorderLayout.CENTER);
        return root;
    }

    /* ---------------- vendas por cliente ---------------- */
    private JPanel abaVendasCliente() {

        JPanel root = new JPanel(new BorderLayout());
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT));

        JTextField id = new JTextField(6);
        JTextArea  txt = new JTextArea(); txt.setEditable(false);

        JButton buscar = new JButton("Buscar");
        buscar.addActionListener(e -> {
            try {
                int clienteId = Integer.parseInt(id.getText());
                List<Venda> vendas = dao.listarVendasPorCliente(clienteId);

                txt.setText("");
                if (vendas.isEmpty()) {
                    txt.append("Sem vendas para o cliente.\n");
                } else {
                    for (Venda v : vendas) {
                        txt.append(String.format(
                            "Venda %d  (%tF)  → %dx %s  [R$ %.2f]%n",
                            v.getId(), v.getData(), v.getQuantidade(),
                            v.getNomeProduto(), v.getValorTotal()));
                    }
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID inválido.");
            }
        });

        topo.add(new JLabel("Cliente ID:"));
        topo.add(id);
        topo.add(buscar);

        root.add(topo, BorderLayout.NORTH);
        root.add(new JScrollPane(txt), BorderLayout.CENTER);
        return root;
    }

    /* ---------------- estatísticas --------------------- */
    private JPanel abaEstatisticas() {
        JTextArea txt = new JTextArea(); txt.setEditable(false);
        JButton btn   = new JButton("Atualizar");
        btn.addActionListener(e -> txt.setText(dao.obterEstatisticas()));

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(txt), BorderLayout.CENTER);
        p.add(btn, BorderLayout.SOUTH);
        return p;
    }

    /* ---------------- cadastro produto ----------------- */
    private JPanel abaCadastrarProduto() {

        JPanel p = new JPanel(new GridLayout(6, 2));
        JTextField n = new JTextField(),
                   q = new JTextField(),
                   d = new JTextField(),
                   v = new JTextField();

        p.add(new JLabel("Nome:"));       p.add(n);
        p.add(new JLabel("Quantidade:")); p.add(q);
        p.add(new JLabel("Descrição:"));  p.add(d);
        p.add(new JLabel("Valor R$:"));   p.add(v);

        JButton btn = new JButton("Salvar");
        btn.addActionListener(e -> {
            try {
                dao.cadastrarProduto(n.getText(),
                        Integer.parseInt(q.getText()),
                        d.getText(),
                        new BigDecimal(v.getText()));
                n.setText(""); q.setText(""); d.setText(""); v.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Dados inválidos.");
            }
        });
        p.add(new JLabel()); p.add(btn);
        return p;
    }

    /* ---------------- cadastro funcionário -------------- */
    private JPanel abaCadastrarFuncionario() {

        JPanel p = new JPanel(new GridLayout(7,2));
        JTextField n=new JTextField(),
                   i=new JTextField(),
                   s=new JTextField(),
                   c=new JTextField(),
                   sal=new JTextField(),
                   nas=new JTextField();

        p.add(new JLabel("Nome:")); p.add(n);
        p.add(new JLabel("Idade:")); p.add(i);
        p.add(new JLabel("Sexo (M/F/O):")); p.add(s);
        p.add(new JLabel("Cargo:")); p.add(c);
        p.add(new JLabel("Salário R$:")); p.add(sal);
        p.add(new JLabel("Nascimento (yyyy-MM-dd):")); p.add(nas);

        JButton btn = new JButton("Salvar");
        btn.addActionListener(e -> {
            try {
                java.sql.Date nascimento = new java.sql.Date(
                        new SimpleDateFormat("yyyy-MM-dd")
                                .parse(nas.getText()).getTime());
                dao.cadastrarFuncionario(
                        n.getText(),
                        Integer.parseInt(i.getText()),
                        s.getText(), c.getText(),
                        new BigDecimal(sal.getText()),
                        nascimento);
                n.setText(""); i.setText(""); s.setText("");
                c.setText(""); sal.setText(""); nas.setText("");
            } catch (NumberFormatException | ParseException ex) {
                JOptionPane.showMessageDialog(frame, "Dados inválidos.");
            }
        });
        p.add(new JLabel()); p.add(btn);
        return p;
    }

    /* ---------------- listar produtos ------------------ */
    private JPanel abaListarProdutos() {
        JTextArea txt = new JTextArea(); txt.setEditable(false);
        JButton btn = new JButton("Atualizar");
        btn.addActionListener(e -> {
            txt.setText("");
            for (Produto pr : dao.listarProdutos()) {
                txt.append(String.format("%-25s | Qtd:%3d | R$ %8.2f%n",
                        pr.getNome(), pr.getQuantidade(), pr.getValor()));
            }
        });

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(txt), BorderLayout.CENTER);
        p.add(btn, BorderLayout.SOUTH);
        return p;
    }

    /* ---------------- listar funcionários --------------- */
    private JPanel abaListarFuncionarios() {
        JTextArea txt = new JTextArea(); txt.setEditable(false);
        JButton btn = new JButton("Atualizar");
        btn.addActionListener(e -> {
            txt.setText("");
            for (Funcionario f : dao.listarFuncionarios()) {
                txt.append(String.format("ID %d | %-20s | R$ %.2f%n",
                        f.getId(), f.getNome(), f.getSalario()));
            }
        });

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(txt), BorderLayout.CENTER);
        p.add(btn, BorderLayout.SOUTH);
        return p;
    }

    /* ---------------- sorteio --------------------------- */
    private JPanel abaSorteio() {
        JTextArea txt = new JTextArea(); txt.setEditable(false);
        JButton btn = new JButton("Sortear");
        btn.addActionListener(e -> {
            ClienteEspecial c = dao.realizarSorteio();
            txt.setText("");
            if (c != null) {
                txt.append(String.format("Sorteado → ID %d | %s%n",
                        c.getIdCliente(), c.getNome()));
            }
        });

        JPanel p = new JPanel(new BorderLayout());
        p.add(new JScrollPane(txt), BorderLayout.CENTER);
        p.add(btn, BorderLayout.SOUTH);
        return p;
    }

    /* ---------------- reajuste -------------------------- */
    private JPanel abaReajuste() {
        JPanel p = new JPanel(new GridLayout(3,2));
        JTextField cat = new JTextField(), per = new JTextField();

        p.add(new JLabel("Categoria:")); p.add(cat);
        p.add(new JLabel("Percentual %:")); p.add(per);

        JButton btn = new JButton("Aplicar");
        btn.addActionListener(e -> {
            try {
                double pct = Double.parseDouble(per.getText());
                if (pct < 0) throw new NumberFormatException();
                dao.reajustarSalario(cat.getText(), pct);
                cat.setText(""); per.setText("");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Percentual inválido.");
            }
        });
        p.add(new JLabel()); p.add(btn);
        return p;
    }
}
