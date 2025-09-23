package com.spaceinvaders.gui;

import javax.swing.*;

import com.spaceinvaders.dao.JogadorDAO;
import com.spaceinvaders.modelo.Jogador;

import java.awt.*;

public class TelaCadastro extends JFrame {
    private JTextField campoNome;
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JogadorDAO jogadorDAO;
    private JFrame telaDeOrigem;

    public TelaCadastro(JFrame telaDeOrigem) {
        super("Space Invaders - Cadastro");
        this.jogadorDAO = new JogadorDAO();
        this.telaDeOrigem = telaDeOrigem;

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());
        setResizable(false);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel labelTitulo = new JLabel("CRIAR CONTA", SwingConstants.CENTER);
        labelTitulo.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Nome:"), gbc);

        campoNome = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 1;
        add(campoNome, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);

        campoEmail = new JTextField(20);
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(campoEmail, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Senha:"), gbc);

        campoSenha = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(campoSenha, gbc);

        JButton botaoCadastrar = new JButton("Cadastrar");
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(botaoCadastrar, gbc);
        
        JButton botaoVoltar = new JButton("Voltar para Login");
        gbc.gridy = 5;
        add(botaoVoltar, gbc);

        botaoCadastrar.addActionListener(e -> registrar());
        botaoVoltar.addActionListener(e -> voltarParaLogin());

        // Garante que a tela de login reapareça se esta for fechada
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                voltarParaLogin();
            }
        });
    }

    private void registrar() {
        String nome = campoNome.getText();
        String email = campoEmail.getText();
        String senha = new String(campoSenha.getPassword());

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Por favor, preencha todos os campos.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Simples validação de email
        if (!email.contains("@") || !email.contains(".")) {
             JOptionPane.showMessageDialog(this, "Por favor, insira um email válido.", "Erro", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Jogador novoJogador = new Jogador(nome, email, senha);
        boolean sucesso = jogadorDAO.registrarJogador(novoJogador);

        if (sucesso) {
            JOptionPane.showMessageDialog(this, "Cadastro realizado com sucesso! Faça o login para jogar.", "Sucesso", JOptionPane.INFORMATION_MESSAGE);
            voltarParaLogin();
        } else {
            JOptionPane.showMessageDialog(this, "Erro ao cadastrar. O email já pode estar em uso.", "Erro de Cadastro", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void voltarParaLogin() {
        this.dispose();
        telaDeOrigem.setVisible(true);
    }
}
