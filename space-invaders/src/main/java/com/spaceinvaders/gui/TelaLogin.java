package com.spaceinvaders.gui;

import javax.swing.*;

import com.spaceinvaders.dao.JogadorDAO;
import com.spaceinvaders.modelo.Jogador;

import java.awt.*;

public class TelaLogin extends JFrame {
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JogadorDAO jogadorDAO;

    public TelaLogin() {
        super("Space Invaders - Login");
        this.jogadorDAO = new JogadorDAO();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel labelTitulo = new JLabel("BEM-VINDO!", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Email:"), gbc);

        campoEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(campoEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Senha:"), gbc);

        campoSenha = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(campoSenha, gbc);

        JButton botaoLogin = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botaoLogin, gbc);

        JButton botaoCadastro = new JButton("Não tem conta? Cadastre-se");
        gbc.gridy = 4;
        add(botaoCadastro, gbc);

        botaoLogin.addActionListener(e -> tentarLogin());
        botaoCadastro.addActionListener(e -> abrirTelaCadastro());
    }

    private void tentarLogin() {
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        if (email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Jogador jogador = jogadorDAO.loginJogador(email, senha);

        if (jogador != null) {
            JOptionPane.showMessageDialog(this, "Login bem-sucedido! Bem-vindo, " + jogador.getNome() + "!");
            this.dispose(); // Fecha a tela de login
            // Inicia o jogo
            JanelaPrincipal janela = new JanelaPrincipal(jogador);
            janela.setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void abrirTelaCadastro() {
        TelaCadastro telaCadastro = new TelaCadastro(this);
        telaCadastro.setVisible(true);
        this.setVisible(false);
    }
}
