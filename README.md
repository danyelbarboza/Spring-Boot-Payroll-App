# Sistema de Folha de Pagamento com Spring Boot

## Descrição
Este projeto é um Sistema de Folha de Pagamento (Payroll) desenvolvido em Java utilizando o framework Spring Boot. O sistema é capaz de calcular salários líquidos, aplicar descontos legais como INSS e IRRF, gerenciar benefícios e gerar contracheques em PDF. Os dados são persistidos em um banco de dados MySQL através do Spring Data JPA, e as funcionalidades são expostas via uma API REST.

## Funcionalidades Principais

### Gerenciamento de Funcionários
* Permite adicionar, editar, excluir e listar funcionários via endpoints REST (expostos automaticamente pelo Spring Data REST em `/funcionarios`).
* Campos obrigatórios para cadastro incluem Nome, CPF, Cargo e Salário Bruto, conforme definido na entidade `Funcionarios`.

### Gerenciamento de Benefícios
* Permite adicionar, editar, excluir e listar tipos de benefícios (ex: Vale-Transporte, Plano de Saúde) via endpoints REST (expostos automaticamente pelo Spring Data REST em `/beneficios`).
* Permite associar benefícios específicos a funcionários via endpoints REST (expostos automaticamente pelo Spring Data REST em `/funcionario_beneficio`).
* Benefícios podem ser configurados como descontos ou adicionais ao salário.

### Cálculo de Folha de Pagamento
* Calcula o salário líquido considerando descontos de INSS (com alíquotas progressivas) e IRRF (com faixas de tributação), conforme implementado em `CalculadoraSalario.java`.
* Gerencia benefícios associados ao funcionário, aplicando-os como descontos ou adicionais no cálculo.
* Endpoints dedicados para acionar o cálculo da folha de pagamento para um funcionário específico ou para todos os funcionários cadastrados.

### Geração de Contracheque
* Gera contracheques em formato PDF automaticamente após o cálculo da folha de pagamento.
* Os PDFs são gerados utilizando a biblioteca iText7 e templates Thymeleaf (`ContraCheque.html`), sendo salvos no diretório `./target/htmlsamples/ch01/` no servidor.
* O contracheque contém dados da empresa, do funcionário, salário bruto e líquido, detalhamento de proventos, descontos (INSS, IRRF, benefícios) e bases de cálculo (FGTS, etc.).

### Persistência e API
* Utiliza MySQL para armazenamento de dados com Spring Data JPA.
* Expõe as funcionalidades através de uma API REST para fácil integração e testes, utilizando Spring Web e Spring Data REST.
* Permite gerenciar (`CRUD`) os contracheques gerados através de endpoints REST.

## Tecnologias Utilizadas
* **Linguagem:** Java (JDK 21)
* **Framework:** Spring Boot
* **Persistência:** Spring Data JPA (com Hibernate)
* **Banco de Dados:** MySQL
* **Geração de PDF:** iText7 (com `html2pdf` para conversão de HTML para PDF)
* **Templating:** Thymeleaf (usado para o template do contracheque em PDF)
* **API:** Spring Web (REST Controllers) e Spring Data REST
* **Documentação da API:** SpringDoc OpenAPI (Swagger UI)
* **Validações:** Hibernate Validator
* **Build Tool:** Maven

## Requisitos Técnicos

### Dependências Principais (conforme `pom.xml`)
* `spring-boot-starter-data-jpa`
* `spring-boot-starter-web`
* `spring-boot-starter-data-rest`
* `spring-boot-starter-thymeleaf`
* `mysql-connector-j`
* `itext7-core`, `layout`, `html2pdf` (para geração de PDF)
* `springdoc-openapi-starter-webmvc-ui` (para documentação da API com Swagger)
* `hibernate-validator` (para validações)
* `spring-boot-devtools` (opcional, para desenvolvimento)

### Instalação
1.  **Pré-requisitos:**
    * Java JDK 21 ou superior instalado.
    * Maven instalado.
    * Servidor MySQL em execução.
2.  **Clone o repositório:**
    ```bash
    git clone https://github.com/your-username/payroll.git
    cd payroll
    ```
3.  **Configure o Banco de Dados:**
    * Crie um banco de dados no MySQL chamado `empresa` (ou o nome que desejar, ajuste o `application.properties` se necessário).
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
    * Abra seu navegador e acesse: `http://localhost:8080/ui.html`
