package com.spaceinvaders.gui;

import javax.swing.*;

import com.spaceinvaders.dao.JogadorDAO;
import com.spaceinvaders.entidades.Barreira;
import com.spaceinvaders.entidades.Invasor;
import com.spaceinvaders.entidades.Nave;
import com.spaceinvaders.entidades.Projetil;
import com.spaceinvaders.modelo.Jogador;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class PainelJogo extends JPanel implements Runnable {

    private final int LARGURA_TELA = 800;
    private final int ALTURA_TELA = 600;

    private Thread gameLoop;
    private boolean emExecucao = false;

    private Jogador jogador;
    private JogadorDAO jogadorDAO;
    private Nave nave;
    private List<Invasor> invasores;
    private List<Barreira> barreiras;
    private Projetil projetilJogador;
    private List<Projetil> projeteisInvasores;
    private Image imagemFundo;

    private int pontuacao;
    private int vidas;
    private int nivel;
    private int direcaoInvasores = 1;
    private long ultimoTiroInvasor = 0;
    private long intervaloTiroInvasor = 1000;
    private Random random = new Random();

    public PainelJogo(Jogador jogador) {
        this.jogador = jogador;
        this.jogadorDAO = new JogadorDAO();

        setPreferredSize(new Dimension(LARGURA_TELA, ALTURA_TELA));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(new TecladoAdapter());
        
        try {
            ImageIcon ii = new ImageIcon(getClass().getResource("/imagens/FundoEspaco.png"));
            this.imagemFundo = ii.getImage();
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem de fundo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void addNotify() {
        super.addNotify();
        iniciarJogo();
    }

    private void iniciarJogo() {
        pontuacao = 0;
        vidas = 3;
        nivel = 1;
        
        barreiras = new ArrayList<>();
        criarBarreiras();
        iniciarNivel();

        emExecucao = true;
        gameLoop = new Thread(this);
        gameLoop.start();
    }

    private void iniciarNivel() {
        intervaloTiroInvasor = Math.max(200, 1000 - (nivel * 50));
        
        nave = new Nave(LARGURA_TELA / 2 - 25, ALTURA_TELA - 70);
        invasores = new ArrayList<>();
        projeteisInvasores = new ArrayList<>();
        projetilJogador = null;
        direcaoInvasores = 1;

        // Mantém 4 fileiras, substituindo uma de Invasor2.png por Invasor4.png
        int[] pontosPorLinha = {30, 20, 10, 10};
        String[] imagensPorLinha = {"Invasor1.png", "Invasor2.png", "Invasor4.png", "Invasor3.png"};

        for (int linha = 0; linha < 4; linha++) { // Volta para 4 fileiras
            for (int col = 0; col < 8; col++) {
                int yOffset = ((nivel - 1) / 5) * 20;
                invasores.add(new Invasor(100 + col * 60, 50 + yOffset + linha * 45, pontosPorLinha[linha], imagensPorLinha[linha]));
            }
        }
    }

    private void criarBarreiras() {
        barreiras.clear();
        int larguraTotalBarreira = 8 * Barreira.LARGURA;
        int espacoDisponivel = LARGURA_TELA - (2 * 100);
        int espacoEntreBarreiras = (espacoDisponivel - (4 * larguraTotalBarreira)) / 3;

        for (int i = 0; i < 4; i++) {
            int xInicial = 100 + i * (larguraTotalBarreira + espacoEntreBarreiras);
            int yInicial = ALTURA_TELA - 200;
            for (int linha = 0; linha < 4; linha++) {
                for (int col = 0; col < 8; col++) {
                    if (linha >= 2 && (col >= 2 && col <= 5)) {
                        continue;
                    }
                    barreiras.add(new Barreira(xInicial + col * Barreira.LARGURA, yInicial + linha * Barreira.ALTURA));
                }
            }
        }
    }

    @Override
    public void run() {
        long ultimoTempo = System.nanoTime();
        final double ns = 1000000000.0 / 60.0;
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
        repaint();
    }

    private void atualizar() {
        if (vidas <= 0) {
            emExecucao = false;
            return;
        }

        nave.atualizar(LARGURA_TELA);
        
        if (projetilJogador != null) {
            projetilJogador.atualizar();
            if (!projetilJogador.isVisivel()) projetilJogador = null;
        }
        
        projeteisInvasores.removeIf(p -> !p.isVisivel());
        projeteisInvasores.forEach(Projetil::atualizar);

        float velocidadeHorizontal = 1.0f + (nivel * 0.2f);
        
        boolean bordaAlcancada = false;
        for (Invasor inv : invasores) {
            if (inv.isVisivel() && ((inv.getX() >= LARGURA_TELA - Invasor.LARGURA && direcaoInvasores > 0) || (inv.getX() <= 0 && direcaoInvasores < 0))) {
                bordaAlcancada = true;
                break;
            }
        }

        if (bordaAlcancada) {
            direcaoInvasores *= -1;
            invasores.forEach(inv -> inv.mover(0, 20));
        } else {
            invasores.forEach(inv -> inv.mover((int)(direcaoInvasores * velocidadeHorizontal), 0));
        }

        long tempoAtual = System.currentTimeMillis();
        if (tempoAtual - ultimoTiroInvasor > intervaloTiroInvasor) {
            List<Invasor> invasoresVisiveis = invasores.stream().filter(Invasor::isVisivel).collect(Collectors.toList());
            if (!invasoresVisiveis.isEmpty()) {
                Invasor atirador = invasoresVisiveis.get(random.nextInt(invasoresVisiveis.size()));
                projeteisInvasores.add(new Projetil(atirador.getX() + Invasor.LARGURA / 2, atirador.getY() + Invasor.ALTURA, false));
                ultimoTiroInvasor = tempoAtual;
            }
        }
        
        verificarColisoes();

        if (invasores.stream().noneMatch(Invasor::isVisivel)) {
            nivel++;
            criarBarreiras();
            iniciarNivel();
        }
    }
    
    private void verificarColisoes() {
        // Colisão: projétil do jogador com invasor ou barreira
        if (projetilJogador != null) {
            boolean alvoRemovido = false; 
            // Com invasor
            for (Invasor inv : invasores) {
                if (inv.isVisivel() && projetilJogador.getBounds().intersects(inv.getBounds())) {
                    inv.setVisivel(false);
                    projetilJogador.setVisivel(false);
                    pontuacao += inv.getPontos();
                    alvoRemovido = true; 
                    break;
                }
            }
            // Com barreira
            if (!alvoRemovido) { 
                for (Barreira b : barreiras) {
                    if (b.isVisivel() && projetilJogador.getBounds().intersects(b.getBounds())) {
                        b.setVisivel(false);
                        projetilJogador.setVisivel(false);
                        break;
                    }
                }
            }
        }

        // Colisão: projéteis dos invasores com nave ou barreira
        for (Projetil p : projeteisInvasores) {
            if (p.isVisivel()) {
                boolean hit = false;
                // Com nave
                if (p.getBounds().intersects(new Rectangle(nave.getX(), nave.getY(), Nave.LARGURA, Nave.ALTURA))) {
                    p.setVisivel(false);
                    vidas--;
                    hit = true;
                }
                // Com barreira
                if (!hit) {
                    for (Barreira b : barreiras) {
                        if (b.isVisivel() && p.getBounds().intersects(b.getBounds())) {
                            p.setVisivel(false);
                            b.setVisivel(false);
                            break;
                        }
                    }
                }
            }
        }

        // Colisão: invasores com a nave ou chegando ao fundo
        for (Invasor inv : invasores) {
             if (inv.isVisivel() && (inv.getBounds().intersects(new Rectangle(nave.getX(), nave.getY(), Nave.LARGURA, Nave.ALTURA)) || inv.getY() > ALTURA_TELA - 100)) {
                 vidas = 0;
                 return;
             }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        if (imagemFundo != null) {
            g2d.drawImage(imagemFundo, 0, 0, LARGURA_TELA, ALTURA_TELA, null);
        } else {
             g2d.setColor(Color.BLACK);
             g2d.fillRect(0, 0, LARGURA_TELA, ALTURA_TELA);
        }

        nave.desenhar(g2d);
        if (projetilJogador != null) projetilJogador.desenhar(g2d);
        projeteisInvasores.forEach(p -> p.desenhar(g2d));
        invasores.forEach(inv -> inv.desenhar(g2d));
        barreiras.forEach(b -> b.desenhar(g2d));

        desenharHUD(g2d);

        if (!emExecucao && vidas <= 0) {
            fimDeJogo(g2d);
        }
    }

    private void desenharHUD(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Pontuação: " + pontuacao, 10, 20);
        g.drawString("Vidas: " + vidas, LARGURA_TELA - 100, 20);
        g.drawString("Nível: " + nivel, LARGURA_TELA / 2 - 100, 20);
        g.drawString("Recorde: " + Math.max(jogador.getPontuacaoMaxima(), pontuacao), LARGURA_TELA / 2 + 20, 20);
    }
    
    private void fimDeJogo(Graphics2D g) {
        if (pontuacao > jogador.getPontuacaoMaxima()) {
            jogadorDAO.atualizarPontuacaoMaxima(jogador.getId(), pontuacao);
            jogador.setPontuacaoMaxima(pontuacao);
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
            int codigo = e.getKeyCode();
            
            if (emExecucao) {
                 nave.teclaPressionada(e);
                if (codigo == KeyEvent.VK_SPACE && projetilJogador == null) {
                    projetilJogador = nave.atirar();
                }
            } else {
                if (codigo == KeyEvent.VK_ENTER) {
                    iniciarJogo();
                } else if (codigo == KeyEvent.VK_ESCAPE) {
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

