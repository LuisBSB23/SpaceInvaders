package com.spaceinvaders.entidades;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import javax.swing.ImageIcon;

public class Invasor {
    private int x, y;
    public static final int LARGURA = 40;
    public static final int ALTURA = 30;
    private boolean visivel;
    private int pontos;
    private Image imagem;

    /**
     * Construtor modificado para aceitar o nome da imagem e o valor de pontos.
     */
    public Invasor(int x, int y, int pontos, String nomeImagem) {
        this.x = x;
        this.y = y;
        this.pontos = pontos;
        this.visivel = true;
        
        try {
            ImageIcon ii = new ImageIcon(getClass().getResource("/imagens/" + nomeImagem));
            this.imagem = ii.getImage().getScaledInstance(LARGURA, ALTURA, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem do invasor '" + nomeImagem + "': " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void mover(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    public void desenhar(Graphics g) {
        if (visivel) {
            if (imagem != null) {
                g.drawImage(imagem, x, y, null);
            } else {
                // Fallback para o retângulo caso a imagem não carregue
                g.setColor(java.awt.Color.RED);
                g.fillRect(x, y, LARGURA, ALTURA);
            }
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
