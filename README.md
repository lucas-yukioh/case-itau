# API de Transferência
Repositório contendo API de Transferência.

Englobando cadastro de clientes, busca de clientes, cadastro de transferências e busca de transferências relacionadas aos clientes.

## Tecnologias Utilizadas
- Java 21
- Spring Boot
- Spring Actuator
- JPA / Hibernate
- Maven
- H2 Database
- Lombok
- Swagger

## Passos para executar a aplicação
Clonar o repositório
```
git clone https://github.com/lucas-yukioh/case-itau.git
```
Executar o comando no terminal
```
./mvnw spring-boot:run
```
## Documentação
Após subir a aplicação, toda a documentação estará disponível acessando o swagger através da url:

http://localhost:8080/case/v1/swagger-ui/index.html

## Healthcheck
O status da aplicação pode ser consultado através da url:

http://localhost:8080/case/v1/actuator/health
