# Simulador_de_Trafego
Repositório destinado ao trabalho da disciplina Desenvolvimento de Sistemas Paralelos e Distribuídos da Udesc.
Este projeto simula o tráfego de veículos em uma malha viária com múltiplas threads, controle de exclusão mútua por semáforos ou monitores, e uma interface gráfica em Java Swing.

# Integrantes: 

Maria Eduarda Jonck e Manoella Marques Felippe.

# Pré-requisitos
- Java JDK 17 ou superior
- [Apache Maven](https://maven.apache.org/) instalado

Verifique se os comandos abaixo funcionam no terminal:
```bash
java -version
mvn -version
```

# Como compilar e executar

1. Clone o repositório:
```bash
git clone https://github.com/seu-usuario/seu-repositorio.git
cd seu-repositorio
```

2. Compile o projeto:
```bash
mvn clean compile
```

3. Execute o simulador:
```bash
mvn exec:java
```

# Executar via NetBeans

Na pasta /docs possui um zip para executar diretamente no NetBeans.

# Teste de Funcionamento

Ao executar, a aplicação exibirá uma janela gráfica com a malha viária lida de um arquivo `.txt` e veículos iniciando seu trajeto. Eles seguirão regras de tráfego e respeitarão o controle de concorrência nos cruzamentos.

---

# Observações

- O arquivo `malha-exemplo[N].txt` contém a definição da malha viária.
- As threads representam veículos independentes.
- O mecanismo de exclusão pode ser alternado entre `ExclusaoMonitor` e `ExclusaoSemaforo`.

---

# Licença

Este projeto é acadêmico e livre para fins educacionais.
