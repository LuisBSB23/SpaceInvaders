package com.spaceinvaders.gui;

import javax.swing.*;

import com.spaceinvaders.dao.JogadorDAO;
import com.spaceinvaders.entidades.*;
import com.spaceinvaders.modelo.Jogador;
import com.spaceinvaders.util.FonteUtil;
import com.spaceinvaders.util.SomUtil;

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

    // --- NOVOS ATRIBUTOS DE ESTADO E ELEMENTOS ---
    private enum EstadoJogo { JOGANDO, PAUSADO, FIM_DE_JOGO, BATALHA_CHEFE }
    private EstadoJogo estadoAtual;
    
    private Thread gameLoop;

    private Jogador jogador;
    private JogadorDAO jogadorDAO;
    private Nave nave;
    private List<Invasor> invasores;
    private List<Barreira> barreiras;
    private Projetil projetilJogador;
    private List<Projetil> projeteisInvasores;
    private Image imagemFundo;
    private List<PowerUp> powerUps;
    private Boss boss;

    private int pontuacao;
    private int vidas;
    private int nivel;
    private int inimigosDestruidosPartida;
    private int direcaoInvasores = 1;
    private long ultimoTiroInvasor = 0;
    private long intervaloTiroInvasor;
    private Random random = new Random();

    private long tempoDanoJogador = 0; // Para feedback visual
    private final long DURACAO_FLASH_DANO = 150; // em ms

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
        inimigosDestruidosPartida = 0;
        
        barreiras = new ArrayList<>();
        powerUps = new ArrayList<>();
        criarBarreiras();
        iniciarNivel();

        estadoAtual = EstadoJogo.JOGANDO;
        SomUtil.tocarMusicaFundo("musica_fundo.wav");

        gameLoop = new Thread(this);
        gameLoop.start();
    }

    private void iniciarNivel() {
        boss = null;
        powerUps.clear();
        estadoAtual = (nivel % 3 == 0) ? EstadoJogo.BATALHA_CHEFE : EstadoJogo.JOGANDO;
        
        intervaloTiroInvasor = Math.max(200, 1000 - (nivel * 50));
        
        nave = new Nave(LARGURA_TELA / 2 - 25, ALTURA_TELA - 70);
        invasores = new ArrayList<>();
        projeteisInvasores = new ArrayList<>();
        projetilJogador = null;
        direcaoInvasores = 1;

        if (estadoAtual == EstadoJogo.BATALHA_CHEFE) {
            boss = new Boss(LARGURA_TELA / 2 - 75, 50, 50 * nivel);
        } else {
            int[] pontosPorLinha = {30, 20, 10, 10};
            String[] imagensPorLinha = {"Invasor1.png", "Invasor2.png", "Invasor4.png", "Invasor3.png"};
            for (int linha = 0; linha < 4; linha++) {
                for (int col = 0; col < 8; col++) {
                    int yOffset = ((nivel - 1) / 5) * 20;
                    invasores.add(new Invasor(100 + col * 60, 50 + yOffset + linha * 45, pontosPorLinha[linha], imagensPorLinha[linha]));
                }
            }
        }
    }

    private void criarBarreiras() {
        barreiras.clear();
        int numBarreiras = 4;
        int larguraBarreira = 8 * Barreira.LARGURA;
        int espacoTotal = LARGURA_TELA - 200;
        int espacoEntre = (espacoTotal - (numBarreiras * larguraBarreira)) / (numBarreiras - 1);
        
        for (int i = 0; i < numBarreiras; i++) {
            int xInicial = 100 + i * (larguraBarreira + espacoEntre);
            int yInicial = ALTURA_TELA - 200;
            for (int linha = 0; linha < 4; linha++) {
                for (int col = 0; col < 8; col++) {
                    if (linha >= 2 && (col >= 2 && col <= 5)) continue;
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

        while (estadoAtual != null) {
            long agora = System.nanoTime();
            delta += (agora - ultimoTempo) / ns;
            ultimoTempo = agora;

            while (delta >= 1) {
                if (estadoAtual == EstadoJogo.JOGANDO || estadoAtual == EstadoJogo.BATALHA_CHEFE) {
                    atualizar();
                }
                delta--;
            }
            repaint();
            if (estadoAtual == EstadoJogo.FIM_DE_JOGO) break;
        }
        repaint();
    }
    
    // --- LÓGICA DE ATUALIZAÇÃO PRINCIPAL DIVIDIDA ---
    private void atualizar() {
        if (vidas <= 0) {
            finalizarJogo();
            return;
        }

        nave.atualizar(LARGURA_TELA);
        atualizarProjeteis();
        atualizarPowerUps();

        if (estadoAtual == EstadoJogo.JOGANDO) {
            atualizarInvasores();
            verificarFimDeNivel();
        } else if (estadoAtual == EstadoJogo.BATALHA_CHEFE) {
            atualizarBoss();
            verificarFimDeNivel();
        }
        
        verificarColisoes();
    }
    
    private void atualizarInvasores() {
        // --- DIFICULDADE DINÂMICA ---
        long invasoresRestantes = invasores.stream().filter(Invasor::isVisivel).count();
        float fatorVelocidade = 1.0f + ( (invasores.size() - invasoresRestantes) * 0.05f);
        float velocidadeHorizontal = (1.0f + (nivel * 0.2f)) * fatorVelocidade;
        
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
    }
    
    private void atualizarBoss() {
        if (boss != null && boss.isVisivel()) {
            boss.atualizar(LARGURA_TELA);
            projeteisInvasores.addAll(boss.atirar());
        }
    }
    
    private void atualizarProjeteis() {
        if (projetilJogador != null) {
            projetilJogador.atualizar();
            if (!projetilJogador.isVisivel()) projetilJogador = null;
        }
        
        projeteisInvasores.removeIf(p -> !p.isVisivel());
        projeteisInvasores.forEach(Projetil::atualizar);

        long tempoAtual = System.currentTimeMillis();
        long invasoresRestantes = invasores.stream().filter(Invasor::isVisivel).count();
        long intervaloDinamico = Math.max(150, intervaloTiroInvasor - ( (invasores.size() - invasoresRestantes) * 10) );

        if (estadoAtual == EstadoJogo.JOGANDO && tempoAtual - ultimoTiroInvasor > intervaloDinamico) {
            List<Invasor> invasoresVisiveis = invasores.stream().filter(Invasor::isVisivel).collect(Collectors.toList());
            if (!invasoresVisiveis.isEmpty()) {
                Invasor atirador = invasoresVisiveis.get(random.nextInt(invasoresVisiveis.size()));
                projeteisInvasores.add(new Projetil(atirador.getX() + Invasor.LARGURA / 2, atirador.getY() + Invasor.ALTURA, false, Projetil.TipoProjetil.NORMAL));
                ultimoTiroInvasor = tempoAtual;
            }
        }
    }
    
    private void atualizarPowerUps() {
        powerUps.removeIf(p -> !p.isVisivel());
        powerUps.forEach(PowerUp::atualizar);
    }
    
    private void verificarFimDeNivel() {
        if (estadoAtual == EstadoJogo.JOGANDO && invasores.stream().noneMatch(Invasor::isVisivel)) {
            nivel++;
            criarBarreiras();
            iniciarNivel();
        } else if (estadoAtual == EstadoJogo.BATALHA_CHEFE && (boss == null || !boss.isVisivel())) {
            pontuacao += 500 * nivel; // Bônus por derrotar o chefe
            nivel++;
            criarBarreiras();
            iniciarNivel();
        }
    }

    private void verificarColisoes() {
        colisaoProjetilJogador();
        colisaoProjeteisInvasores();
        colisaoInvasorComNave();
        colisaoNaveComPowerUp();
    }

    private void colisaoProjetilJogador() {
        if (projetilJogador == null || !projetilJogador.isVisivel()) return;

        Rectangle projBounds = projetilJogador.getBounds();

        // Com invasores
        for (Invasor inv : invasores) {
            if (inv.isVisivel() && projBounds.intersects(inv.getBounds())) {
                inv.setVisivel(false);
                pontuacao += inv.getPontos();
                inimigosDestruidosPartida++;
                SomUtil.tocarSom("explosao.wav");
                tentarGerarPowerUp(inv.getX(), inv.getY());
                if (projetilJogador.getTipo() != Projetil.TipoProjetil.FORTE) {
                    projetilJogador.setVisivel(false);
                }
                return; // Projétil some ou continua
            }
        }
        
        // Com chefe
        if (boss != null && boss.isVisivel() && projBounds.intersects(boss.getBounds())) {
            boss.levarDano(projetilJogador.getTipo() == Projetil.TipoProjetil.FORTE ? 3 : 1);
             SomUtil.tocarSom("explosao.wav");
            if (!boss.isVisivel()) {
                 inimigosDestruidosPartida++;
            }
            projetilJogador.setVisivel(false);
            return;
        }

        // Com barreiras
        for (Barreira b : barreiras) {
            if (b.isVisivel() && projBounds.intersects(b.getBounds())) {
                b.setVisivel(false);
                projetilJogador.setVisivel(false);
                return;
            }
        }
    }

    private void colisaoProjeteisInvasores() {
        Rectangle naveBounds = new Rectangle(nave.getX(), nave.getY(), Nave.LARGURA, Nave.ALTURA);
        for (Projetil p : projeteisInvasores) {
            if (p.isVisivel()) {
                // Com a nave
                if (p.getBounds().intersects(naveBounds)) {
                    p.setVisivel(false);
                    if (nave.temEscudo()) {
                        nave.ativarEscudo(); // Apenas reinicia o timer
                    } else {
                        vidas--;
                        SomUtil.tocarSom("dano.wav");
                        tempoDanoJogador = System.currentTimeMillis();
                    }
                    continue;
                }
                // Com barreiras
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

    private void colisaoInvasorComNave() {
        Rectangle naveBounds = new Rectangle(nave.getX(), nave.getY(), Nave.LARGURA, Nave.ALTURA);
        for (Invasor inv : invasores) {
            if (inv.isVisivel() && (inv.getBounds().intersects(naveBounds) || inv.getY() > ALTURA_TELA - 100)) {
                vidas = 0;
                return;
            }
        }
    }
    
    private void colisaoNaveComPowerUp() {
         Rectangle naveBounds = new Rectangle(nave.getX(), nave.getY(), Nave.LARGURA, Nave.ALTURA);
         for (PowerUp pu : powerUps) {
             if (pu.isVisivel() && pu.getBounds().intersects(naveBounds)) {
                 pu.setVisivel(false);
                 SomUtil.tocarSom("powerup.wav");
                 if (pu.getTipo() == PowerUp.TipoPowerUp.ESCUDO) {
                     nave.ativarEscudo();
                 } else if (pu.getTipo() == PowerUp.TipoPowerUp.TIRO_FORTE) {
                     nave.ativarTiroForte();
                 }
             }
         }
    }
    
    private void tentarGerarPowerUp(int x, int y) {
        if (random.nextInt(100) < 15) { // 15% de chance
            PowerUp.TipoPowerUp tipo = random.nextBoolean() ? PowerUp.TipoPowerUp.ESCUDO : PowerUp.TipoPowerUp.TIRO_FORTE;
            powerUps.add(new PowerUp(x, y, tipo));
        }
    }

    private void finalizarJogo() {
        estadoAtual = EstadoJogo.FIM_DE_JOGO;
        SomUtil.pararMusicaFundo();
        if (pontuacao > jogador.getPontuacaoMaxima()) {
            jogadorDAO.atualizarPontuacaoMaxima(jogador.getId(), pontuacao);
            jogador.setPontuacaoMaxima(pontuacao);
        }
        jogadorDAO.atualizarEstatisticas(jogador.getId(), inimigosDestruidosPartida);
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
        
        // --- FEEDBACK VISUAL DE DANO ---
        if (System.currentTimeMillis() - tempoDanoJogador < DURACAO_FLASH_DANO) {
            g2d.setColor(new Color(255, 0, 0, 100));
            g2d.fillRect(0, 0, LARGURA_TELA, ALTURA_TELA);
        }

        if (estadoAtual != EstadoJogo.FIM_DE_JOGO) {
            nave.desenhar(g2d);
            if (projetilJogador != null) projetilJogador.desenhar(g2d);
            projeteisInvasores.forEach(p -> p.desenhar(g2d));
            invasores.forEach(inv -> inv.desenhar(g2d));
            barreiras.forEach(b -> b.desenhar(g2d));
            powerUps.forEach(pu -> pu.desenhar(g2d));
            if (boss != null) boss.desenhar(g2d);
        }

        desenharHUD(g2d);

        if (estadoAtual == EstadoJogo.PAUSADO) {
            desenharTelaPausa(g2d);
        } else if (estadoAtual == EstadoJogo.FIM_DE_JOGO) {
            desenharTelaFimDeJogo(g2d);
        }
    }

    private void desenharHUD(Graphics2D g) {
        g.setColor(Color.WHITE);
        g.setFont(FonteUtil.getFonte(16f));
        g.drawString("Pontuação: " + pontuacao, 10, 20);
        g.drawString("Vidas: " + vidas, LARGURA_TELA - 100, 20);
        g.drawString("Nível: " + nivel, LARGURA_TELA / 2 - 100, 20);
        g.drawString("Recorde: " + Math.max(jogador.getPontuacaoMaxima(), pontuacao), LARGURA_TELA / 2 + 20, 20);
    }
    
    private void desenharTelaPausa(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, LARGURA_TELA, ALTURA_TELA);
        
        String msg = "PAUSADO";
        g.setColor(Color.YELLOW);
        g.setFont(FonteUtil.getFonte(50f));
        FontMetrics fm = g.getFontMetrics();
        int x = (LARGURA_TELA - fm.stringWidth(msg)) / 2;
        int y = (ALTURA_TELA / 2);
        g.drawString(msg, x, y);
    }
    
    private void desenharTelaFimDeJogo(Graphics2D g) {
        String msg = "Fim de Jogo";
        g.setColor(Color.RED);
        g.setFont(FonteUtil.getFonte(50f));
        FontMetrics fm = g.getFontMetrics();
        int x = (LARGURA_TELA - fm.stringWidth(msg)) / 2;
        int y = (ALTURA_TELA / 2);
        g.drawString(msg, x, y);
        
        g.setFont(FonteUtil.getFonte(20f));
        g.setColor(Color.WHITE);
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
            
            if (estadoAtual == EstadoJogo.JOGANDO || estadoAtual == EstadoJogo.BATALHA_CHEFE) {
                nave.teclaPressionada(e);
                if (codigo == KeyEvent.VK_SPACE && projetilJogador == null) {
                    projetilJogador = nave.atirar();
                    SomUtil.tocarSom("tiro.wav");
                }
                if (codigo == KeyEvent.VK_P) {
                    estadoAtual = EstadoJogo.PAUSADO;
                }
            } else if (estadoAtual == EstadoJogo.PAUSADO) {
                 if (codigo == KeyEvent.VK_P) {
                    estadoAtual = (boss != null && boss.isVisivel()) ? EstadoJogo.BATALHA_CHEFE : EstadoJogo.JOGANDO;
                }
            }
            else if (estadoAtual == EstadoJogo.FIM_DE_JOGO) {
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
             if (estadoAtual == EstadoJogo.JOGANDO || estadoAtual == EstadoJogo.BATALHA_CHEFE) {
                nave.teclaLiberada(e);
             }
        }
    }
}
