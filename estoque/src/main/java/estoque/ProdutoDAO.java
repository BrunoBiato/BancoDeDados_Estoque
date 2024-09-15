package estoque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoDAO {
    private static final String SQL_INSERT = "INSERT INTO produtos (nome, preco, quantidade) VALUES (?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE produtos SET nome = ?, preco = ?, quantidade = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM produtos WHERE id = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM produtos";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM produtos WHERE id = ?";
    private static final String SQL_UPDATE_STOCK = "UPDATE produtos SET quantidade = quantidade - ? WHERE id = ?";

    public void adicionarProduto(Produto produto) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setInt(3, produto.getQuantidade());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    produto.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Produto> listarTodos() throws SQLException {
        List<Produto> produtos = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
            while (rs.next()) {
                produtos.add(new Produto(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getDouble("preco"),
                    rs.getInt("quantidade")
                ));
            }
        }
        return produtos;
    }

    public void atualizarProduto(Produto produto) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {
            pstmt.setString(1, produto.getNome());
            pstmt.setDouble(2, produto.getPreco());
            pstmt.setInt(3, produto.getQuantidade());
            pstmt.setInt(4, produto.getId());
            pstmt.executeUpdate();
        }
    }

    public void deletarProduto(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Produto buscarProdutoPorId(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Produto(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getDouble("preco"),
                        rs.getInt("quantidade")
                    );
                }
            }
        }
        return null;
    }

    public void atualizarEstoqueAposVenda(int produtoId, int quantidadeVendida) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_STOCK)) {
            pstmt.setInt(1, quantidadeVendida);
            pstmt.setInt(2, produtoId);
            pstmt.executeUpdate();
        }
    }
}