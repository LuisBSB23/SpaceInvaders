package com.spaceinvaders;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.spaceinvaders.gui.TelaLogin;

/**
 * Classe principal que inicia a aplicação do jogo.
 */
public class Jogo {

    public static void main(String[] args) {
        // Tenta aplicar um Look and Feel mais moderno
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Não foi possível aplicar o Look and Feel do sistema.");
        }

        // Garante que a GUI seja criada na Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            TelaLogin telaLogin = new TelaLogin();
            telaLogin.setVisible(true);
        });
    }
}
