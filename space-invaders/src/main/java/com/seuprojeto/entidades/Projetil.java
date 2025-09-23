package com.seuprojeto.entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Projetil {
    private int x, y;
    private int velocidade;
    private boolean doJogador;
    private boolean visivel;

    public static final int LARGURA = 5;
    public static final int ALTURA = 15;

    public Projetil(int x, int y, boolean doJogador) {
        this.x = x - LARGURA / 2;
        this.y = y;
        this.doJogador = doJogador;
        this.velocidade = doJogador ? -10 : 5; // Negativo para subir, positivo para descer
        this.visivel = true;
    }

    public void atualizar() {
        y += velocidade;
        // Se o projétil sair da tela, fica invisível
        if (y < 0 || y > 600) { 
            visivel = false;
        }
    }

    public void desenhar(Graphics g) {
        if (visivel) {
            g.setColor(doJogador ? Color.CYAN : Color.ORANGE);
            g.fillRect(x, y, LARGURA, ALTURA);
        }
    }

    public boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, LARGURA, ALTURA);
    }
}
