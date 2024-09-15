package estoque;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class TelaRegistrarVenda extends JFrame {
    private JComboBox<Produto> produtoComboBox;
    private JTextField quantidadeField;
    private JLabel valorTotalLabel;
    private ProdutoDAO produtoDAO;
    private VendaDAO vendaDAO;
    private Usuario usuarioLogado;

    public TelaRegistrarVenda(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        produtoDAO = new ProdutoDAO();
        vendaDAO = new VendaDAO();
        setTitle("Registrar Venda");
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(0, 2, 5, 5));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panel.add(new JLabel("Produto:"));
        produtoComboBox = new JComboBox<>();
        panel.add(produtoComboBox);

        panel.add(new JLabel("Quantidade:"));
        quantidadeField = new JTextField();
        panel.add(quantidadeField);

        panel.add(new JLabel("Valor Total:"));
        valorTotalLabel = new JLabel("R$ 0.00");
        panel.add(valorTotalLabel);

        JButton registrarButton = new JButton("Registrar Venda");
        registrarButton.addActionListener(e -> registrarVenda());

        setLayout(new BorderLayout());
        add(panel, BorderLayout.CENTER);
        add(registrarButton, BorderLayout.SOUTH);

        carregarProdutos();

        produtoComboBox.addActionListener(e -> atualizarValorTotal());
        quantidadeField.addActionListener(e -> atualizarValorTotal());
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            for (Produto produto : produtos) {
                produtoComboBox.addItem(produto);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void atualizarValorTotal() {
        Produto produtoSelecionado = (Produto) produtoComboBox.getSelectedItem();
        if (produtoSelecionado != null) {
            try {
                int quantidade = Integer.parseInt(quantidadeField.getText());
                double valorTotal = produtoSelecionado.getPreco() * quantidade;
                valorTotalLabel.setText(String.format("R$ %.2f", valorTotal));
            } catch (NumberFormatException e) {
                valorTotalLabel.setText("R$ 0.00");
            }
        }
    }

    private void registrarVenda() {
        Produto produtoSelecionado = (Produto) produtoComboBox.getSelectedItem();
        if (produtoSelecionado == null) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um produto.");
            return;
        }

        try {
            int quantidade = Integer.parseInt(quantidadeField.getText());
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "A quantidade deve ser maior que zero.");
                return;
            }

            if (quantidade > produtoSelecionado.getQuantidade()) {
                JOptionPane.showMessageDialog(this, "Quantidade insuficiente em estoque.");
                return;
            }

            double valorTotal = produtoSelecionado.getPreco() * quantidade;
            Venda venda = new Venda(0, produtoSelecionado.getId(), quantidade, valorTotal, new Date(), usuarioLogado.getId());
            vendaDAO.registrarVenda(venda);

            produtoSelecionado.setQuantidade(produtoSelecionado.getQuantidade() - quantidade);
            produtoDAO.atualizarProduto(produtoSelecionado);

            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!");
            limparCampos();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Por favor, insira uma quantidade válida.");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao registrar venda: " + e.getMessage());
        }
    }

    private void limparCampos() {
        produtoComboBox.setSelectedIndex(0);
        quantidadeField.setText("");
        valorTotalLabel.setText("R$ 0.00");
    }
}