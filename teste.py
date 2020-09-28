
def main():

  while(True):
    try:
      operationId = int(input("Que operação deseja fazer? > "))
    except ValueError:
      print("ERRO: OPERACAO INVALIDA!")
      continue

    print(operationId)

main()