2.  **Interagir com os Endpoints:**
    Utilize os endpoints da API (disponíveis na documentação do Swagger UI) para:

    * **Gerenciar Funcionários (via Spring Data REST em `/funcionarios`):**
        * `POST /funcionarios`: Cadastrar um novo funcionário.
            * Exemplo de corpo da requisição:
                ```json
                {
                  "name": "João da Silva",
                  "cpf": "123.456.789-00",
                  "position": "Desenvolvedor Backend",
                  "grossSalary": 5000.00
                }
                ```
        * `GET /funcionarios`: Listar todos os funcionários.
        * `GET /funcionarios/{id}`: Buscar funcionário por ID.
        * `PUT /funcionarios/{id}`: Atualizar um funcionário.
        * `DELETE /funcionarios/{id}`: Excluir um funcionário.

    * **Gerenciar Tipos de Benefícios (via Spring Data REST em `/beneficios`):**
        * `POST /beneficios`: Cadastrar um novo tipo de benefício.
            * Exemplo de corpo da requisição:
                ```json
                {
                  "name": "Vale-Transporte",
                  "defaultValue": 150.00,
                  "isDiscount": true
                }
                ```
        * `GET /beneficios`: Listar todos os tipos de benefícios.

    * **Associar Benefícios a Funcionários (via Spring Data REST em `/funcionario_beneficio`):**
        * `POST /funcionario_beneficio`: Associar um benefício a um funcionário.
            * Exemplo de corpo da requisição (usando URIs para as entidades relacionadas):
                ```json
                {
                  "funcionario": "/funcionarios/1", // URI do funcionário
                  "beneficio": "/beneficios/1"    // URI do tipo de benefício
                }
                ```
        * `GET /funcionario_beneficio`: Listar todas as associações.
        * `GET /funcionario_beneficio/search/findByFuncionario_Id?funcionarioId={id}`: Listar benefícios de um funcionário específico.

    * **Calcular Folha de Pagamento e Gerar Contracheque (via `FolhaPagamentoController`):**
        * `POST /api/folha-pagamento/calcular/{funcionarioId}`: Calcula a folha de pagamento para um funcionário específico. O contracheque em PDF será salvo no servidor.
        * `POST /api/folha-pagamento/calcular`: Calcula a folha de pagamento para todos os funcionários. Os contracheques em PDF serão salvos no servidor.

    * **Gerenciar Contracheques (via `FolhaPagamentoController` e Spring Data REST):**
        * Controller (`/api/folha-pagamento/contracheques`):
            * `GET /api/folha-pagamento/contracheques`: Listar todos os contracheques salvos.
            * `GET /api/folha-pagamento/contracheques/{id}`: Buscar contracheque por seu ID.
            * `GET /api/folha-pagamento/contracheques/funcionario/{funcionarioId}`: Listar contracheques de um funcionário específico (Observação: o endpoint real no código é `/contracheques/{funcionarioId}`, este precisa ser verificado ou ajustado no controller se o mapeamento `funcionario/{funcionarioId}` for desejado. O código atual do `FolhaPagamentoController` usa `@GetMapping("/contracheques/{funcionarioId}")` mas este parece ser para buscar *contracheques* e não uma lista por ID de funcionário. O método `getContrachequesByFuncionarioId` está correto.
            * `DELETE /api/folha-pagamento/contracheques/{id}`: Excluir um contracheque.
            * `DELETE /api/folha-pagamento/contracheques`: Excluir todos os contracheques.
        * Spring Data REST (`/contracheques`):
            * Endpoints CRUD para a entidade `ContraCheque` também são expostos aqui.

## Exemplo de Interação (Conforme Repositórios REST Expostos)

### Terminal (usando cURL ou similar)

**Adicionar um funcionário:**
```bash
curl -X POST -H "Content-Type: application/json" -d '{
  "name": "Maria Souza",
  "cpf": "987.654.321-99",
  "position": "Analista de RH",
  "grossSalary": 4500.00
}' http://localhost:8080/funcionarios
```
Listar funcionários:

```bash
curl -X GET http://localhost:8080/funcionarios
```
Calcular folha de pagamento para o funcionário com ID 1:

```bash
curl -X POST http://localhost:8080/api/folha-pagamento/calcular/1
```
(Após este comando, o PDF do contracheque será salvo em ./target/htmlsamples/ch01/)