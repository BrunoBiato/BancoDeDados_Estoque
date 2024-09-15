package estoque;

import javax.swing.*;
import java.awt.*;

public class TelaPrincipal extends JFrame {
    private Usuario usuarioLogado;

    public TelaPrincipal(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        
        setTitle("Sistema de Estoque - Bem-vindo, " + usuarioLogado.getNome());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel welcomePanel = new JPanel();
        JLabel welcomeLabel = new JLabel("Bem-vindo ao Sistema de Estoque, " + usuarioLogado.getNome() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));
        welcomePanel.add(welcomeLabel);
        mainPanel.add(welcomePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnGerenciarProdutos = new JButton("Gerenciar Produtos");
        JButton btnRegistrarVenda = new JButton("Registrar Venda");
        JButton btnRelatorios = new JButton("Relatórios");
        JButton btnGerenciarUsuarios = new JButton("Gerenciar Usuários");

        buttonPanel.add(btnGerenciarProdutos);
        buttonPanel.add(btnRegistrarVenda);
        buttonPanel.add(btnRelatorios);
        buttonPanel.add(btnGerenciarUsuarios);

        mainPanel.add(buttonPanel, BorderLayout.CENTER);

        add(mainPanel);

        btnGerenciarProdutos.addActionListener(e -> abrirTelaEstoque());
        btnRegistrarVenda.addActionListener(e -> abrirTelaVendas());
        btnRelatorios.addActionListener(e -> abrirTelaRelatorios());
        btnGerenciarUsuarios.addActionListener(e -> abrirTelaUsuarios());
    }

    private void abrirTelaEstoque() {
        SwingUtilities.invokeLater(() -> {
            TelaGerenciarProdutos telaEstoque = new TelaGerenciarProdutos();
            telaEstoque.setVisible(true);
        });
    }

    private void abrirTelaVendas() {
        SwingUtilities.invokeLater(() -> {
            TelaVendas telaVendas = new TelaVendas(usuarioLogado);
            telaVendas.setVisible(true);
        });
    }

    private void abrirTelaRelatorios() {
        SwingUtilities.invokeLater(() -> {
            TelaRelatorios telaRelatorios = new TelaRelatorios();
            telaRelatorios.setVisible(true);
        });
    }

    private void abrirTelaUsuarios() {
        if (usuarioLogado.getTipo() == TipoUsuario.ADMIN) {
            SwingUtilities.invokeLater(() -> {
                TelaGerenciarUsuarios telaGerenciarUsuarios = new TelaGerenciarUsuarios();
                telaGerenciarUsuarios.setVisible(true);
            });
        } else {
            JOptionPane.showMessageDialog(this, 
                "Acesso negado. Apenas administradores podem gerenciar usuários.",
                "Acesso Restrito", 
                JOptionPane.WARNING_MESSAGE);
        }
    }
}