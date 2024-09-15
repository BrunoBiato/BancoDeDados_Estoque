package estoque;

import javax.swing.*;
import java.awt.*;
import java.sql.SQLException;
import java.util.List;

public class TelaGerenciarUsuarios extends JFrame {
    private UsuarioDAO usuarioDAO;
    private JTextField txtId, txtNome, txtUsername;
    private JPasswordField txtPassword;
    private JComboBox<TipoUsuario> cbTipoUsuario;
    private JTextArea txtAreaResultados;

    public TelaGerenciarUsuarios() {
        usuarioDAO = new UsuarioDAO();
        setTitle("Gerenciar Usuários");
        setSize(600, 500);
        setLayout(new BorderLayout());

        JPanel painelEntrada = new JPanel(new GridLayout(5, 2));
        painelEntrada.add(new JLabel("ID:"));
        txtId = new JTextField(10);
        painelEntrada.add(txtId);
        painelEntrada.add(new JLabel("Nome:"));
        txtNome = new JTextField(20);
        painelEntrada.add(txtNome);
        painelEntrada.add(new JLabel("Username:"));
        txtUsername = new JTextField(20);
        painelEntrada.add(txtUsername);
        painelEntrada.add(new JLabel("Senha:"));
        txtPassword = new JPasswordField(20);
        painelEntrada.add(txtPassword);
        painelEntrada.add(new JLabel("Tipo de Usuário:"));
        cbTipoUsuario = new JComboBox<>(TipoUsuario.values());
        painelEntrada.add(cbTipoUsuario);

        JPanel painelBotoes = new JPanel();
        JButton btnAdicionar = new JButton("Adicionar");
        JButton btnListar = new JButton("Listar");
        JButton btnAtualizar = new JButton("Atualizar");
        JButton btnRemover = new JButton("Remover");
        painelBotoes.add(btnAdicionar);
        painelBotoes.add(btnListar);
        painelBotoes.add(btnAtualizar);
        painelBotoes.add(btnRemover);

        txtAreaResultados = new JTextArea(15, 50);
        txtAreaResultados.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(txtAreaResultados);

        add(painelEntrada, BorderLayout.NORTH);
        add(painelBotoes, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        btnAdicionar.addActionListener(e -> adicionarUsuario());
        btnListar.addActionListener(e -> listarUsuarios());
        btnAtualizar.addActionListener(e -> atualizarUsuario());
        btnRemover.addActionListener(e -> removerUsuario());

        setLocationRelativeTo(null);
    }

    private void adicionarUsuario() {
        try {
            String nome = txtNome.getText();
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            TipoUsuario tipo = (TipoUsuario) cbTipoUsuario.getSelectedItem();
            Usuario novoUsuario = new Usuario(nome, username, password, tipo);
            usuarioDAO.cadastrarUsuario(novoUsuario);
            txtAreaResultados.setText("Usuário adicionado com sucesso!");
            limparCampos();
        } catch (SQLException ex) {
            txtAreaResultados.setText("Erro ao adicionar usuário: " + ex.getMessage());
        }
    }

    private void listarUsuarios() {
        try {
            List<Usuario> usuarios = usuarioDAO.listarTodos();
            StringBuilder sb = new StringBuilder();
            for (Usuario u : usuarios) {
                sb.append(u.toString()).append("\n");
            }
            txtAreaResultados.setText(sb.toString());
        } catch (SQLException ex) {
            txtAreaResultados.setText("Erro ao listar usuários: " + ex.getMessage());
        }
    }

    private void atualizarUsuario() {
        try {
            int id = Integer.parseInt(txtId.getText());
            String nome = txtNome.getText();
            String username = txtUsername.getText();
            String password = new String(txtPassword.getPassword());
            TipoUsuario tipo = (TipoUsuario) cbTipoUsuario.getSelectedItem();
            Usuario usuario = new Usuario(id, nome, username, password, tipo);
            usuarioDAO.atualizarUsuario(usuario);
            txtAreaResultados.setText("Usuário atualizado com sucesso!");
            limparCampos();
        } catch (SQLException | NumberFormatException ex) {
            txtAreaResultados.setText("Erro ao atualizar usuário: " + ex.getMessage());
        }
    }

    private void removerUsuario() {
        try {
            int id = Integer.parseInt(txtId.getText());
            usuarioDAO.deletarUsuario(id);
            txtAreaResultados.setText("Usuário removido com sucesso!");
            limparCampos();
        } catch (SQLException | NumberFormatException ex) {
            txtAreaResultados.setText("Erro ao remover usuário: " + ex.getMessage());
        }
    }

    private void limparCampos() {
        txtId.setText("");
        txtNome.setText("");
        txtUsername.setText("");
        txtPassword.setText("");
        cbTipoUsuario.setSelectedIndex(0);
    }
}