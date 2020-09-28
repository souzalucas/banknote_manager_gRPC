/**
* FunctionalitiesImpl.java
* Código das funcionalidades de um serviço de gerenciamento 
* de notas de alunos usando gRPC.
* Autor: Lucas Souza Santos 
* Data de Criação: 21/09/2020
* Ultima atualização: 28/09/2020
 */

import io.grpc.stub.StreamObserver;
import java.sql.*;

public class FunctionalitiesImpl extends FunctionalitiesGrpc.FunctionalitiesImplBase {

  @Override
  public void execute(Request request, StreamObserver<Reply> responseObserver) {
    // Conexao com banco de dados
    Connection dbConnection = SQLiteJDBCDriverConnection.connect();

    // Instancia resposta
    Reply.Builder response = Reply.newBuilder();
    response.setOperationId(request.getOperationId());

    /* Chama a funcionalidade de acordo com o operationId */
    switch(request.getOperationId()) {

      case 1:
        Functionalities.cadastroNota(request, response, dbConnection);
      break;

      case 2:
        Functionalities.consultaNota(request, response, dbConnection);
      break;

      case 3:
        Functionalities.removeNota(request, response, dbConnection);
      break;

      case 4:
        Functionalities.atualizaNota(request, response, dbConnection);
      break;

      case 5:
        Functionalities.notasEFaltas(request, response, dbConnection);
      break;

      case 6:
        Functionalities.listaAlunos(request, response, dbConnection);
      break;

      default:
        response.setStatus("operationId invalido!");
      break;
    }
  
    responseObserver.onNext(response.build());
    responseObserver.onCompleted();

    /* Fecha a conexao com o banco de dados */
    try {
      if (dbConnection != null) {
        dbConnection.close();
      }
    } catch (SQLException ex) {
      System.out.println(ex.getMessage());
    }
  }   
}
