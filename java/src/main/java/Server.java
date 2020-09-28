/**
* Server.java
* Código do servidor de um serviço de gerenciamento 
* de notas de alunos usando gRPC.
* Autor: Lucas Souza Santos 
* Data de Criação: 21/09/2020
* Ultima atualização: 28/09/2020
 */

import io.grpc.ServerBuilder;
import java.io.IOException;

public class Server {
  public static void main(String[] args) {
    io.grpc.Server server = ServerBuilder
      .forPort(7777)
      .addService(new FunctionalitiesImpl())
      .build();
        
    try {
      server.start();
      System.out.println("Servidor iniciado.");
      server.awaitTermination();
    } catch (IOException | InterruptedException e) {
      System.err.println("Erro: " + e);
    }
  }
}
