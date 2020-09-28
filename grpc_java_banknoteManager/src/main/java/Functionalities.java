/**
* Functionalities.java
* Código das funcionalidades de um serviço de gerenciamento 
* de notas de alunos usando gRPC.
* Autor: Lucas Souza Santos 
* Data de Criação: 21/09/2020
* Ultima atualização: 28/09/2020
 */

import java.sql.*;

public class Functionalities {

  static Statement statement;

  /**
   * Verifica se aluno existe de acordo com seu RA
   */
  private static String buscaAluno(int RA) {

    try {
      /* Busca pelo aluno */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM aluno WHERE (ra = " + String.valueOf(RA) + ");");
      if(!resultSet.isBeforeFirst()){
        return "RA inexistente";
      }

    } catch (SQLException e) {
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  /**
   * Verifica se disciplina existe de acordo com seu codigo
   */
  private static String buscaDisciplina(String discCode) {

    try {
      /* Busca pela disiplina */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM disciplina WHERE (codigo = '" + String.valueOf(discCode) + "');");
      if(!resultSet.isBeforeFirst()){
        return "Disciplina inexistente";
      }

    } catch (SQLException e) {
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  public static String cadastroNota(Request req, Reply.Builder res, Connection dbConnection) {
     
    /* Obtendo os dados para a busca e insercao */
    int RA = req.getRA();
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();
    float nota = req.getNota();

    try {

      statement = dbConnection.createStatement();

      /* Valida aluno e disciplina */
      String ret = buscaAluno(RA);
      if(!ret.equals("1")) {
        res.setStatus(ret);
        return ret;
      }
      ret = buscaDisciplina(discCode);
      if(!ret.equals("1")) {
        res.setStatus(ret);
        return ret;
      }

      /* Busca pela matricula */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM matricula WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      
      /* Matricula ja existe */
      if(resultSet.isBeforeFirst()){
        res.setStatus("Ja ha uma matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre));
        return ("Ja ha uma matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre));
      }
      
      /* Cadastra nota */
      statement.execute("INSERT INTO matricula (ra_aluno, cod_disciplina, ano, semestre, nota) VALUES ( " + String.valueOf(RA) + ", '" + discCode + "', " + String.valueOf(ano) + ", " + String.valueOf(semestre) + ", '" + String.valueOf(nota) + "');");
      
      /* Confirma cadastro */
      resultSet = statement.executeQuery("SELECT * FROM matricula WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      if(resultSet.isBeforeFirst()){
        res.setStatus("1");
      
      } else {
        res.setStatus("Erro no cadastro!");
        return "Erro no cadastro!";
      }

    } catch (SQLException e) {
      res.setStatus(String.valueOf(e.getMessage()));
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  public static String consultaNota(Request req, Reply.Builder res, Connection dbConnection) {

    /* Obtendo os dados para a busca */
    int RA = req.getRA();
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();

    try {

      statement = dbConnection.createStatement();
      
      /* Valida aluno e disciplina */
      String ret = buscaAluno(RA);
      if(!ret.equals("1")) {
        res.setStatus(ret);
        return ret;
      }
      ret = buscaDisciplina(discCode);
      if(!ret.equals("1")) {
        res.setStatus(ret);
        return ret;
      }
      
      /* Busca pela matricula */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM matricula WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      
      /* Matricula não existe */
      if(!resultSet.isBeforeFirst()){
        res.setStatus("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
        return ("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
      }
      
      while (resultSet.next()) {
        /* Construindo Aluno */
        Aluno.Builder aluno = Aluno.newBuilder();
        
        /* Adicionando valores no aluno */
        aluno.setNota(resultSet.getFloat("nota"));

        /* Adicionando aluno */
        res.addAlunos(aluno);
      }

      res.setStatus("1");

    } catch (SQLException e) {
      res.setStatus(String.valueOf(e.getMessage()));
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  public static String removeNota(Request req, Reply.Builder res, Connection dbConnection) {

    /* Obtendo os dados para a busca e remocao */
    int RA = req.getRA();
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();

    try {

      statement = dbConnection.createStatement();
      
      /* Valida aluno e disciplina */
      String ret = buscaAluno(RA);
      if(!ret.equals("1")) {
        res.setStatus(ret);
        return ret;
      }
      ret = buscaDisciplina(discCode);
      if(!ret.equals("1")) {
        res.setStatus(ret);
        return ret;
      }

      /* Busca pela matricula */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM matricula WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      
      /* Matricula nao existe */
      if(!resultSet.isBeforeFirst()){
        res.setStatus("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
        return ("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
      }

      /* remove nota */
      statement.execute("UPDATE matricula SET nota = -1 WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      res.setStatus("1");

    } catch (SQLException e) {
      res.setStatus(String.valueOf(e.getMessage()));
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  public static String atualizaNota(Request req, Reply.Builder res, Connection dbConnection) {
    
    /* Obtendo os dados para a busca e atulizacao */
    int RA = req.getRA();
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();
    float nota = req.getNota();

    try {

      statement = dbConnection.createStatement();
      
      /* Valida aluno e disciplina */
      String ret = buscaAluno(RA);
      if(!ret.equals("1")) {
        res.setStatus(ret);
        return ret;
      }
      ret = buscaDisciplina(discCode);
      if(!ret.equals("1")) {
        res.setStatus(ret);
        return ret;
      }

      /* Busca pela matricula */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM matricula WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      
      /* Matricula nao existe */
      if(!resultSet.isBeforeFirst()){
        res.setStatus("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
        return ("Matricula do aluno em " + String.valueOf(ano) + "/" + String.valueOf(semestre) + " inexistente");
      }

      /* Atualiza nota */
      statement.execute("UPDATE matricula SET nota = " + String.valueOf(nota) + " WHERE (ra_aluno = " + String.valueOf(RA) + " AND cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = "+ String.valueOf(ano) +" AND semestre = "+ String.valueOf(semestre) +");");
      res.setStatus("1");

    } catch (SQLException e) {
      res.setStatus(String.valueOf(e.getMessage()));
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  public static String notasEFaltas(Request req, Reply.Builder res, Connection dbConnection) {
    
    /* Obtendo os dados para a busca */
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();

    try {

      statement = dbConnection.createStatement();

      /* Valida disciplina */
      String ret = buscaDisciplina(discCode);
      if(!ret.equals("1")) {
        res.setStatus(ret);
        return ret;
      }
      
      /* Lista notas e faltas */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM matricula WHERE (cod_disciplina = '" + String.valueOf(discCode) + "' AND ano = " + String.valueOf(ano) + " AND semestre = " + String.valueOf(semestre) + ");");
      
      /* Nao ha alunos matriculados */
      if(!resultSet.isBeforeFirst()){
        res.setStatus("Nesta disciplina nao ha alunos matriculados em " + String.valueOf(ano) + "/" + String.valueOf(semestre));
        return ("Nesta disciplina nao ha alunos matriculados em " + String.valueOf(ano) + "/" + String.valueOf(semestre));
      }

      while (resultSet.next()) {

        /* Construindo Aluno */
        Aluno.Builder aluno = Aluno.newBuilder();
        
        /* Adicionando valores no aluno */
        aluno.setRA(resultSet.getInt("ra_aluno"));
        aluno.setNota(resultSet.getFloat("nota"));
        aluno.setFaltas(resultSet.getInt("faltas"));

        /* Adicionando aluno */
        res.addAlunos(aluno);
      }
  
      res.setStatus("1");

    } catch (SQLException e) {
      res.setStatus(String.valueOf(e.getMessage()));
      return String.valueOf(e.getMessage());
    }
    return "1";
  }

  public static String listaAlunos(Request req, Reply.Builder res, Connection dbConnection) {

    /* Obtendo os dados para a busca */
    String discCode = req.getDiscCode();
    int ano = req.getAno();
    int semestre = req.getSemestre();

    try {

      statement = dbConnection.createStatement();

      /* Valida disciplina */
      String ret = buscaDisciplina(discCode);
      if(!ret.equals("1")) {
        res.setStatus(ret);
        return ret;
      }
      
      /* Lista alunos */
      ResultSet resultSet = statement.executeQuery("SELECT * FROM aluno, matricula WHERE (select ra_aluno FROM matricula WHERE ano = " + String.valueOf(ano) + " AND semestre = " + String.valueOf(semestre) + " AND cod_disciplina = '" + String.valueOf(discCode) + "') AND matricula.ra_aluno = aluno.ra;");
      
      /* Nao ha alunos matriculados */
      if(!resultSet.isBeforeFirst()){
        res.setStatus("Nesta disciplina nao ha alunos matriculados em " + String.valueOf(ano) + "/" + String.valueOf(semestre));
        return ("Nesta disciplina nao ha alunos matriculados em " + String.valueOf(ano) + "/" + String.valueOf(semestre));
      }

      while (resultSet.next()) {

        /* Construindo Aluno */
        Aluno.Builder aluno = Aluno.newBuilder();
        
        /* Adicionando valores no aluno */
        aluno.setRA(resultSet.getInt("ra"));
        aluno.setNome(resultSet.getString("nome"));
        aluno.setPeriodo(resultSet.getInt("periodo"));
        aluno.setNota(resultSet.getFloat("nota"));
        aluno.setFaltas(resultSet.getInt("faltas"));

        /* Adicionando aluno */
        res.addAlunos(aluno);
      }
  
      res.setStatus("1");

    } catch (SQLException e) {
      res.setStatus(String.valueOf(e.getMessage()));
      return String.valueOf(e.getMessage());
    }
    return "1";
  }
}