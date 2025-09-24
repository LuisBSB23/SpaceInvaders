package com.spaceinvaders.entidades;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class PowerUp {
    public enum TipoPowerUp {
        ESCUDO, TIRO_FORTE
    }

    private int x, y;
    private final int LARGURA = 30;
    private final int ALTURA = 30;
    private boolean visivel;
    private TipoPowerUp tipo;
    private Image imagem;

    public PowerUp(int x, int y, TipoPowerUp tipo) {
        this.x = x;
        this.y = y;
        this.tipo = tipo;
        this.visivel = true;
        carregarImagem();
    }

    private void carregarImagem() {
        String nomeImagem = "";
        if (tipo == TipoPowerUp.ESCUDO) {
            nomeImagem = "Escudo.png";
        } else if (tipo == TipoPowerUp.TIRO_FORTE) {
            nomeImagem = "BalaForte.png";
        }
        try {
            ImageIcon ii = new ImageIcon(getClass().getResource("/imagens/" + nomeImagem));
            this.imagem = ii.getImage().getScaledInstance(LARGURA, ALTURA, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem do power-up: " + e.getMessage());
        }
    }

    public void atualizar() {
        y += 2; // Cai lentamente
        if (y > 600) {
            visivel = false;
        }
    }

    public void desenhar(Graphics g) {
        if (visivel) {
            g.drawImage(imagem, x, y, null);
        }
    }

    public boolean isVisivel() {
        return visivel;
    }

    public void setVisivel(boolean visivel) {
        this.visivel = visivel;
    }
    
    public TipoPowerUp getTipo() {
        return tipo;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, LARGURA, ALTURA);
    }
}
