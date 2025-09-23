package com.seuprojeto.dao;

import com.seuprojeto.modelo.Jogador;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) para a entidade Jogador.
 * Centraliza todas as operações de banco de dados relacionadas aos jogadores.
 */
public class JogadorDAO {

    /**
     * Registra um novo jogador no banco de dados.
     * A senha é criptografada usando jBCrypt antes de ser armazenada.
     * @param jogador O objeto Jogador contendo nome, email e senha em texto plano.
     * @return true se o registro for bem-sucedido, false caso contrário.
     */
    public boolean registrarJogador(Jogador jogador) {
        String sql = "INSERT INTO jogadores (nome, email, hash_senha) VALUES (?, ?, ?)";
        
        try (Connection conexao = ConexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {

            // Gera o hash da senha
            String hashSenha = BCrypt.hashpw(jogador.getSenha(), BCrypt.gensalt());

            stmt.setString(1, jogador.getNome());
            stmt.setString(2, jogador.getEmail());
            stmt.setString(3, hashSenha);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;

        } catch (SQLException e) {
            // Um erro de violação de chave única (email duplicado) pode ocorrer aqui.
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Autentica um jogador com base no email e senha.
     * @param email O email do jogador.
     * @param senha A senha em texto plano a ser verificada.
     * @return Um objeto Jogador se a autenticação for bem-sucedida, caso contrário, null.
     */
    public Jogador loginJogador(String email, String senha) {
        String sql = "SELECT id, nome, email, hash_senha, pontuacao_maxima FROM jogadores WHERE email = ?";
        
        try (Connection conexao = ConexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashArmazenado = rs.getString("hash_senha");
                // Compara a senha fornecida com o hash armazenado
                if (BCrypt.checkpw(senha, hashArmazenado)) {
                    int id = rs.getInt("id");
                    String nome = rs.getString("nome");
                    int pontuacaoMaxima = rs.getInt("pontuacao_maxima");
                    return new Jogador(id, nome, email, pontuacaoMaxima);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // Retorna null se o email não for encontrado ou a senha estiver incorreta.
        return null;
    }

    /**
     * Atualiza a pontuação máxima de um jogador no banco de dados.
     * A atualização só ocorre se a nova pontuação for maior que a atual.
     * @param idJogador O ID do jogador.
     * @param novaPontuacao A pontuação obtida na partida.
     */
    public void atualizarPontuacaoMaxima(int idJogador, int novaPontuacao) {
        // A cláusula WHERE garante que a atualização só ocorra se a nova pontuação for maior.
        String sql = "UPDATE jogadores SET pontuacao_maxima = ? WHERE id = ? AND pontuacao_maxima < ?";
        
        try (Connection conexao = ConexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, novaPontuacao);
            stmt.setInt(2, idJogador);
            stmt.setInt(3, novaPontuacao);

            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
