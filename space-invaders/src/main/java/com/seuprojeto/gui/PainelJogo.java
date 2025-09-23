package com.seuprojeto.gui;

import com.seuprojeto.dao.JogadorDAO;
import com.seuprojeto.entidades.Invasor;
import com.seuprojeto.entidades.Nave;
import com.seuprojeto.entidades.Projetil;
import com.seuprojeto.modelo.Jogador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PainelJogo extends JPanel implements Runnable {

    private final int LARGURA_TELA = 800;
    private final int ALTURA_TELA = 600;

    private Thread gameLoop;
    private boolean emExecucao = false;

    private Jogador jogador;
    private JogadorDAO jogadorDAO;
    private Nave nave;
    private List<Invasor> invasores;
    private Projetil projetilJogador;
    private List<Projetil> projeteisInvasores;

    private int pontuacao;
    private int vidas;
    private int direcaoInvasores = 1; // 1 para direita, -1 para esquerda
    private long ultimoTiroInvasor = 0;
    private long intervaloTiroInvasor = 1000; // 1 segundo
    private Random random = new Random();

    public PainelJogo(Jogador jogador) {
        this.jogador = jogador;
        this.jogadorDAO = new JogadorDAO();

        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TecladoAdapter());
    }

    @Override
    public void addNotify() {
        super.addNotify();
        iniciarJogo();
    }

    private void iniciarJogo() {
        emExecucao = true;
        pontuacao = 0;
        vidas = 3;
        nave = new Nave(LARGURA_TELA / 2 - 25, ALTURA_TELA - 70);
        invasores = new ArrayList<>();
        projeteisInvasores = new ArrayList<>();

        // Cria a grade de invasores
        for (int linha = 0; linha < 4; linha++) {
            for (int col = 0; col < 8; col++) {
                invasores.add(new Invasor(100 + col * 60, 50 + linha * 50));
            }
        }

        gameLoop = new Thread(this);
        gameLoop.start();
    }

    @Override
    public void run() {
        long ultimoTempo = System.nanoTime();
        final double ns = 1000000000.0 / 60.0; // 60 FPS
        double delta = 0;

        while (emExecucao) {
            long agora = System.nanoTime();
            delta += (agora - ultimoTempo) / ns;
            ultimoTempo = agora;

            while (delta >= 1) {
                atualizar();
                delta--;
            }
            repaint();
        }
    }

    private void atualizar() {
        if (vidas <= 0) {
            emExecucao = false;
        }

        nave.atualizar(LARGURA_TELA);
        
        // Atualiza projétil do jogador
        if (projetilJogador != null) {
            projetilJogador.atualizar();
            if (!projetilJogador.isVisivel()) {
                projetilJogador = null;
            }
        }
        
        // Atualiza projéteis dos invasores
        projeteisInvasores.removeIf(p -> !p.isVisivel());
        for (Projetil p : projeteisInvasores) {
            p.atualizar();
        }

        // Movimento dos invasores
        boolean bordaAlcancada = false;
        for (Invasor inv : invasores) {
            if (inv.isVisivel()) {
                if (inv.getX() >= LARGURA_TELA - Invasor.LARGURA && direcaoInvasores == 1) {
                    bordaAlcancada = true;
                }
                if (inv.getX() <= 0 && direcaoInvasores == -1) {
                    bordaAlcancada = true;
                }
            }
        }

        if (bordaAlcancada) {
            direcaoInvasores *= -1;
            for (Invasor inv : invasores) {
                inv.mover(0, 20); // Desce
            }
        } else {
            for (Invasor inv : invasores) {
                inv.mover(direcaoInvasores, 0); // Move horizontalmente
            }
        }

        // Invasores atiram
        long tempoAtual = System.currentTimeMillis();
        if (tempoAtual - ultimoTiroInvasor > intervaloTiroInvasor) {
            List<Invasor> invasoresAtivos = new ArrayList<>();
            for(Invasor i : invasores) {
                if(i.isVisivel()) invasoresAtivos.add(i);
            }

            if (!invasoresAtivos.isEmpty()) {
                Invasor atirador = invasoresAtivos.get(random.nextInt(invasoresAtivos.size()));
                projeteisInvasores.add(new Projetil(atirador.getX() + Invasor.LARGURA / 2, atirador.getY() + Invasor.ALTURA, false));
                ultimoTiroInvasor = tempoAtual;
            }
        }
        
        verificarColisoes();

        // Condição de vitória (nível)
        if (invasores.stream().noneMatch(Invasor::isVisivel)) {
            // Em uma implementação mais complexa, aqui você iria para o próximo nível.
            // Por simplicidade, vamos terminar o jogo como vitória.
            emExecucao = false;
        }
    }
    
    private void verificarColisoes() {
        // Colisão: projétil do jogador com invasor
        if (projetilJogador != null) {
            for (Invasor inv : invasores) {
                if (inv.isVisivel() && projetilJogador.getBounds().intersects(inv.getBounds())) {
                    inv.setVisivel(false);
                    projetilJogador.setVisivel(false);
                    projetilJogador = null;
                    pontuacao += 10;
                    break;
                }
            }
        }

        // Colisão: projétil do invasor com a nave
        for (Projetil p : projeteisInvasores) {
            if (p.isVisivel() && p.getBounds().intersects(new Rectangle(nave.getX(), nave.getY(), Nave.LARGURA, Nave.ALTURA))) {
                p.setVisivel(false);
                vidas--;
                // Poderia adicionar um efeito de explosão ou invencibilidade temporária
            }
        }

        // Colisão: invasores com a nave ou chegando ao fundo
        for (Invasor inv : invasores) {
             if (inv.isVisivel() && (inv.getBounds().intersects(new Rectangle(nave.getX(), nave.getY(), Nave.LARGURA, Nave.ALTURA)) || inv.getY() > ALTURA_TELA - 80)) {
                 vidas = 0; // Fim de jogo imediato
                 return;
             }
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        nave.desenhar(g2d);

        if (projetilJogador != null) {
            projetilJogador.desenhar(g2d);
        }
        
        for (Projetil p : projeteisInvasores) {
            p.desenhar(g2d);
        }

        for (Invasor inv : invasores) {
            inv.desenhar(g2d);
        }

        desenharHUD(g2d);

        if (!emExecucao) {
            fimDeJogo(g2d);
        }
    }

    private void desenharHUD(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Pontuação: " + pontuacao, 10, 20);
        g.drawString("Vidas: " + vidas, LARGURA_TELA - 100, 20);
        g.drawString("Recorde: " + jogador.getPontuacaoMaxima(), LARGURA_TELA / 2 - 50, 20);
    }
    
    private void fimDeJogo(Graphics2D g) {
        // Salva a pontuação se for um novo recorde
        if (pontuacao > jogador.getPontuacaoMaxima()) {
            jogadorDAO.atualizarPontuacaoMaxima(jogador.getId(), pontuacao);
            jogador.setPontuacaoMaxima(pontuacao); // Atualiza o objeto local também
        }

        String msg = "Fim de Jogo";
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 50));
        FontMetrics fm = g.getFontMetrics();
        int x = (LARGURA_TELA - fm.stringWidth(msg)) / 2;
        int y = (ALTURA_TELA / 2);
        g.drawString(msg, x, y);
        
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        String msgPontuacao = "Sua pontuação final: " + pontuacao;
        x = (LARGURA_TELA - g.getFontMetrics().stringWidth(msgPontuacao)) / 2;
        g.drawString(msgPontuacao, x, y + 50);

        String msgReiniciar = "Pressione 'Enter' para jogar novamente ou 'Esc' para sair.";
        x = (LARGURA_TELA - g.getFontMetrics().stringWidth(msgReiniciar)) / 2;
        g.drawString(msgReiniciar, x, y + 100);
    }

    private class TecladoAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            nave.teclaPressionada(e);

            int codigo = e.getKeyCode();
            if (codigo == KeyEvent.VK_SPACE && projetilJogador == null) {
                projetilJogador = nave.atirar();
            }

            if (!emExecucao) {
                if (codigo == KeyEvent.VK_ENTER) {
                    iniciarJogo();
                } else if (codigo == KeyEvent.VK_ESCAPE) {
                    // Fecha a janela do jogo e volta para a tela de login
                    SwingUtilities.getWindowAncestor(PainelJogo.this).dispose();
                    new TelaLogin().setVisible(true);
                }
            }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            nave.teclaLiberada(e);
        }
    }
}
