package estoque;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RelatorioDAO {
    private Connection getConnection() throws SQLException {
        return DatabaseConnection.getConnection();
    }

    public String gerarRelatorioVendasDiario(Date data) throws SQLException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dataStr = sdf.format(data);
        String sql = "SELECT p.nome, SUM(v.quantidade) as total_vendido, SUM(v.valor_total) as valor_total " +
                     "FROM vendas v JOIN produtos p ON v.produto_id = p.id " +
                     "WHERE DATE(v.data_venda) = ? " +
                     "GROUP BY p.id";
        
        StringBuilder relatorio = new StringBuilder("Relatório de Vendas - " + dataStr + "\n\n");
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dataStr);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    relatorio.append("Produto: ").append(rs.getString("nome"))
                             .append(", Quantidade Vendida: ").append(rs.getInt("total_vendido"))
                             .append(", Valor Total: R$").append(String.format("%.2f", rs.getDouble("valor_total")))
                             .append("\n");
                }
            }
        }
        
        return relatorio.toString();
    }
}