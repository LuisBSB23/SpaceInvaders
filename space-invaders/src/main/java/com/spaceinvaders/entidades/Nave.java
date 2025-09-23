package com.spaceinvaders.entidades;

import java.awt.Graphics;
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
    }

    public void desenhar(Graphics g) {
        if (imagem != null) {
            g.drawImage(imagem, x, y, null);
        } else {
            // Fallback para o retângulo caso a imagem não carregue
            g.setColor(java.awt.Color.GREEN);
            g.fillRect(x, y, LARGURA, ALTURA);
        }
    }

    public Projetil atirar() {
        // O projétil sai do meio da nave
        return new Projetil(x + LARGURA / 2, y, true);
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
