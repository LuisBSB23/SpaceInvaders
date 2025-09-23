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

    // Construtor usado para carregar dados do banco
    public Jogador(int id, String nome, String email, int pontuacaoMaxima) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.pontuacaoMaxima = pontuacaoMaxima;
    }
    
    // Construtor usado para registrar um novo jogador
    public Jogador(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
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
}
