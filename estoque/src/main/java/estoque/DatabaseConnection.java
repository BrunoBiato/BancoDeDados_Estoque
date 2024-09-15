package estoque;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://192.168.0.15:3306/estoque?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private static final String USER = "blackADM";
    private static final String PASSWORD = "BlackBDT@2021";

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Driver MySQL carregado com sucesso.");
            System.out.println("Tentando conectar a: " + URL);
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexão estabelecida com sucesso.");
            return conn;
        } catch (ClassNotFoundException e) {
            System.err.println("Erro ao carregar o driver MySQL: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Driver MySQL não encontrado", e);
        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            System.err.println("SQLState: " + e.getSQLState());
            System.err.println("Error Code: " + e.getErrorCode());
            e.printStackTrace();
            throw e;
        } catch (Exception e) {
            System.err.println("Erro inesperado: " + e.getMessage());
            e.printStackTrace();
            throw new SQLException("Erro inesperado ao conectar", e);
        }
    }
}