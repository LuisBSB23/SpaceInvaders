package com.spaceinvaders.modelo;

/**
 * Classe que representa a entidade Jogador.
 * Contém os dados do jogador que serão persistidos no banco de dados.
 */
public class Jogador {

    private int id;
    private String nome;
    private String email;
    private String senha; // Usada apenas para cadastro, não é armazenada diretamente
    private int pontuacaoMaxima;
    // --- NOVOS ATRIBUTOS ---
    private int partidasJogadas;
    private int inimigosDestruidos;

    // Construtor completo para carregar dados do banco
    public Jogador(int id, String nome, String email, int pontuacaoMaxima, int partidasJogadas, int inimigosDestruidos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.pontuacaoMaxima = pontuacaoMaxima;
        this.partidasJogadas = partidasJogadas;
        this.inimigosDestruidos = inimigosDestruidos;
    }
    
    // Construtor usado para registrar um novo jogador
    public Jogador(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        // Valores padrão para novos jogadores
        this.pontuacaoMaxima = 0;
        this.partidasJogadas = 0;
        this.inimigosDestruidos = 0;
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public int getPontuacaoMaxima() {
        return pontuacaoMaxima;
    }

    public void setPontuacaoMaxima(int pontuacaoMaxima) {
        this.pontuacaoMaxima = pontuacaoMaxima;
    }
    
    // --- NOVOS GETTERS E SETTERS ---
    public int getPartidasJogadas() {
        return partidasJogadas;
    }

    public void setPartidasJogadas(int partidasJogadas) {
        this.partidasJogadas = partidasJogadas;
    }

    public int getInimigosDestruidos() {
        return inimigosDestruidos;
    }

    public void setInimigosDestruidos(int inimigosDestruidos) {
        this.inimigosDestruidos = inimigosDestruidos;
    }
}
