CREATE DATABASE banco_de_usuarios;

USE banco_de_usuarios;

CREATE TABLE Usuario (
  id INT NOT NULL AUTO_INCREMENT,
  nome VARCHAR(200) NOT NULL,
  email VARCHAR(50) NOT NULL,
  senha TEXT NOT NULL,
  telefone VARCHAR(15) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY email (email)
  );

