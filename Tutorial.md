 ## Tutorial Completo do Space Invaders
     Este tutorial está dividido em duas partes:

     1. Executando a Aplicação: Passos para configurar o ambiente e rodar o jogo.

     2. Como Jogar: Uma visão geral das telas e da jogabilidade.

## 1. Executando a Aplicação
Siga estes passos para garantir que o jogo execute corretamente no seu ambiente de desenvolvimento.

## Pré-requisitos
Visual Studio Code: O projeto é configurado para ser executado facilmente no VS Code.
Java Extension Pack: Essencial para compilar e executar projetos Java. Instale a extensão "Extension Pack for Java" da Microsoft. (Será necessario reabrir o repositório ou reiniciar o VSCODE caso não tenha a extensão instalada para reconhecer os pacotes)
MySQL: É necessário ter o MySQL instalado. Você pode usar uma ferramenta como o MySQL Workbench para gerenciar o banco de dados.

## Passo 1: Configurar o Banco de Dados
O jogo precisa de um banco de dados para salvar os perfis dos jogadores e suas pontuações.

Crie o Schema: Abra o seu cliente MySQL (como o MySQL Workbench) e execute o script SQL localizado em database/schema.sql. Isso criará o banco de dados space_invaders_db e a tabela jogadores.

## Passo 2: Configurar a Conexão no Código
Agora, informe ao projeto Java como se conectar ao banco de dados que você acabou de criar.

Abra o arquivo: space-invaders/src/main/java/com/spaceinvaders/dao/ConexaoBD.java.

Localize e altere as seguintes constantes com as suas credenciais do MySQL:

private static final String USUARIO = "root"; // <-- Altere para seu usuário
private static final String SENHA = "sua-senha";   // <-- Altere para sua senha

Obs: Se o seu MySQL roda em uma porta diferente da padrão (3306), você também pode alterar a constante PORTA.

## Passo 3: Executar o Jogo
Com tudo configurado, você já pode iniciar a aplicação.

No explorador de arquivos do VS Code, localize o arquivo Jogo.java no seguinte caminho: space-invaders/src/main/java/com/spaceinvaders/Jogo.java.

Clique com o botão direito sobre o arquivo Jogo.java e selecione a opção "Run Java".

A janela de login do Space Invaders deverá aparecer.

## 2. Como Jogar

## Telas Principais
Tela de Login:

Login: Insira seu email e senha para acessar o menu principal.

Cadastre-se: Se você não tiver uma conta, clique aqui para criar um novo perfil.

Recordes: Veja a lista dos 10 maiores pontuadores do jogo.

## Menu Principal:

Jogar: Inicia uma nova partida.

Meu Perfil: Exibe suas estatísticas, como recorde de pontos, total de partidas jogadas e inimigos destruídos.

Sair (Logout): Retorna para a tela de login.

## Jogabilidade
Controles:

Movimentação: Use as Setas Direita/Esquerda ou as teclas A e D.

Atirar: Pressione a tecla Espaço.

Pausar: Pressione a tecla P durante o jogo para pausar ou despausar.

## Objetivo:

O objetivo principal é destruir todas as hordas de invasores alienígenas para avançar de nível e acumular o máximo de pontos possível.

Elementos do Jogo:

Invasores: Existem diferentes tipos de invasores, cada um com uma pontuação diferente. Conforme os níveis avançam, eles se movem mais rápido.

Barreiras: Use os quatro blocos de barreiras para se proteger dos tiros inimigos. Elas são destrutíveis tanto pelos seus tiros quanto pelos dos inimigos.

Power-ups: Aleatoriamente, ao destruir um invasor, um item especial pode aparecer:

Escudo (Ícone Azul): Protege sua nave de um único disparo inimigo.

Tiro Forte (Ícone Rosa): Seus projéteis ficam mais fortes e podem atravessar múltiplos inimigos.

Batalha contra o Chefe: A cada 3 níveis, você enfrentará um chefe poderoso com uma barra de vida e múltiplos padrões de tiro.

## Fim de Jogo:

Você começa com 3 vidas. O jogo termina quando suas vidas chegam a zero.

Sua pontuação final será exibida. Se for maior que seu recorde anterior, ela será salva.

Você pode pressionar Enter para jogar novamente ou Esc para voltar ao menu principal.