package com.spaceinvaders.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária para gerenciar a conexão com o banco de dados MySQL.
 */
public class ConexaoBD {

    // --- AJUSTE 4: CONSTANTES PARA FACILITAR A CONFIGURAÇÃO ---
    private static final String HOST = "localhost";
    private static final int PORTA = 3306; // <-- Altere aqui se usar uma porta diferente
    private static final String DATABASE = "space_invaders_db";
    private static final String USUARIO = "root"; // <-- Altere para seu usuário
    private static final String SENHA = "samyi23s11";   // <-- Altere para sua senha

    // URL de conexão montada a partir das constantes
    private static final String URL = String.format("jdbc:mysql://%s:%d/%s?useSSL=false&serverTimezone=UTC", 
                                                    HOST, PORTA, DATABASE);

    /**
     * Obtém uma conexão com o banco de dados.
     * @return um objeto Connection ou null se a conexão falhar.
     */
    public static Connection obterConexao() {
        try {
            // Carrega o driver JDBC do MySQL.
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Tenta estabelecer a conexão.
            return DriverManager.getConnection(URL, USUARIO, SENHA);
        } catch (ClassNotFoundException | SQLException e) {
            // Imprime o erro no console e lança uma exceção para sinalizar o problema.
            e.printStackTrace();
            throw new RuntimeException("Erro ao conectar ao banco de dados.", e);
        }
    }
}
