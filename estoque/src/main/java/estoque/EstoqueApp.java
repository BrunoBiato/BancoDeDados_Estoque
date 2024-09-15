package estoque;

import java.sql.SQLException;
import java.util.List;

public class EstoqueApp {
    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        ProdutoDAO produtoDAO = new ProdutoDAO();

        try {
           
            Produto novoProduto = new Produto("Notebook", 2500.00, 10);
            produtoDAO.adicionarProduto(novoProduto);
            System.out.println("Produto adicionado com sucesso!");

           
            List<Produto> produtos = produtoDAO.listarTodos();
            System.out.println("Lista de produtos:");
            for (Produto p : produtos) {
                System.out.println(p);
            }

           
            if (!produtos.isEmpty()) {
                Produto produtoParaAtualizar = produtos.get(0);
                produtoParaAtualizar.setPreco(2600.00);
                produtoDAO.atualizarProduto(produtoParaAtualizar);
                System.out.println("Produto atualizado com sucesso!");
            }

            
            if (!produtos.isEmpty()) {
                int idParaRemover = produtos.get(produtos.size() - 1).getId();
                produtoDAO.deletarProduto(idParaRemover);
                System.out.println("Produto removido com sucesso!");
            }

            
            produtos = produtoDAO.listarTodos();
            System.out.println("Lista atualizada de produtos:");
            for (Produto p : produtos) {
                System.out.println(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}