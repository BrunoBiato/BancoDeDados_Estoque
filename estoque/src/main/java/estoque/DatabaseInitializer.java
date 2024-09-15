package estoque;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;

public class DatabaseInitializer {
    public static void initializeDatabase() {
        System.out.println("Iniciando inicialização do banco de dados...");
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {
            
            createTable(stmt, "usuarios",
                "CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(100) NOT NULL," +
                "username VARCHAR(50) NOT NULL UNIQUE," +
                "senha VARCHAR(255) NOT NULL," +
                "tipo_usuario VARCHAR(20) NOT NULL)");

            createTable(stmt, "produtos",
                "CREATE TABLE IF NOT EXISTS produtos (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(100) NOT NULL," +
                "preco DECIMAL(10, 2) NOT NULL," +
                "quantidade INT NOT NULL)");

            createTable(stmt, "vendas",
                "CREATE TABLE IF NOT EXISTS vendas (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "produto_id INT," +
                "quantidade INT," +
                "valor_total DECIMAL(10, 2)," +
                "data_venda TIMESTAMP," +
                "vendedor_id INT," +
                "FOREIGN KEY (produto_id) REFERENCES produtos(id)," +
                "FOREIGN KEY (vendedor_id) REFERENCES usuarios(id))");

            createTable(stmt, "alteracoes_produto",
                "CREATE TABLE IF NOT EXISTS alteracoes_produto (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "produto_id INT," +
                "campo_alterado VARCHAR(50)," +
                "valor_antigo VARCHAR(255)," +
                "valor_novo VARCHAR(255)," +
                "data_alteracao TIMESTAMP," +
                "usuario_id INT," +
                "FOREIGN KEY (produto_id) REFERENCES produtos(id)," +
                "FOREIGN KEY (usuario_id) REFERENCES usuarios(id))");

            System.out.println("Banco de dados inicializado com sucesso.");
            
            verifyTable(conn, "usuarios");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTable(Statement stmt, String tableName, String sql) {
        try {
            stmt.execute(sql);
            System.out.println("Tabela " + tableName + " criada ou já existente.");
        } catch (SQLException e) {
            System.out.println("Erro ao criar tabela " + tableName + ": " + e.getMessage());
        }
    }

    private static void verifyTable(Connection conn, String tableName) {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName + " LIMIT 1")) {
            System.out.println("Tabela " + tableName + " existe e pode ser consultada.");
        } catch (SQLException e) {
            System.out.println("Erro ao consultar tabela " + tableName + ": " + e.getMessage());
        }
    }
}