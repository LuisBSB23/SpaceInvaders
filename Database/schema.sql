-- Criação do banco de dados (opcional, se ainda não existir)
CREATE DATABASE IF NOT EXISTS space_invaders_db;

-- Seleciona o banco de dados para usar
USE space_invaders_db;

-- Criação da tabela de jogadores atualizada
DROP TABLE IF EXISTS `jogadores`;
CREATE TABLE IF NOT EXISTS `jogadores` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nome` VARCHAR(100) NOT NULL,
  `email` VARCHAR(100) NOT NULL UNIQUE,
  `hash_senha` VARCHAR(255) NOT NULL,
  `pontuacao_maxima` INT DEFAULT 0,
  `partidas_jogadas` INT DEFAULT 0,
  `inimigos_destruidos` INT DEFAULT 0,
  PRIMARY KEY (`id`)
);

-- Insere 7 jogadores de exemplo com as novas estatísticas
-- A senha '123' está representada como um hash bcrypt para fins de exemplo.
INSERT INTO `jogadores` (nome, email, hash_senha, pontuacao_maxima, partidas_jogadas, inimigos_destruidos) VALUES
('AstroPlayer', 'astro@email.com', '$2a$10$VjD/BONQdH/xUD4UBByWMuCgJyZT4pfssJWkS6utXv6sBrk58jbHm', 1250, 15, 320),
('CometaHalley', 'halley@email.com', '$2a$10$VjD/BONQdH/xUD4UBByWMuCgJyZT4pfssJWkS6utXv6sBrk58jbHm', 980, 12, 215),
('Supernova', 'nova@email.com', '$2a$10$VjD/BONQdH/xUD4UBByWMuCgJyZT4pfssJWkS6utXv6sBrk58jbHm', 2100, 25, 550),
('Galaxian', 'galaxy@email.com', '$2a$10$VjD/BONQdH/xUD4UBByWMuCgJyZT4pfssJWkS6utXv6sBrk58jbHm', 1500, 18, 410),
('Zorg', 'zorg@email.com', '$2a$10$VjD/BONQdH/xUD4UBByWMuCgJyZT4pfssJWkS6utXv6sBrk58jbHm', 750, 10, 180),
('RocketMan', 'rocket@email.com', '$2a$10$VjD/BONQdH/xUD4UBByWMuCgJyZT4pfssJWkS6utXv6sBrk58jbHm', 1820, 22, 480),
('Luna', 'luna@email.com', '$2a$10$VjD/BONQdH/xUD4UBByWMuCgJyZT4pfssJWkS6utXv6sBrk58jbHm', 2500, 30, 620);
