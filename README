# Sistema de Folha de Pagamento com Spring Boot

## Descrição
Este projeto é um Sistema de Folha de Pagamento (Payroll) desenvolvido em Java utilizando o framework Spring Boot. O sistema é capaz de calcular salários líquidos, aplicar descontos legais como INSS e IRRF, gerenciar benefícios e gerar holerites em PDF. Os dados são persistidos em um banco de dados MySQL através do Spring Data JPA, e as funcionalidades são expostas via uma API REST. 

## Funcionalidades Principais

### Gerenciamento de Funcionários
* Permite adicionar, editar, excluir e listar funcionários via endpoints REST. 
* Campos obrigatórios para cadastro incluem Nome, Cargo, Salário Bruto e CPF.

### Cálculo de Folha de Pagamento
* Calcula o salário líquido considerando descontos de INSS (alíquotas progressivas) e IRRF (faixas de tributação). 
* Gerencia benefícios (como Vale-Transporte e Plano de Saúde) que podem ser descontos ou adicionais.

### Geração de Holerite
* Gera holerites em formato PDF (ou HTML, se desejado).
* O holerite contém dados do funcionário, salário bruto e líquido, e o detalhamento de todos os descontos. 

### Persistência e API
* Utiliza MySQL para armazenamento de dados com Spring Data JPA.
* Expõe as funcionalidades através de uma API REST para fácil integração e testes.

## Tecnologias Utilizadas 
* **Linguagem:** Java (JDK 11+ especificado no PDF, projeto usa JDK 21)
* **Framework:** Spring Boot
* **Persistência:** Spring Data JPA (com Hibernate)
* **Banco de Dados:** MySQL
* **Geração de PDF:** OpenHTMLtoPDF (conforme `pom.xml`)
* **API:** Spring Web (REST Controllers)
* **Build Tool:** Maven

## Requisitos Técnicos

### Dependências Principais (conforme `pom.xml`)
* `spring-boot-starter-data-jpa`
* `spring-boot-starter-web`
* `mysql-connector-j`
* `openhtmltopdf-pdfbox` (para geração de PDF)
* `springdoc-openapi-starter-webmvc-ui` (para documentação da API com Swagger)
* `hibernate-validator` (para validações)
* `spring-boot-starter-data-rest`

### Instalação
1.  **Pré-requisitos:**
    * Java JDK 21 ou superior instalado.
    * Maven instalado.
    * Servidor MySQL em execução.
2.  **Clone o repositório:**
    ```bash
    git clone <URL_DO_REPOSITORIO_DO_USUARIO>
    cd spring-boot-payroll-app
    ```
3.  **Configure o Banco de Dados:**
    * Crie um banco de dados no MySQL chamado `empresa` (ou o nome que desejar).
    * Atualize as credenciais do banco de dados no arquivo `src/main/resources/application.properties`: 
        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/empresa # Atualize se necessário
        spring.datasource.username=seu_usuario_mysql
        spring.datasource.password=sua_senha_mysql
        ```
4.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```
    Ou execute a classe principal `PayrollApplication.java` a partir da sua IDE.

## Como Utilizar

Após iniciar a aplicação:

1.  **Acessar a Documentação da API (Swagger UI):**
    * Abra seu navegador e acesse: `http://localhost:8080/ui.html` (ou o caminho configurado em `application.properties` para `springdoc.swagger-ui.path`).
2.  **Interagir com os Endpoints:**
    Utilize os endpoints da API para:
    * **Cadastrar Funcionários:** Ex: `POST /funcionarios` 
    * **Listar Funcionários:** Ex: `GET /funcionarios`
    * **Buscar Funcionário por ID:** Ex: `GET /funcionarios/{id}`
    * **Cadastrar Tipos de Benefícios:** Ex: `POST /beneficios`
    * **Associar Benefícios a Funcionários:** Ex: `POST /funcionario_beneficio`
    * **(Implementar) Calcular Folha de Pagamento:** Ex: `POST /api/folha-pagamento/calcular` (sugerido no PDF) 
    * **(Implementar) Gerar Holerite:** Ex: `GET /api/holerite/{funcionarioId}/{mes}/{ano}` (sugerido no PDF)

    *Nota: Alguns endpoints específicos de cálculo e geração de holerite podem precisar ser desenvolvidos conforme a lógica de negócio detalhada no PDF `Sistema de Folha de Pagamento.pdf`.*

## Exemplo de Interação (Conforme Repositórios REST Expostos)

### Terminal (usando cURL ou similar)

**Adicionar um funcionário:**
```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "name": "João da Silva",
  "cpf": "123.456.789-00",
  "position": "Desenvolvedor Backend",
  "grossSalary": 5000.00
}' http://localhost:8080/funcionarios
