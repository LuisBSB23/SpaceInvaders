package com.seuprojeto.entidades;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.event.KeyEvent;

public class Nave {
    private int x, y;
    private int velocidade;
    private boolean movendoParaEsquerda;
    private boolean movendoParaDireita;
    public static final int LARGURA = 50;
    public static final int ALTURA = 30;

    public Nave(int xInicial, int yInicial) {
        this.x = xInicial;
        this.y = yInicial;
        this.velocidade = 5;
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
        g.setColor(Color.GREEN);
        g.fillRect(x, y, LARGURA, ALTURA);
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
