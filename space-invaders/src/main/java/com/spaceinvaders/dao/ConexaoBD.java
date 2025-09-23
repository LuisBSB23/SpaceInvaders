package com.spaceinvaders.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe utilitária para gerenciar a conexão com o banco de dados MySQL.
 */
public class ConexaoBD {

    // Altere as credenciais abaixo para corresponder à sua configuração local do MySQL.
    private static final String URL = "jdbc:mysql://localhost:3306/space_invaders_db?useSSL=false&serverTimezone=UTC";
    private static final String USUARIO = "root"; // <-- Altere para seu usuário
    private static final String SENHA = "samyi23s11";   // <-- Altere para sua senha

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
