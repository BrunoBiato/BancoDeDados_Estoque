package estoque;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;
import java.util.Date;

public class TelaVendas extends JFrame {
    private VendaDAO vendaDAO;
    private ProdutoDAO produtoDAO;
    private JComboBox<Produto> cbProdutos;
    private JTextField txtQuantidade;
    private JTextArea txtAreaResultados;
    private Usuario usuarioLogado;

    public TelaVendas(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        vendaDAO = new VendaDAO();
        produtoDAO = new ProdutoDAO();
        setTitle("Registrar Venda - Usuário: " + usuarioLogado.getNome());
        setSize(600, 400);
        setLayout(new BorderLayout());

        JPanel painelEntrada = new JPanel(new GridLayout(2, 2));
        painelEntrada.add(new JLabel("Produto:"));
        cbProdutos = new JComboBox<>();
        painelEntrada.add(cbProdutos);
        painelEntrada.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField(10);
        painelEntrada.add(txtQuantidade);

        JPanel painelBotoes = new JPanel();
        JButton btnRegistrarVenda = new JButton("Registrar Venda");
        JButton btnListarVendas = new JButton("Listar Vendas");
        painelBotoes.add(btnRegistrarVenda);
        painelBotoes.add(btnListarVendas);

        txtAreaResultados = new JTextArea(15, 40);
        txtAreaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaResultados);

        add(painelEntrada, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        btnRegistrarVenda.addActionListener(e -> registrarVenda());
        btnListarVendas.addActionListener(e -> listarVendas());

        carregarProdutos();
        setLocationRelativeTo(null);
    }

    private void carregarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            for (Produto produto : produtos) {
                cbProdutos.addItem(produto);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar produtos: " + e.getMessage());
        }
    }

    private void registrarVenda() {
        try {
            Produto produtoSelecionado = (Produto) cbProdutos.getSelectedItem();
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            
            if (produtoSelecionado.getQuantidade() < quantidade) {
                txtAreaResultados.setText("Quantidade insuficiente em estoque.");
                return;
            }
            
            double valorTotal = produtoSelecionado.getPreco() * quantidade;
            Venda venda = new Venda(0, produtoSelecionado.getId(), quantidade, valorTotal, new Date(), usuarioLogado.getId());
            vendaDAO.registrarVenda(venda);
            
            produtoSelecionado.setQuantidade(produtoSelecionado.getQuantidade() - quantidade);
            produtoDAO.atualizarProduto(produtoSelecionado);
            
            txtAreaResultados.setText("Venda registrada com sucesso!\n" + venda);
            limparCampos();
        } catch (SQLException | NumberFormatException ex) {
            txtAreaResultados.setText("Erro ao registrar venda: " + ex.getMessage());
        }
    }

    private void listarVendas() {
        try {
            List<Venda> vendas = vendaDAO.listarVendas();
            StringBuilder sb = new StringBuilder();
            for (Venda v : vendas) {
                sb.append(v.toString()).append("\n");
            }
            txtAreaResultados.setText(sb.toString());
        } catch (SQLException ex) {
            txtAreaResultados.setText("Erro ao listar vendas: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        cbProdutos.setSelectedIndex(0);
        txtQuantidade.setText("");
    }
}