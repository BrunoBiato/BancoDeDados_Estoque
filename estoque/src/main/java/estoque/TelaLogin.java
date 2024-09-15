package estoque;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class TelaLogin extends JFrame {
    private JTextField txtUsuario;
    private JPasswordField txtSenha;
    private JButton btnLogin;
    private JButton btnCadastrar;

    public TelaLogin() {
        setTitle("Login - Sistema de Estoque");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Usuário:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        txtUsuario = new JTextField(15);
        panel.add(txtUsuario, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Senha:"), gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        txtSenha = new JPasswordField(15);
        panel.add(txtSenha, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.NONE;
        btnLogin = new JButton("Login");
        panel.add(btnLogin, gbc);

        gbc.gridy = 3;
        btnCadastrar = new JButton("Cadastrar");
        panel.add(btnCadastrar, gbc);

        add(panel);

        btnLogin.addActionListener(e -> realizarLogin());
        btnCadastrar.addActionListener(e -> abrirTelaCadastro());
    }

    private void realizarLogin() {
        String username = txtUsuario.getText();
        String senha = new String(txtSenha.getPassword());

        try {
            UsuarioDAO usuarioDAO = new UsuarioDAO();
            Usuario usuario = usuarioDAO.autenticarUsuario(username, senha);

            if (usuario != null) {
                JOptionPane.showMessageDialog(this, "Login realizado com sucesso!");
                
                SwingUtilities.invokeLater(() -> {
                    TelaPrincipal telaPrincipal = new TelaPrincipal(usuario);
                    telaPrincipal.setVisible(true);
                });
                
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Usuário ou senha inválidos!", "Erro de Login", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao realizar login: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirTelaCadastro() {
        SwingUtilities.invokeLater(() -> {
            new TelaCadastro().setVisible(true);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaLogin().setVisible(true));
    }
}