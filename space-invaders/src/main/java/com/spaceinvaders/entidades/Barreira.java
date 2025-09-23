package com.spaceinvaders.entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Representa um único bloco destrutível de uma barreira/escudo.
 */
public class Barreira {
    private int x, y;
    public static final int LARGURA = 10;
    public static final int ALTURA = 10;
    private boolean visivel;

    public Barreira(int x, int y) {
        this.x = x;
        this.y = y;
        this.visivel = true;
    }

    public void desenhar(Graphics g) {
        if (visivel) {
            // Uma cor verde que remete aos escudos clássicos
            g.setColor(new Color(50, 205, 50)); 
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
}
