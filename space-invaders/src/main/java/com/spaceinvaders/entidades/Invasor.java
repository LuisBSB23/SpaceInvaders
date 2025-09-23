package com.spaceinvaders.entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Invasor {
    private int x, y;
    public static final int LARGURA = 40;
    public static final int ALTURA = 30;
    private boolean visivel;
    private int pontos; // Novo: Campo para armazenar os pontos do invasor

    /**
     * Construtor modificado para aceitar o valor de pontos.
     */
    public Invasor(int x, int y, int pontos) {
        this.x = x;
        this.y = y;
        this.pontos = pontos;
        this.visivel = true;
    }

    public void mover(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void desenhar(Graphics g) {
        if (visivel) {
            // Muda a cor com base nos pontos para um feedback visual
            if (pontos >= 30) {
                g.setColor(Color.YELLOW);
            } else if (pontos >= 20) {
                g.setColor(Color.ORANGE);
            } else {
                g.setColor(Color.RED);
            }
            g.fillRect(x, y, LARGURA, ALTURA);
        }
    }
    
    public Rectangle getBounds() {
        return new Rectangle(x, y, LARGURA, ALTURA);
    }

    public boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    /**
     * Retorna a quantidade de pontos que este invasor vale.
     * @return os pontos do invasor.
     */
    public int getPontos() {
        return pontos;
    }
}

