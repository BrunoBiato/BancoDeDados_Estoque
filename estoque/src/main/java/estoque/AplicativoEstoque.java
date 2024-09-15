package estoque;

import javax.swing.*;

public class AplicativoEstoque {
    public static void main(String[] args) {
        DatabaseInitializer.initializeDatabase();
        
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