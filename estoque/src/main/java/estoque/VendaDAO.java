package estoque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class VendaDAO {
    private static final String SQL_INSERT = "INSERT INTO vendas (produto_id, quantidade, valor_total, data_venda, vendedor_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_SELECT_ALL = "SELECT * FROM vendas";
    private static final String SQL_SELECT_BY_PERIOD = "SELECT * FROM vendas WHERE data_venda BETWEEN ? AND ?";

    public void registrarVenda(Venda venda) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setInt(1, venda.getProdutoId());
            pstmt.setInt(2, venda.getQuantidade());
            pstmt.setDouble(3, venda.getValorTotal());
            pstmt.setTimestamp(4, new Timestamp(venda.getDataVenda().getTime()));
            pstmt.setInt(5, venda.getVendedorId());
            pstmt.executeUpdate();
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    venda.setId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public List<Venda> listarVendas() throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
            while (rs.next()) {
                vendas.add(criarVendaDoResultSet(rs));
            }
        }
        return vendas;
    }

    public List<Venda> listarVendasPorPeriodo(Date dataInicio, Date dataFim) throws SQLException {
        List<Venda> vendas = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_PERIOD)) {
            pstmt.setTimestamp(1, new Timestamp(dataInicio.getTime()));
            pstmt.setTimestamp(2, new Timestamp(dataFim.getTime()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    vendas.add(criarVendaDoResultSet(rs));
                }
            }
        }
        return vendas;
    }

    private Venda criarVendaDoResultSet(ResultSet rs) throws SQLException {
        return new Venda(
            rs.getInt("id"),
            rs.getInt("produto_id"),
            rs.getInt("quantidade"),
            rs.getDouble("valor_total"),
            rs.getTimestamp("data_venda"),
            rs.getInt("vendedor_id")
        );
    }
}