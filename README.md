# Magic-Application
## Introdução
Este projeto foi criado como um desafio da iniciativa MasterDevs. Tem por objetivo simular o jogo Magic.

#Documentação
Para acessar a documentação, suba o projeto e acesse a url:
```
http://localhost:8080/swagger-ui.html
```

## Ambiente de desenvolvimento
Tecnologias utilizadas:
* Java 8
* Maven
* JUnit
* Mockito
* Swagger
* Flyway
 
Antes de rodar o projeto no ambiente de desenvolvimento, é necessário executar o seguinte comando
para instalar as dependencias necessárias:
```
mvn clean install
```
## Build
Todos os comandos listados a seguir, devem ser executados a partir da raíz do projeto.

Para compilar o projeto, utilize:
```
mvn build
```

Para a execução dos testes, utilize:
```
mvn test
```

Para execução da aplicação, utilize:
```
mvn spring-boot:run
```