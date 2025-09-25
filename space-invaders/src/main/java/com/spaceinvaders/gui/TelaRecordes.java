package com.spaceinvaders.gui;

import com.spaceinvaders.dao.JogadorDAO;
import com.spaceinvaders.modelo.Jogador;
import com.spaceinvaders.util.FonteUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaRecordes extends JFrame {

    public TelaRecordes(JFrame telaDeOrigem) {
        super("Space Invaders - Recordes");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // --- AJUSTE 2: TAMANHO DA JANELA ---
        setSize(750, 450); // Aumentado para caber as novas colunas
        setLocationRelativeTo(null);
        setResizable(false);
        
        JPanel painel = new JPanel(new BorderLayout()) {
             @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon ii = new ImageIcon(getClass().getResource("/imagens/FundoEspaco.png"));
                g.drawImage(ii.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        painel.setOpaque(false);
        setContentPane(painel);


        JLabel labelTitulo = new JLabel("RECORDES", SwingConstants.CENTER);
        labelTitulo.setFont(FonteUtil.getFonte(32f));
        labelTitulo.setForeground(Color.YELLOW);
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        painel.add(labelTitulo, BorderLayout.NORTH);

        // --- AJUSTE 2: NOVAS COLUNAS ---
        String[] colunas = {"Posição", "Nome", "Pontuação", "Partidas", "Inimigos"};
        DefaultTableModel model = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JTable tabela = new JTable(model);
        tabela.setFont(FonteUtil.getFonte(14f));
        tabela.setForeground(Color.WHITE);
        tabela.setBackground(new Color(0, 0, 0, 180));
        tabela.setOpaque(false);
        ((JComponent)tabela.getDefaultRenderer(Object.class)).setOpaque(false);

        JogadorDAO jogadorDAO = new JogadorDAO();
        List<Jogador> topJogadores = jogadorDAO.getTopDezJogadores();

        int pos = 1;
        for (Jogador jogador : topJogadores) {
            // --- AJUSTE 2: ADICIONANDO NOVOS DADOS NA LINHA ---
            model.addRow(new Object[]{
                pos + "º", 
                jogador.getNome(), 
                jogador.getPontuacaoMaxima(), 
                jogador.getPartidasJogadas(), 
                jogador.getInimigosDestruidos()
            });
            pos++;
        }

        JScrollPane scrollPane = new JScrollPane(tabela);
        scrollPane.getViewport().setBackground(new Color(0,0,0,180));
        scrollPane.setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        painel.add(scrollPane, BorderLayout.CENTER);
        
        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.setFont(FonteUtil.getFonte(16f));
        JPanel painelBotao = new JPanel();
        painelBotao.setOpaque(false);
        painelBotao.add(botaoVoltar);
        painel.add(painelBotao, BorderLayout.SOUTH);

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
