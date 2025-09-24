package com.spaceinvaders.entidades;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Projetil {
    // --- NOVO: TIPO DE PROJÉTIL ---
    public enum TipoProjetil {
        NORMAL, FORTE
    }

    private int x, y;
    private int velocidade;
    private boolean doJogador;
    private boolean visivel;
    private TipoProjetil tipo;
    private int largura;
    private int altura;


    public Projetil(int x, int y, boolean doJogador, TipoProjetil tipo) {
        this.doJogador = doJogador;
        this.tipo = tipo;
        this.velocidade = doJogador ? -10 : 5;
        this.visivel = true;
        
        // --- MODIFICADO: TAMANHO BASEADO NO TIPO ---
        if (tipo == TipoProjetil.FORTE) {
            this.largura = 10;
            this.altura = 25;
        } else {
            this.largura = 5;
            this.altura = 15;
        }
        this.x = x - largura / 2;
        this.y = y;
    }

    public void atualizar() {
        y += velocidade;
        if (y < 0 || y > 600) { 
            visivel = false;
        }
    }

    public void desenhar(Graphics g) {
        if (visivel) {
            if (doJogador) {
                g.setColor(tipo == TipoProjetil.FORTE ? Color.MAGENTA : Color.CYAN);
            } else {
                g.setColor(Color.ORANGE);
            }
            g.fillRect(x, y, largura, altura);
        }
    }

    public boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }
    
    public TipoProjetil getTipo() {
        return tipo;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, largura, altura);
    }
}
