package estoque;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.mindrot.jbcrypt.BCrypt;

public class UsuarioDAO {
    private static final String SQL_INSERT = "INSERT INTO usuarios (nome, username, senha, tipo_usuario) VALUES (?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE usuarios SET nome = ?, username = ?, senha = ?, tipo_usuario = ? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM usuarios WHERE id = ?";
    private static final String SQL_SELECT_ALL = "SELECT * FROM usuarios";
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM usuarios WHERE id = ?";
    private static final String SQL_SELECT_BY_USERNAME = "SELECT * FROM usuarios WHERE username = ?";
    private static final String SQL_CHECK_USERNAME = "SELECT COUNT(*) FROM usuarios WHERE username = ?";

    public void cadastrarUsuario(Usuario usuario) throws SQLException {
        System.out.println("Iniciando cadastro de usuário: " + usuario.getUsername());
        if (usernameExiste(usuario.getUsername())) {
            System.out.println("Nome de usuário já existe: " + usuario.getUsername());
            throw new SQLException("Nome de usuário já existe. Por favor, escolha outro.");
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getUsername());
            String senhaHash = BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt());
            pstmt.setString(3, senhaHash);
            pstmt.setString(4, usuario.getTipo().name());
            
            System.out.println("Executando query de inserção...");
            int affectedRows = pstmt.executeUpdate();
            System.out.println("Linhas afetadas: " + affectedRows);
            
            if (affectedRows == 0) {
                throw new SQLException("Falha ao criar usuário, nenhuma linha afetada.");
            }
            
            try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    usuario.setId(generatedKeys.getInt(1));
                    System.out.println("Usuário cadastrado com sucesso. ID: " + usuario.getId());
                } else {
                    throw new SQLException("Falha ao criar usuário, nenhum ID obtido.");
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao cadastrar usuário: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    public Usuario autenticarUsuario(String username, String senha) throws SQLException {
        System.out.println("Tentando autenticar usuário: " + username);
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_USERNAME)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String senhaHash = rs.getString("senha");
                    if (BCrypt.checkpw(senha, senhaHash)) {
                        Usuario usuario = new Usuario(
                            rs.getInt("id"),
                            rs.getString("nome"),
                            rs.getString("username"),
                            senhaHash,
                            TipoUsuario.valueOf(rs.getString("tipo_usuario"))
                        );
                        System.out.println("Usuário autenticado com sucesso: " + username);
                        return usuario;
                    } else {
                        System.out.println("Senha incorreta para o usuário: " + username);
                    }
                } else {
                    System.out.println("Usuário não encontrado: " + username);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    private boolean usernameExiste(String username) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_CHECK_USERNAME)) {
            pstmt.setString(1, username);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void atualizarUsuario(Usuario usuario) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getUsername());
            pstmt.setString(3, BCrypt.hashpw(usuario.getSenha(), BCrypt.gensalt()));
            pstmt.setString(4, usuario.getTipo().name());
            pstmt.setInt(5, usuario.getId());
            pstmt.executeUpdate();
        }
    }

    public void deletarUsuario(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public Usuario buscarUsuarioPorId(int id) throws SQLException {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_BY_ID)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("username"),
                        rs.getString("senha"),
                        TipoUsuario.valueOf(rs.getString("tipo_usuario"))
                    );
                }
            }
        }
        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(SQL_SELECT_ALL)) {
            while (rs.next()) {
                usuarios.add(new Usuario(
                    rs.getInt("id"),
                    rs.getString("nome"),
                    rs.getString("username"),
                    rs.getString("senha"),
                    TipoUsuario.valueOf(rs.getString("tipo_usuario"))
                ));
            }
        }
        return usuarios;
    }
}