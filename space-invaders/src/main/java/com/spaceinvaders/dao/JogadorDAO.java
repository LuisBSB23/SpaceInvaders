package com.spaceinvaders.dao;

import org.mindrot.jbcrypt.BCrypt;

import com.spaceinvaders.modelo.Jogador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object (DAO) para a entidade Jogador.
 * Centraliza todas as operações de banco de dados relacionadas aos jogadores.
 */
public class JogadorDAO {

    /**
     * Verifica se um email já existe no banco de dados.
     * @param email O email a ser verificado.
     * @return true se o email já existir, false caso contrário.
     */
    public boolean emailJaExiste(String email) {
        String sql = "SELECT COUNT(*) FROM jogadores WHERE email = ?";
        try (Connection conexao = ConexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            // Em caso de erro, é mais seguro assumir que o e-mail já existe
            // para evitar uma inserção inválida.
            return true; 
        }
        return false;
    }

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
            // Este bloco agora serve como uma segurança para outras falhas de inserção,
            // já que a verificação de e-mail duplicado é feita antes.
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
        String sql = "SELECT id, nome, email, hash_senha, pontuacao_maxima, partidas_jogadas, inimigos_destruidos FROM jogadores WHERE email = ?";
        
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
                    int partidasJogadas = rs.getInt("partidas_jogadas");
                    int inimigosDestruidos = rs.getInt("inimigos_destruidos");
                    return new Jogador(id, nome, email, pontuacaoMaxima, partidasJogadas, inimigosDestruidos);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Atualiza a pontuação máxima de um jogador no banco de dados.
     * A atualização só ocorre se a nova pontuação for maior que a atual.
     * @param idJogador O ID do jogador.
     * @param novaPontuacao A pontuação obtida na partida.
     */
    public void atualizarPontuacaoMaxima(int idJogador, int novaPontuacao) {
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
    
    public void atualizarEstatisticas(int idJogador, int inimigosDestruidos) {
        String sql = "UPDATE jogadores SET partidas_jogadas = partidas_jogadas + 1, inimigos_destruidos = inimigos_destruidos + ? WHERE id = ?";
         try (Connection conexao = ConexaoBD.obterConexao();
             PreparedStatement stmt = conexao.prepareStatement(sql)) {
            
            stmt.setInt(1, inimigosDestruidos);
            stmt.setInt(2, idJogador);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- AJUSTE 2: BUSCAR MAIS DADOS NA QUERY ---
    public List<Jogador> getTopDezJogadores() {
        List<Jogador> jogadores = new ArrayList<>();
        String sql = "SELECT nome, pontuacao_maxima, partidas_jogadas, inimigos_destruidos FROM jogadores ORDER BY pontuacao_maxima DESC LIMIT 10";

        try (Connection conexao = ConexaoBD.obterConexao();
             Statement stmt = conexao.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String nome = rs.getString("nome");
                int pontuacaoMaxima = rs.getInt("pontuacao_maxima");
                int partidasJogadas = rs.getInt("partidas_jogadas");
                int inimigosDestruidos = rs.getInt("inimigos_destruidos");
                
                Jogador j = new Jogador(0, nome, "", pontuacaoMaxima, partidasJogadas, inimigosDestruidos);
                jogadores.add(j);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return jogadores;
    }
}
