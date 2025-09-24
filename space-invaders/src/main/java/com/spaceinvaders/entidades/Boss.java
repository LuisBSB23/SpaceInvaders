package com.spaceinvaders.entidades;

import javax.swing.ImageIcon;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Boss {
    private int x, y;
    private int vida;
    private final int vidaMaxima;
    private Image imagem;
    private int direcaoX = 5;
    private final int LARGURA = 150;
    private final int ALTURA = 100;
    private boolean visivel;
    private Random random = new Random();
    private long ultimoTiro = 0;
    private long proximoTiroIntervalo = 1500; // Intervalo inicial

    public Boss(int x, int y, int vida) {
        this.x = x;
        this.y = y;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.visivel = true;
        carregarImagem();
    }

    private void carregarImagem() {
        try {
            ImageIcon ii = new ImageIcon(getClass().getResource("/imagens/Boss.png"));
            this.imagem = ii.getImage().getScaledInstance(LARGURA, ALTURA, Image.SCALE_SMOOTH);
        } catch (Exception e) {
            System.err.println("Erro ao carregar a imagem do chefe: " + e.getMessage());
        }
    }

    public void atualizar(int larguraTela) {
        x += direcaoX;
        if (x <= 0 || x >= larguraTela - LARGURA) {
            direcaoX *= -1;
        }
    }

    public List<Projetil> atirar() {
        long tempoAtual = System.currentTimeMillis();
        List<Projetil> projeteis = new ArrayList<>();
        // CORREÇÃO: Usa o intervalo de tiro variável
        if (tempoAtual - ultimoTiro > proximoTiroIntervalo) { 
            // Tiro triplo
            projeteis.add(new Projetil(x + LARGURA / 2, y + ALTURA, false, Projetil.TipoProjetil.NORMAL));
            projeteis.add(new Projetil(x + 20, y + ALTURA - 20, false, Projetil.TipoProjetil.NORMAL));
            projeteis.add(new Projetil(x + LARGURA - 20, y + ALTURA - 20, false, Projetil.TipoProjetil.NORMAL));
            ultimoTiro = tempoAtual;
            // CORREÇÃO: Define um novo intervalo aleatório para o próximo tiro (entre 1 e 2 segundos)
            proximoTiroIntervalo = 1000 + random.nextInt(1000); 
        }
        return projeteis;
    }

    public void desenhar(Graphics g) {
        if (visivel) {
            g.drawImage(imagem, x, y, null);
            // Barra de vida
            g.setColor(java.awt.Color.RED);
            g.fillRect(x, y - 20, LARGURA, 10);
            g.setColor(java.awt.Color.GREEN);
            g.fillRect(x, y - 20, (int) (((double) vida / vidaMaxima) * LARGURA), 10);
        }
    }

    public void levarDano(int dano) {
        this.vida -= dano;
        if (this.vida <= 0) {
            this.visivel = false;
        }
    }

    public boolean isVisivel() {
        return visivel;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, LARGURA, ALTURA);
    }
}

