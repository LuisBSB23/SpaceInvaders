package com.spaceinvaders.entidades;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Nave {
    private int x, y;
    private int velocidade;
    private boolean movendoParaEsquerda;
    private boolean movendoParaDireita;
    public static final int LARGURA = 50;
    public static final int ALTURA = 30;
    private Image imagem;

    // --- NOVOS ATRIBUTOS ---
    private boolean escudoAtivo = false;
    private long tempoInicioEscudo;
    private final long DURACAO_ESCUDO = 5000; // 5 segundos

    private boolean tiroForteAtivo = false;
    private long tempoInicioTiroForte;
    private final long DURACAO_TIRO_FORTE = 8000; // 8 segundos

    public Nave(int xInicial, int yInicial) {
        this.x = xInicial;
        this.y = yInicial;
        this.velocidade = 5;

        try {
            ImageIcon ii = new ImageIcon(getClass().getResource("/imagens/NavePrincipal.png"));
            this.imagem = ii.getImage().getScaledInstance(LARGURA, ALTURA, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem da nave: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void atualizar(int larguraTela) {
        if (movendoParaEsquerda && x > 0) {
            x -= velocidade;
        }
        if (movendoParaDireita && x < larguraTela - LARGURA) {
            x += velocidade;
        }
        
        // --- ATUALIZAÇÃO DOS POWER-UPS ---
        long tempoAtual = System.currentTimeMillis();
        if (escudoAtivo && tempoAtual - tempoInicioEscudo > DURACAO_ESCUDO) {
            escudoAtivo = false;
        }
        if (tiroForteAtivo && tempoAtual - tempoInicioTiroForte > DURACAO_TIRO_FORTE) {
            tiroForteAtivo = false;
        }
    }

    public void desenhar(Graphics g) {
        if (imagem != null) {
            g.drawImage(imagem, x, y, null);
        } else {
            g.setColor(java.awt.Color.GREEN);
            g.fillRect(x, y, LARGURA, ALTURA);
        }
        
        // --- DESENHAR ESCUDO ---
        if (escudoAtivo) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            g2d.setColor(Color.CYAN);
            g2d.fillOval(x - 10, y - 10, LARGURA + 20, ALTURA + 20);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }
    }

    public Projetil atirar() {
        Projetil.TipoProjetil tipo = tiroForteAtivo ? Projetil.TipoProjetil.FORTE : Projetil.TipoProjetil.NORMAL;
        return new Projetil(x + LARGURA / 2, y, true, tipo);
    }
    
    // --- MÉTODOS PARA POWER-UPS E DANO ---
    public void ativarEscudo() {
        this.escudoAtivo = true;
        this.tempoInicioEscudo = System.currentTimeMillis();
    }
    
    public void ativarTiroForte() {
        this.tiroForteAtivo = true;
        this.tempoInicioTiroForte = System.currentTimeMillis();
    }
    
    public boolean temEscudo() {
        return escudoAtivo;
    }

    public void teclaPressionada(KeyEvent e) {
        int codigo = e.getKeyCode();
        if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) {
            movendoParaEsquerda = true;
        }
        if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) {
            movendoParaDireita = true;
        }
    }

    public void teclaLiberada(KeyEvent e) {
        int codigo = e.getKeyCode();
        if (codigo == KeyEvent.VK_LEFT || codigo == KeyEvent.VK_A) {
            movendoParaEsquerda = false;
        }
        if (codigo == KeyEvent.VK_RIGHT || codigo == KeyEvent.VK_D) {
            movendoParaDireita = false;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
