package com.spaceinvaders.gui;

import com.spaceinvaders.modelo.Jogador;
import com.spaceinvaders.util.FonteUtil;

import javax.swing.*;
import java.awt.*;

public class TelaPerfil extends JFrame {

    public TelaPerfil(Jogador jogador, JFrame telaDeOrigem) {
        super("Space Invaders - Perfil");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 350);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel painel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon ii = new ImageIcon(getClass().getResource("/imagens/FundoEspaco.png"));
                g.drawImage(ii.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        setContentPane(painel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridwidth = 2;
        gbc.gridx = 0;

        Font fonteTitulo = FonteUtil.getFonte(28f);
        Font fonteTexto = FonteUtil.getFonte(16f);
        
        JLabel labelTitulo = new JLabel("PERFIL", SwingConstants.CENTER);
        labelTitulo.setFont(fonteTitulo);
        labelTitulo.setForeground(Color.YELLOW);
        gbc.gridy = 0;
        painel.add(labelTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel labelNome = new JLabel("Nome:");
        labelNome.setFont(fonteTexto);
        labelNome.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        painel.add(labelNome, gbc);

        JLabel valorNome = new JLabel(jogador.getNome());
        valorNome.setFont(fonteTexto);
        valorNome.setForeground(Color.CYAN);
        gbc.gridx = 1;
        gbc.gridy = 1;
        painel.add(valorNome, gbc);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(fonteTexto);
        labelEmail.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        painel.add(labelEmail, gbc);

        JLabel valorEmail = new JLabel(jogador.getEmail());
        valorEmail.setFont(fonteTexto);
        valorEmail.setForeground(Color.CYAN);
        gbc.gridx = 1;
        gbc.gridy = 2;
        painel.add(valorEmail, gbc);

        JLabel labelRecorde = new JLabel("Recorde:");
        labelRecorde.setFont(fonteTexto);
        labelRecorde.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        painel.add(labelRecorde, gbc);

        JLabel valorRecorde = new JLabel(String.valueOf(jogador.getPontuacaoMaxima()));
        valorRecorde.setFont(fonteTexto);
        valorRecorde.setForeground(Color.CYAN);
        gbc.gridx = 1;
        gbc.gridy = 3;
        painel.add(valorRecorde, gbc);

        JLabel labelPartidas = new JLabel("Partidas Jogadas:");
        labelPartidas.setFont(fonteTexto);
        labelPartidas.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 4;
        painel.add(labelPartidas, gbc);
        
        JLabel valorPartidas = new JLabel(String.valueOf(jogador.getPartidasJogadas()));
        valorPartidas.setFont(fonteTexto);
        valorPartidas.setForeground(Color.CYAN);
        gbc.gridx = 1;
        gbc.gridy = 4;
        painel.add(valorPartidas, gbc);

        JLabel labelInimigos = new JLabel("Inimigos Destruídos:");
        labelInimigos.setFont(fonteTexto);
        labelInimigos.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 5;
        painel.add(labelInimigos, gbc);

        JLabel valorInimigos = new JLabel(String.valueOf(jogador.getInimigosDestruidos()));
        valorInimigos.setFont(fonteTexto);
        valorInimigos.setForeground(Color.CYAN);
        gbc.gridx = 1;
        gbc.gridy = 5;
        painel.add(valorInimigos, gbc);

        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.setFont(fonteTexto);
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        painel.add(botaoVoltar, gbc);

        botaoVoltar.addActionListener(e -> {
            dispose();
            telaDeOrigem.setVisible(true);
        });

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                telaDeOrigem.setVisible(true);
            }
        });
    }
}
