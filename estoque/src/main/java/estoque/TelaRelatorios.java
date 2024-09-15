package estoque;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

public class TelaRelatorios extends JFrame {
    private JTabbedPane tabbedPane;
    private ProdutoDAO produtoDAO;
    private VendaDAO vendaDAO;

    public TelaRelatorios() {
        produtoDAO = new ProdutoDAO();
        vendaDAO = new VendaDAO();
        setTitle("Relatórios");
        setSize(800, 600);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Estoque", criarPainelEstoque());
        tabbedPane.addTab("Vendas", criarPainelVendas());

        add(tabbedPane);
    }

    private JPanel criarPainelEstoque() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Preço");
        modeloTabela.addColumn("Quantidade");

        JTable tabelaEstoque = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaEstoque);
        panel.add(scrollPane, BorderLayout.CENTER);

        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            for (Produto produto : produtos) {
                modeloTabela.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    String.format("R$ %.2f", produto.getPreco()),
                    produto.getQuantidade()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar relatório de estoque: " + e.getMessage());
        }

        return panel;
    }

    private JPanel criarPainelVendas() {
        JPanel panel = new JPanel(new BorderLayout());
        DefaultTableModel modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Produto");
        modeloTabela.addColumn("Quantidade");
        modeloTabela.addColumn("Valor Total");
        modeloTabela.addColumn("Data da Venda");

        JTable tabelaVendas = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaVendas);
        panel.add(scrollPane, BorderLayout.CENTER);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        try {
            List<Venda> vendas = vendaDAO.listarVendas();
            for (Venda venda : vendas) {
                Produto produto = produtoDAO.buscarProdutoPorId(venda.getProdutoId());
                modeloTabela.addRow(new Object[]{
                    venda.getId(),
                    produto != null ? produto.getNome() : "Produto não encontrado",
                    venda.getQuantidade(),
                    String.format("R$ %.2f", venda.getValorTotal()),
                    sdf.format(venda.getDataVenda())
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar relatório de vendas: " + e.getMessage());
        }

        return panel;
    }
}