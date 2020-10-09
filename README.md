# banknote_manager_gRPC
Um serviço de gerenciamento de notas de alunos usando gRPC. 

## Bibliotecas utilizadas

### Java
- java.net
- java.io
- io.grpc
- java.sql

### Python
- grpc

## Instalação de dependências

### Clonar e acessar repositório
```
git clone https://github.com/souzalucas/banknote_manager_gRPC.git
cd banknote_manager_gRPC
```

### Obter APIs Python3
```
pip3 install grpcio
pip3 install grpcio-tools
```

## Compilação e execução

### Compilar e executar servidor Java
```
cd java
mvn compile
mvn exec:java -D"exec.mainClass"="Server"
```

### Compilar e executar cliente Python
```
cd ../python
python3 -m grpc_tools.protoc -I. --python_out=. --grpc_python_out=. service.proto
python3 client.py
```

## Exemplo de uso (Listar alunos em uma disciplina)

### No cliente, digite o código da operação
```
6
```

### E por fim, insira as informações da disciplina. O resultado sairá como:
```
RA: 111111
Nome: Fulano
Periodo: 6
--------------------

RA: 222222
Nome: Ciclano
Periodo: 7
--------------------
```

### Funcionalidades do sistema
```
[1] Cadastra nota para um aluno

[2] Consulta a nota de um aluno

[3] Remove a nota de um aluno

[4] Atualiza a nota de um aluno

[5] Consulta as notas e faltas de uma disciplina em um ano/semestre

[6] Consulta de alunos de uma disciplina em um ano/semestre
```
