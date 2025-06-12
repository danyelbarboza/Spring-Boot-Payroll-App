# Sistema de Folha de Pagamento com Spring Boot

## Descrição

Este projeto é um **Sistema de Folha de Pagamento (Payroll)** desenvolvido em Java com o framework Spring Boot. O sistema realiza o cálculo de salários líquidos, aplicando os descontos legais de INSS e IRRF, gerencia benefícios dos funcionários e gera contracheques detalhados em formato PDF. Os dados são persistidos em um banco de dados MySQL com Spring Data JPA, e as funcionalidades são expostas através de uma API RESTful.

## Funcionalidades Principais

### Gerenciamento de Funcionários e Benefícios

  * **Funcionários**: Adição, edição, exclusão e listagem de funcionários através de endpoints REST expostos automaticamente pelo Spring Data REST (`/funcionarios`).
  * **Benefícios**: Gerenciamento completo de tipos de benefícios (ex: Vale-Transporte, Plano de Saúde) via endpoints REST (`/beneficios`).
  * **Associação**: Permite associar benefícios específicos a cada funcionário de forma flexível (`/funcionario_beneficio`).

### Cálculo Automatizado da Folha de Pagamento

  * **Cálculo Preciso**: O `CalculadoraSalario.java` calcula o salário líquido considerando as alíquotas progressivas de **INSS** e as faixas de tributação do **IRRF**, garantindo conformidade com a legislação.
  * **Gestão de Benefícios**: Aplica os benefícios associados ao funcionário, tratando-os como proventos (ex: Auxílio Creche) ou descontos (ex: Plano de Saúde).
  * **Endpoints Dedicados**:
      * `POST /api/folha-pagamento/calcular/{funcionarioId}`: Calcula a folha para um funcionário específico.
      * `POST /api/folha-pagamento/calcular`: Calcula a folha para todos os funcionários cadastrados.

### Geração de Contracheque em PDF

  * **Geração Automática**: Após o cálculo da folha, um contracheque em **PDF** é gerado automaticamente.
  * **Tecnologia**: Utiliza a biblioteca **iText7** e templates **Thymeleaf** (`ContraCheque.html`) para criar PDFs a partir de um modelo HTML dinâmico.
  * **Armazenamento**: Os PDFs são salvos no diretório `./target/htmlsamples/ch01/` no servidor.
  * **Detalhes do Contracheque**: Inclui dados da empresa, do funcionário, salário bruto e líquido, detalhamento de proventos e descontos, e bases de cálculo (FGTS, INSS, IRRF).

### API REST e Persistência

  * **Banco de Dados**: Utiliza **MySQL** para armazenamento de dados, gerenciado pelo Spring Data JPA.
  * **API RESTful**: Expõe as funcionalidades através de uma API REST, utilizando Spring Web e Spring Data REST para um desenvolvimento rápido e padronizado.
  * **Gerenciamento de Contracheques**: Permite listar e excluir os contracheques gerados via endpoints no `FolhaPagamentoController`.

## Tecnologias Utilizadas

  * **Linguagem**: **Java 21**
  * **Framework**: **Spring Boot 3.3.0**
  * **Persistência**: Spring Data JPA (com Hibernate)
  * **Banco de Dados**: MySQL
  * **Geração de PDF**: iText7 com `html2pdf`
  * **Templating**: Thymeleaf (para o template do contracheque)
  * **API**: Spring Web (REST Controllers) e Spring Data REST
  * **Documentação da API**: SpringDoc OpenAPI (Swagger UI)
  * **Validações**: Hibernate Validator
  * **Build Tool**: Maven
  * **Testes**: JUnit 5, Mockito e H2 (banco de dados em memória)

## Como Executar o Projeto

### Pré-requisitos

  * Java JDK 21 ou superior.
  * Maven 3.9 ou superior.
  * Servidor MySQL em execução.

### Passos para Instalação

1.  **Clone o repositório:**

    ```bash
    git clone https://github.com/danyelbarboza/Spring-Boot-Payroll-App.git
    cd seu-repositorio
    ```

2.  **Configure o Banco de Dados:**

      * Crie um banco de dados no MySQL com o nome `empresa`.
      * Atualize as credenciais do banco de dados no arquivo `src/main/resources/application.properties`:
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/empresa
        spring.datasource.username=seu_usuario_mysql
        spring.datasource.password=sua_senha_mysql
        ```

3.  **Execute a aplicação:**

    ```bash
    mvn spring-boot:run
    ```

    Ou execute a classe `PayrollApplication.java` a partir da sua IDE.

## Como Utilizar a API

Após iniciar a aplicação, acesse a documentação interativa da API (Swagger UI) para testar os endpoints:

  * **URL do Swagger UI**: [http://localhost:8080/ui.html](http://localhost:8080/ui.html)

### Exemplos de Requisições com cURL

#### 1\. Cadastrar um Funcionário

```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "name": "João da Silva",
  "cpf": "123.456.789-00",
  "position": "Desenvolvedor Backend",
  "grossSalary": 5000.00
}' http://localhost:8080/funcionarios
```

#### 2\. Cadastrar um Tipo de Benefício

```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "name": "Plano de Saúde",
  "defaultValue": 250.00,
  "isDiscount": true
}' http://localhost:8080/beneficios
```

#### 3\. Associar um Benefício a um Funcionário

*Primeiro, obtenha a URI do funcionário (ex: `/funcionarios/1`) e do benefício (ex: `/beneficios/1`).*

```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "funcionario": "http://localhost:8080/funcionarios/1",
  "beneficio": "http://localhost:8080/beneficios/1"
}' http://localhost:8080/funcionario_beneficio
```

#### 4\. Calcular a Folha de Pagamento e Gerar o Contracheque

*Para o funcionário com ID 1:*

```bash
curl -X POST http://localhost:8080/api/folha-pagamento/calcular/1
```

Após a execução, o PDF do contracheque será salvo no diretório `./target/htmlsamples/ch01/`.

#### 5\. Listar Contracheques de um Funcionário

```bash
curl -X GET http://localhost:8080/api/folha-pagamento/contracheques/1
```