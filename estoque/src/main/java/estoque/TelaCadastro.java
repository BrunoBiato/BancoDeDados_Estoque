package estoque;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;

public class TelaCadastro extends JFrame {
    private JTextField txtNome, txtUsername;
    private JPasswordField txtSenha;
    private JComboBox<TipoUsuario> cbTipoUsuario;
    private JButton btnCadastrar;
    private UsuarioDAO usuarioDAO;

    public TelaCadastro() {
        usuarioDAO = new UsuarioDAO();
        setTitle("Cadastro de Usuário");
        setSize(300, 250);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2));

        add(new JLabel("Nome:"));
        txtNome = new JTextField();
        add(txtNome);

        add(new JLabel("Username:"));
        txtUsername = new JTextField();
        add(txtUsername);

        add(new JLabel("Senha:"));
        txtSenha = new JPasswordField();
        add(txtSenha);

        add(new JLabel("Tipo de Usuário:"));
        cbTipoUsuario = new JComboBox<>(TipoUsuario.values());
        add(cbTipoUsuario);

        btnCadastrar = new JButton("Cadastrar");
        btnCadastrar.addActionListener(e -> realizarCadastro());
        add(btnCadastrar);

        setLocationRelativeTo(null);
    }

    private void realizarCadastro() {
        String nome = txtNome.getText();
        String username = txtUsername.getText();
        String senha = new String(txtSenha.getPassword());
        TipoUsuario tipo = (TipoUsuario) cbTipoUsuario.getSelectedItem();
        
        if (nome.isEmpty() || username.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos!");
            return;
        }
        
        try {
            Usuario novoUsuario = new Usuario(nome, username, senha, tipo);
            usuarioDAO.cadastrarUsuario(novoUsuario);
            JOptionPane.showMessageDialog(this, "Usuário cadastrado com sucesso!");
            this.dispose();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar usuário: " + ex.getMessage());
        }
    }
}