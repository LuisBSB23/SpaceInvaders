package com.seuprojeto.entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Invasor {
    private int x, y;
    public static final int LARGURA = 40;
    public static final int ALTURA = 30;
    private boolean visivel;

    public Invasor(int x, int y) {
        this.x = x;
        this.y = y;
        this.visivel = true;
    }

    public void mover(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void desenhar(Graphics g) {
        if (visivel) {
            g.setColor(Color.RED);
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
}
