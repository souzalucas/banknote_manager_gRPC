# client.py
# Código do cliente de um serviço de gerenciamento 
# de notas de alunos usando gRPC.
# Autor: Lucas Souza Santos
# Data de Criação: 21/09/2020
# Ultima atualização: 28/09/2020
#################################

# Copyright 2015, Google Inc.
# All rights reserved.
#
# Redistribution and use in source and binary forms, with or without
# modification, are permitted provided that the following conditions are
# met:
#
#     * Redistributions of source code must retain the above copyright
# notice, this list of conditions and the following disclaimer.
#     * Redistributions in binary form must reproduce the above
# copyright notice, this list of conditions and the following disclaimer
# in the documentation and/or other materials provided with the
# distribution.
#     * Neither the name of Google Inc. nor the names of its
# contributors may be used to endorse or promote products derived from
# this software without specific prior written permission.
#
# THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
# "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
# LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
# A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
# OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
# SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
# LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
# DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
# THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
# (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
# OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

"""The Python implementation of the GRPC helloworld.Greeter client."""

from __future__ import print_function

import grpc

import service_pb2
import service_pb2_grpc


def client():
  # Configura o canal de comunicacao
  channel = grpc.insecure_channel('localhost:7777')
  
  # Inicializa e configura o stub
  stub = service_pb2_grpc.FunctionalitiesStub(channel)

  print("[1] Cadastra nota para um aluno")
  print("[2] Consulta a nota de um aluno")
  print("[3] Remove a nota de um aluno")
  print("[4] Atualiza a nota de um aluno")
  print("[5] Consulta as notas e faltas de uma disciplina em um ano/semestre.")
  print("[6] Consulta de alunos de uma disciplina em um ano/semestre\n")

  while (True):
    try:
      operationId = int(input("Que operação deseja fazer? > "))
    except ValueError:
      print("ERRO: OPERACAO INVALIDA!")
      continue

    # Instanciando a estrutura
    req = service_pb2.Request()
    req.operationId = operationId

    # Preenche a estrutura de acordo com a operacao
    if (operationId >= 1 and operationId <= 6):
      discCode = input("Codigo da disciplina > ")
      ano = input("Ano > ")
      semestre = input("Semestre > ")

      # Tratando campos vazios
      if(discCode == '' or ano == '' or semestre == ''):
        print("ERRO: CAMPOS VAZIOS!")
        continue

      req.discCode = str(discCode)
      req.ano = int(ano)
      req.semestre = int(semestre)
    
    else:
      print("ERRO: OPERACAO INVALIDA!")
      continue
    
    if (operationId >= 1 and operationId <= 4):
      ra = input("RA do aluno > ")

      # Tratando campos vazios
      if(ra == ''):
        print("ERRO: CAMPO VAZIO!")
        continue

      req.RA = int(ra)

    if (operationId == 1 or operationId == 4):
      nota = input("Nota > ")

      # Tratando campos vazios
      if(nota == ''):
        print("ERRO: CAMPO VAZIO!")
        continue

      req.nota = float(nota)
    
    # Chamada remota
    res = stub.Execute(req)

    # Trata resposta do servidor
    if (res.operationId == operationId):
      # Sucesso na operacao
      if (res.status == "1"):
        
        if (res.operationId == 2):
          for aluno in res.alunos:
            if (aluno.nota == float(-1)):
              print("Nota: N/A")
            else:
              print("Nota:", aluno.nota)  

        if (res.operationId == 5):
          for aluno in res.alunos:
            print("\nRA:", aluno.RA)
            
            if (aluno.nota == float(-1)):
              print("Nota: N/A")
            else:
              print("Nota:", aluno.nota)
  
            print("Faltas:", aluno.faltas)
            print("--------------------")

        if (res.operationId == 6):
          for aluno in res.alunos:
            print("\nRA:", aluno.RA)
            print("Nome:", aluno.nome)
            print("Periodo:", aluno.periodo)
            print("--------------------")
        
        else:
          print("Operação realizada com sucesso")

      # Mostra codigo do erro
      else:
        print(res.status)

if __name__ == '__main__':
  client()
