package estoque;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TelaGerenciarProdutos extends JFrame {
    private JTable tabelaProdutos;
    private DefaultTableModel modeloTabela;
    private ProdutoDAO produtoDAO;

    public TelaGerenciarProdutos() {
        produtoDAO = new ProdutoDAO();
        setTitle("Gerenciar Produtos");
        setSize(600, 400);
        setLocationRelativeTo(null);

        modeloTabela = new DefaultTableModel();
        modeloTabela.addColumn("ID");
        modeloTabela.addColumn("Nome");
        modeloTabela.addColumn("Preço");
        modeloTabela.addColumn("Quantidade");

        tabelaProdutos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaProdutos);

        JPanel botoesPanel = new JPanel();
        JButton adicionarButton = new JButton("Adicionar");
        JButton editarButton = new JButton("Editar");
        JButton excluirButton = new JButton("Excluir");

        botoesPanel.add(adicionarButton);
        botoesPanel.add(editarButton);
        botoesPanel.add(excluirButton);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);
        add(botoesPanel, BorderLayout.SOUTH);

        adicionarButton.addActionListener(e -> adicionarProduto());
        editarButton.addActionListener(e -> editarProduto());
        excluirButton.addActionListener(e -> excluirProduto());

        carregarProdutos();
    }

    private void carregarProdutos() {
        modeloTabela.setRowCount(0);
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            for (Produto produto : produtos) {
                modeloTabela.addRow(new Object[]{
                    produto.getId(),
                    produto.getNome(),
                    produto.getPreco(),
                    produto.getQuantidade()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void adicionarProduto() {
        JTextField nomeField = new JTextField();
        JTextField precoField = new JTextField();
        JTextField quantidadeField = new JTextField();

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("Nome:"));
        panel.add(nomeField);
        panel.add(new JLabel("Preço:"));
        panel.add(precoField);
        panel.add(new JLabel("Quantidade:"));
        panel.add(quantidadeField);

        int result = JOptionPane.showConfirmDialog(null, panel, "Adicionar Produto",
            JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if (result == JOptionPane.OK_OPTION) {
            try {
                String nome = nomeField.getText();
                double preco = Double.parseDouble(precoField.getText());
                int quantidade = Integer.parseInt(quantidadeField.getText());

                Produto novoProduto = new Produto(nome, preco, quantidade);
                produtoDAO.adicionarProduto(novoProduto);
                carregarProdutos();
                JOptionPane.showMessageDialog(this, "Produto adicionado com sucesso!");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Por favor, insira valores válidos para preço e quantidade.");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao adicionar produto: " + e.getMessage());
            }
        }
    }

    private void editarProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um produto para editar.");
            return;
        }

        int id = (int) modeloTabela.getValueAt(selectedRow, 0);
        try {
            Produto produto = produtoDAO.buscarProdutoPorId(id);
            if (produto != null) {
                JTextField nomeField = new JTextField(produto.getNome());
                JTextField precoField = new JTextField(String.valueOf(produto.getPreco()));
                JTextField quantidadeField = new JTextField(String.valueOf(produto.getQuantidade()));

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Nome:"));
                panel.add(nomeField);
                panel.add(new JLabel("Preço:"));
                panel.add(precoField);
                panel.add(new JLabel("Quantidade:"));
                panel.add(quantidadeField);

                int result = JOptionPane.showConfirmDialog(null, panel, "Editar Produto",
                    JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                
                if (result == JOptionPane.OK_OPTION) {
                    produto.setNome(nomeField.getText());
                    produto.setPreco(Double.parseDouble(precoField.getText()));
                    produto.setQuantidade(Integer.parseInt(quantidadeField.getText()));

                    produtoDAO.atualizarProduto(produto);
                    carregarProdutos();
                    JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
                }
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao editar produto: " + e.getMessage());
        }
    }

    private void excluirProduto() {
        int selectedRow = tabelaProdutos.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um produto para excluir.");
            return;
        }

        int id = (int) modeloTabela.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja excluir este produto?",
            "Confirmar Exclusão", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                produtoDAO.deletarProduto(id);
                carregarProdutos();
                JOptionPane.showMessageDialog(this, "Produto excluído com sucesso!");
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Erro ao excluir produto: " + e.getMessage());
            }
        }
    }
}