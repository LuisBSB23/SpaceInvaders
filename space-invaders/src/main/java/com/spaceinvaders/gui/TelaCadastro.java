package com.spaceinvaders.gui;

import javax.swing.*;

import com.spaceinvaders.dao.JogadorDAO;
import com.spaceinvaders.modelo.Jogador;
import com.spaceinvaders.util.FonteUtil;

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
        setSize(450, 400);
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
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        Font fonteTitulo = FonteUtil.getFonte(28f);
        Font fonteTexto = FonteUtil.getFonte(14f);
        
        JLabel labelTitulo = new JLabel("CRIAR CONTA", SwingConstants.CENTER);
        labelTitulo.setFont(fonteTitulo);
        labelTitulo.setForeground(Color.YELLOW);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        painelFundo.add(labelTitulo, gbc);

        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.LINE_END;

        JLabel labelNome = new JLabel("Nome:");
        labelNome.setFont(fonteTexto);
        labelNome.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 1;
        painelFundo.add(labelNome, gbc);

        campoNome = new JTextField(15);
        campoNome.setFont(fonteTexto);
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.LINE_START;
        painelFundo.add(campoNome, gbc);

        JLabel labelEmail = new JLabel("Email:");
        labelEmail.setFont(fonteTexto);
        labelEmail.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_END;
        painelFundo.add(labelEmail, gbc);

        campoEmail = new JTextField(15);
        campoEmail.setFont(fonteTexto);
        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.LINE_START;
        painelFundo.add(campoEmail, gbc);

        JLabel labelSenha = new JLabel("Senha:");
        labelSenha.setFont(fonteTexto);
        labelSenha.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_END;
        painelFundo.add(labelSenha, gbc);

        campoSenha = new JPasswordField(15);
        campoSenha.setFont(fonteTexto);
        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.LINE_START;
        painelFundo.add(campoSenha, gbc);
        
        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        painelBotoes.setOpaque(false);

        JButton botaoCadastrar = new JButton("Cadastrar");
        botaoCadastrar.setFont(fonteTexto);
        
        JButton botaoVoltar = new JButton("Voltar");
        botaoVoltar.setFont(fonteTexto);
        
        painelBotoes.add(botaoCadastrar);
        painelBotoes.add(botaoVoltar);
        
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        painelFundo.add(painelBotoes, gbc);

        botaoCadastrar.addActionListener(e -> registrar());
        botaoVoltar.addActionListener(e -> voltarParaLogin());

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
