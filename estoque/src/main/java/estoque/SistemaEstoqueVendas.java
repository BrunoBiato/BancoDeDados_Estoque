package estoque;

import javax.swing.*;
import java.awt.*;

public class SistemaEstoqueVendas extends JFrame {
    private Usuario usuarioLogado;

    public SistemaEstoqueVendas(Usuario usuarioLogado) {
        this.usuarioLogado = usuarioLogado;
        setTitle("Sistema de Estoque e Vendas - Usuário: " + usuarioLogado.getNome());
        setSize(1200, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(1, 2));

        TelaEstoque telaEstoque = new TelaEstoque(usuarioLogado);
        TelaVendas telaVendas = new TelaVendas(usuarioLogado);

        add(telaEstoque.getContentPane());
        add(telaVendas.getContentPane());

        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new TelaLogin().setVisible(true);
        });
    }
}