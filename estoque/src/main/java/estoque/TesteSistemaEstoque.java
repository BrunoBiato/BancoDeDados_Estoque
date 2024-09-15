package estoque;

import java.sql.SQLException;
import java.util.List;

public class TesteSistemaEstoque {
    private static ProdutoDAO produtoDAO = new ProdutoDAO();

    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        try {
            testarAdicaoProduto();
            testarListagemProdutos();
            testarAtualizacaoProduto();
            testarRemocaoProduto();
            testarBuscaProduto();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void testarAdicaoProduto() throws SQLException {
        System.out.println("Testando adição de produto...");
        Produto novoProduto = new Produto("Teste Produto", 100.00, 5);
        produtoDAO.adicionarProduto(novoProduto);
        System.out.println("Produto adicionado com ID: " + novoProduto.getId());
    }

    private static void testarListagemProdutos() throws SQLException {
        System.out.println("\nTestando listagem de produtos...");
        List<Produto> produtos = produtoDAO.listarTodos();
        for (Produto p : produtos) {
            System.out.println(p);
        }
    }

    private static void testarAtualizacaoProduto() throws SQLException {
        System.out.println("\nTestando atualização de produto...");
        List<Produto> produtos = produtoDAO.listarTodos();
        if (!produtos.isEmpty()) {
            Produto produtoParaAtualizar = produtos.get(0);
            produtoParaAtualizar.setPreco(produtoParaAtualizar.getPreco() + 10);
            produtoDAO.atualizarProduto(produtoParaAtualizar);
            System.out.println("Produto atualizado: " + produtoParaAtualizar);
        }
    }

    private static void testarRemocaoProduto() throws SQLException {
        System.out.println("\nTestando remoção de produto...");
        List<Produto> produtos = produtoDAO.listarTodos();
        if (!produtos.isEmpty()) {
            Produto produtoParaRemover = produtos.get(produtos.size() - 1);
            produtoDAO.deletarProduto(produtoParaRemover.getId());
            System.out.println("Produto removido com ID: " + produtoParaRemover.getId());
        }
    }

    private static void testarBuscaProduto() throws SQLException {
        System.out.println("\nTestando busca de produto...");
        List<Produto> produtos = produtoDAO.listarTodos();
        if (!produtos.isEmpty()) {
            int idParaBuscar = produtos.get(0).getId();
            Produto produtoEncontrado = produtoDAO.buscarProdutoPorId(idParaBuscar);
            System.out.println("Produto encontrado: " + produtoEncontrado);
        }
    }
}