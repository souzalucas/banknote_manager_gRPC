/**
* SQLiteJDBCDriverConnection.java
* Código da Conexão com o banco de dados de um serviço de gerenciamento 
* de notas de alunos usando gRPC.
* Autor: Lucas Souza Santos
* Data de Criação: 21/09/2020
* Ultima atualização: 28/09/2020
 */

import java.sql.*;

public class SQLiteJDBCDriverConnection {
  static Connection connection;

  public static Connection connect() {    
    try {

      connection = DriverManager.getConnection("jdbc:sqlite:./src/main/database/gerenciamento_notas.db");

      System.out.println("Conexão com o banco de dados realizada!");

    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return connection;
  }
}