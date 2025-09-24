package com.spaceinvaders.gui;

import javax.swing.*;

import com.spaceinvaders.dao.JogadorDAO;
import com.spaceinvaders.modelo.Jogador;
import com.spaceinvaders.util.FonteUtil;

import java.awt.*;

public class TelaLogin extends JFrame {
    private JTextField campoEmail;
    private JPasswordField campoSenha;
    private JogadorDAO jogadorDAO;

    public TelaLogin() {
        super("Space Invaders - Login");
        this.jogadorDAO = new JogadorDAO();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);
        setResizable(false);
        
        // --- NOVO LAYOUT ---
        JPanel painelFundo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon ii = new ImageIcon(getClass().getResource("/imagens/FundoEspaco.png"));
                g.drawImage(ii.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        setContentPane(painelFundo);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        
        Font fonteTitulo = FonteUtil.getFonte(40f);
        Font fonteTexto = FonteUtil.getFonte(14f);

        JLabel labelTitulo = new JLabel("SPACE INVADERS", SwingConstants.CENTER);
        labelTitulo.setFont(fonteTitulo);
        labelTitulo.setForeground(Color.YELLOW);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(20, 10, 20, 10);
        painelFundo.add(labelTitulo, gbc);
        
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(fonteTexto);
        labelEmail.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelFundo.add(labelEmail, gbc);

        campoEmail = new JTextField(15);
        campoEmail.setFont(fonteTexto);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        painelFundo.add(campoEmail, gbc);

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(fonteTexto);
        labelSenha.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        painelFundo.add(labelSenha, gbc);

        campoSenha = new JPasswordField(15);
        campoSenha.setFont(fonteTexto);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        painelFundo.add(campoSenha, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;
        
        // --- PAINEL DE BOTÕES ---
        JPanel painelBotoesLogin = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        painelBotoesLogin.setOpaque(false);
        JButton botaoLogin = new JButton("Login");
        botaoLogin.setFont(fonteTexto);
        JButton botaoCadastro = new JButton("Cadastre-se");
        botaoCadastro.setFont(fonteTexto);
        painelBotoesLogin.add(botaoLogin);
        painelBotoesLogin.add(botaoCadastro);
        painelFundo.add(painelBotoesLogin, gbc);
        
        gbc.gridy = 4;
        gbc.insets = new Insets(15, 10, 5, 10);
        JButton botaoRecordes = new JButton("Recordes");
        botaoRecordes.setFont(fonteTexto);
        painelFundo.add(botaoRecordes, gbc);

        botaoLogin.addActionListener(e -> tentarLogin());
        botaoCadastro.addActionListener(e -> abrirTelaCadastro());
        botaoRecordes.addActionListener(e -> abrirTelaRecordes());
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
            this.dispose();
            abrirMenuPrincipal(jogador);
        } else {
            JOptionPane.showMessageDialog(this, "Email ou senha inválidos.", "Erro de Login", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // --- NOVO MENU PRINCIPAL APÓS LOGIN ---
    private void abrirMenuPrincipal(Jogador jogador) {
        JFrame menuFrame = new JFrame("Space Invaders - Menu");
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setSize(500, 450);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setResizable(false);
        
        JPanel painelFundo = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon ii = new ImageIcon(getClass().getResource("/imagens/FundoEspaco.png"));
                g.drawImage(ii.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        menuFrame.setContentPane(painelFundo);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        
        JLabel labelBemVindo = new JLabel("Bem-vindo, " + jogador.getNome() + "!");
        labelBemVindo.setFont(FonteUtil.getFonte(22f));
        labelBemVindo.setForeground(Color.CYAN);
        gbc.gridy = 0;
        painelFundo.add(labelBemVindo, gbc);

        JButton botaoJogar = new JButton("Jogar");
        botaoJogar.setFont(FonteUtil.getFonte(16f));
        gbc.gridy = 1;
        painelFundo.add(botaoJogar, gbc);
        
        JButton botaoPerfil = new JButton("Meu Perfil");
        botaoPerfil.setFont(FonteUtil.getFonte(16f));
        gbc.gridy = 2;
        painelFundo.add(botaoPerfil, gbc);
        
        JButton botaoSair = new JButton("Sair (Logout)");
        botaoSair.setFont(FonteUtil.getFonte(16f));
        gbc.gridy = 3;
        painelFundo.add(botaoSair, gbc);

        botaoJogar.addActionListener(e -> {
            menuFrame.dispose();
            JanelaPrincipal janela = new JanelaPrincipal(jogador);
            janela.setVisible(true);
        });
        
        botaoPerfil.addActionListener(e -> {
            TelaPerfil telaPerfil = new TelaPerfil(jogador, menuFrame);
            telaPerfil.setVisible(true);
            menuFrame.setVisible(false);
        });

        botaoSair.addActionListener(e -> {
            menuFrame.dispose();
            new TelaLogin().setVisible(true);
        });

        menuFrame.setVisible(true);
    }

    private void abrirTelaCadastro() {
        TelaCadastro telaCadastro = new TelaCadastro(this);
        telaCadastro.setVisible(true);
        this.setVisible(false);
    }
    
    private void abrirTelaRecordes() {
        TelaRecordes telaRecordes = new TelaRecordes(this);
        telaRecordes.setVisible(true);
        this.setVisible(false);
    }
}
