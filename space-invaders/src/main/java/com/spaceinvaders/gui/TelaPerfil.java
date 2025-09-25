package com.spaceinvaders.gui;

import com.spaceinvaders.modelo.Jogador;
import com.spaceinvaders.util.FonteUtil;

import javax.swing.*;
import java.awt.*;

public class TelaPerfil extends JFrame {

    public TelaPerfil(Jogador jogador, JFrame telaDeOrigem) {
        super("Space Invaders - Perfil");

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // --- AJUSTE 3: TAMANHO DA JANELA E FONTE ---
        setSize(600, 450); // Aumentado a largura para caber todo o texto
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
        
        Font fonteTitulo = FonteUtil.getFonte(28f);
        Font fonteTexto = FonteUtil.getFonte(14f); // Reduzido o tamanho para melhor encaixe
        
        // --- AJUSTE 3: LAYOUT ---
        // Título centralizado
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(10, 10, 20, 10); // Margem inferior maior para o título
        JLabel labelTitulo = new JLabel("PERFIL", SwingConstants.CENTER);
        labelTitulo.setFont(fonteTitulo);
        labelTitulo.setForeground(Color.YELLOW);
        painel.add(labelTitulo, gbc);
        
        // Resetando para layout em duas colunas e margens padrão
        gbc.gridwidth = 1;
        gbc.insets = new Insets(10, 10, 10, 10);

        // Linha Nome
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel labelNome = new JLabel("Nome:");
        labelNome.setFont(fonteTexto);
        labelNome.setForeground(Color.WHITE);
        painel.add(labelNome, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel valorNome = new JLabel(jogador.getNome());
        valorNome.setFont(fonteTexto);
        valorNome.setForeground(Color.CYAN);
        painel.add(valorNome, gbc);

        // Linha Email
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(fonteTexto);
        labelEmail.setForeground(Color.WHITE);
        painel.add(labelEmail, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel valorEmail = new JLabel(jogador.getEmail());
        valorEmail.setFont(fonteTexto);
        valorEmail.setForeground(Color.CYAN);
        painel.add(valorEmail, gbc);

        // Linha Recorde
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel labelRecorde = new JLabel("Recorde:");
        labelRecorde.setFont(fonteTexto);
        labelRecorde.setForeground(Color.WHITE);
        painel.add(labelRecorde, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel valorRecorde = new JLabel(String.valueOf(jogador.getPontuacaoMaxima()));
        valorRecorde.setFont(fonteTexto);
        valorRecorde.setForeground(Color.CYAN);
        painel.add(valorRecorde, gbc);

        // Linha Partidas
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel labelPartidas = new JLabel("Partidas Jogadas:");
        labelPartidas.setFont(fonteTexto);
        labelPartidas.setForeground(Color.WHITE);
        painel.add(labelPartidas, gbc);
        
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel valorPartidas = new JLabel(String.valueOf(jogador.getPartidasJogadas()));
        valorPartidas.setFont(fonteTexto);
        valorPartidas.setForeground(Color.CYAN);
        painel.add(valorPartidas, gbc);

        // Linha Inimigos
        gbc.gridy = 5;
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.EAST;
        JLabel labelInimigos = new JLabel("Inimigos Destruídos:");
        labelInimigos.setFont(fonteTexto);
        labelInimigos.setForeground(Color.WHITE);
        painel.add(labelInimigos, gbc);

        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        JLabel valorInimigos = new JLabel(String.valueOf(jogador.getInimigosDestruidos()));
        valorInimigos.setFont(fonteTexto);
        valorInimigos.setForeground(Color.CYAN);
        painel.add(valorInimigos, gbc);

        // Botão Voltar
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.setFont(fonteTexto);
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
