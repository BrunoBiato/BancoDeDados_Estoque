package estoque;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TelaEstoque extends JFrame {
    private ProdutoDAO produtoDAO;
    private JTextField txtId, txtNome, txtPreco, txtQuantidade;
    private JTextArea txtAreaResultados;
    private Usuario usuarioLogado;

    public TelaEstoque(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        produtoDAO = new ProdutoDAO();
        setTitle("Gerenciar Produtos - Usuário: " + usuarioLogado.getNome());
        setSize(600, 500);
        setLayout(new BorderLayout());

        JPanel painelEntrada = new JPanel(new GridLayout(4, 2));
        painelEntrada.add(new JLabel("ID:"));
        txtId = new JTextField(10);
        painelEntrada.add(txtId);
        painelEntrada.add(new JLabel("Nome:"));
        txtNome = new JTextField(20);
        painelEntrada.add(txtNome);
        painelEntrada.add(new JLabel("Preço:"));
        txtPreco = new JTextField(10);
        painelEntrada.add(txtPreco);
        painelEntrada.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField(10);
        painelEntrada.add(txtQuantidade);

        JPanel painelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnListar = new JButton("Listar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");
        JButton btnBuscar = new JButton("Buscar");
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);
        painelBotoes.add(btnBuscar);

        txtAreaResultados = new JTextArea(15, 50);
        txtAreaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaResultados);

        add(painelEntrada, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarProduto());
        btnListar.addActionListener(e -> listarProdutos());
        btnAtualizar.addActionListener(e -> atualizarProduto());
        btnRemover.addActionListener(e -> removerProduto());
        btnBuscar.addActionListener(e -> buscarProduto());

        setLocationRelativeTo(null);
    }

    private void adicionarProduto() {
        try {
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            Produto novoProduto = new Produto(nome, preco, quantidade);
            produtoDAO.adicionarProduto(novoProduto);
            txtAreaResultados.setText("Produto adicionado com sucesso!");
            limparCampos();
        } catch (SQLException | NumberFormatException ex) {
            txtAreaResultados.setText("Erro ao adicionar produto: " + ex.getMessage());
        }
    }

    private void listarProdutos() {
        try {
            List<Produto> produtos = produtoDAO.listarTodos();
            StringBuilder sb = new StringBuilder();
            for (Produto p : produtos) {
                sb.append(p.toString()).append("\n");
            }
            txtAreaResultados.setText(sb.toString());
        } catch (SQLException ex) {
            txtAreaResultados.setText("Erro ao listar produtos: " + ex.getMessage());
        }
    }

    private void atualizarProduto() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nome = txtNome.getText();
            double preco = Double.parseDouble(txtPreco.getText());
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            Produto produto = new Produto(id, nome, preco, quantidade);
            produtoDAO.atualizarProduto(produto);
            txtAreaResultados.setText("Produto atualizado com sucesso!");
            limparCampos();
        } catch (SQLException | NumberFormatException ex) {
            txtAreaResultados.setText("Erro ao atualizar produto: " + ex.getMessage());
        }
    }

    private void removerProduto() {
        try {
            int id = Integer.parseInt(txtId.getText());
            produtoDAO.deletarProduto(id);
            txtAreaResultados.setText("Produto removido com sucesso!");
            limparCampos();
        } catch (SQLException | NumberFormatException ex) {
            txtAreaResultados.setText("Erro ao remover produto: " + ex.getMessage());
        }
    }

    private void buscarProduto() {
        try {
            int id = Integer.parseInt(txtId.getText());
            Produto produto = produtoDAO.buscarProdutoPorId(id);
            if (produto != null) {
                txtAreaResultados.setText(produto.toString());
                preencherCampos(produto);
            } else {
                txtAreaResultados.setText("Produto não encontrado.");
            }
        } catch (SQLException | NumberFormatException ex) {
            txtAreaResultados.setText("Erro ao buscar produto: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtPreco.setText("");
        txtQuantidade.setText("");
    }

    private void preencherCampos(Produto produto) {
        txtId.setText(String.valueOf(produto.getId()));
        txtNome.setText(produto.getNome());
        txtPreco.setText(String.valueOf(produto.getPreco()));
        txtQuantidade.setText(String.valueOf(produto.getQuantidade()));
    }
}