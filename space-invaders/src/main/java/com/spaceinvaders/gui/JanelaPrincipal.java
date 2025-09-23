package com.spaceinvaders.gui;

import javax.swing.*;

import com.spaceinvaders.modelo.Jogador;

public class JanelaPrincipal extends JFrame {

    public JanelaPrincipal(Jogador jogador) {
        setTitle("Space Invaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        add(new PainelJogo(jogador));

        pack(); // Ajusta o tamanho da janela ao do painel
        setLocationRelativeTo(null); // Centraliza na tela
    }
}
