# Desafio Hackaton API

API desenvolvida para o Hackaton, com foco em gerenciamento de benefícios para empresas e seus colaboradores.

## Índice

- [Descrição](#descrição)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Começando](#começando)
  - [Clonando o Repositório](#clonando-o-repositório)
  - [Variáveis de Ambiente](#variáveis-de-ambiente)
  - [Build do Projeto](#build-do-projeto)
- [Executando a Aplicação](#executando-a-aplicação)
  - [Usando Docker Compose (Recomendado)](#usando-docker-compose-recomendado)
  - [Localmente (Sem Docker)](#localmente-sem-docker)
- [Banco de Dados](#banco-de-dados)
  - [Inicialização](#inicialização)
- [Documentação da API (Swagger)](#documentação-da-api-swagger)
- [Visão Geral dos Endpoints](#visão-geral-dos-endpoints)
- [Testes](#testes)
- [Licença](#licença)

## Descrição

Esta API permite o cadastro de empresas, colaboradores e benefícios. Estabelece parcerias entre empresas para que os colaboradores de uma empresa possam usufruir dos benefícios oferecidos por empresas parceiras. A autenticação é realizada via JWT.

## Funcionalidades

- **Autenticação de Usuários:**
    - Login para usuários (colaboradores e admin).
    - Registro de usuário ADMIN inicial (requer um token de registro específico).
- **Operações de Administrador (requerem role ADMIN):**
    - Criar novos colaboradores (tipo USER) para empresas.
    - Cadastrar novos benefícios e associá-los a uma empresa fornecedora.
    - Cadastrar novas empresas.
    - Criar parcerias entre empresas.
- **Operações de Usuário:**
    - Visualizar benefícios oferecidos pela sua própria empresa.
    - Visualizar benefícios oferecidos por empresas parceiras.
    - Filtrar benefícios por categoria.
    - Registrar dúvidas/perguntas para um sistema de IA (funcionalidade de criação de dúvida).
- **Outras:**
    - Endpoint de Health Check para verificar o status da API.
    - Configuração de CORS permitindo acesso amplo (para desenvolvimento/hackathon).
    - Tratamento global de exceções.
    - Inicialização de dados de exemplo para ambiente de desenvolvimento/teste.

## Tecnologias Utilizadas

- **Backend:**
    - Java 21
    - Spring Boot 3.4.5
    - Spring Web
    - Spring Data JPA
    - Spring Security
- **Banco de Dados:**
    - PostgreSQL
- **Autenticação:**
    - JSON Web Tokens (JWT) com a biblioteca `java-jwt`.
    - BCrypt para hashing de senhas.
- **Documentação da API:**
    - Swagger/OpenAPI (via Springdoc).
- **Build e Gerenciamento de Dependências:**
    - Apache Maven (versão 3.9.9 via wrapper).
- **Contêinerização:**
    - Docker e Docker Compose.
- **Testes:**
    - JUnit 5
    - Mockito
    - Spring Test & Spring Security Test.
- **Outras bibliotecas:**
    - `spring-security-oauth2` (2.5.2.RELEASE) - *Nota: Esta é uma biblioteca mais antiga, o mecanismo principal de JWT parece ser via `java-jwt`.*

## Pré-requisitos

- Java JDK 21 ou superior.
- Apache Maven 3.9.x ou superior (ou utilize o Maven Wrapper incluído).
- Docker e Docker Compose (para execução via contêineres).
- Git.

## Começando

### Clonando o Repositório

```bash
git clone <url-do-repositorio>
cd Hackathon-tests
```
### Variáveis de Ambiente

### Configurações do Banco de Dados (usadas pelo docker-compose para o serviço 'api' e 'database')
SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/hackathondb
SPRING_DATASOURCE_USERNAME=hackathonuser
SPRING_DATASOURCE_PASSWORD=hackathonpass

### Credenciais para o container do PostgreSQL (usadas pelo serviço 'database')
POSTGRES_USER=hackathonuser
POSTGRES_PASSWORD=hackathonpass
POSTGRES_DB=hackathondb

### Segredo para a geração e validação de tokens JWT (usado pelo serviço 'api')
API_SECURITY_TOKEN_SECRET=seu-segredo-super-secreto-aqui

### Build do Projeto 
# No Linux ou macOS
./mvnw clean install

### No Windows
mvnw.cmd clean install

### Se instalado globalmente
mvn clean install

### Executando a aplicação
docker-compose up -d

### Usando o Maven Wrapper
./mvnw spring-boot:run

### Ou se o JAR foi construído (após mvn install)
java -jar target/desafio-0.0.1-SNAPSHOT.jar

### Testes

### Usando o Maven Wrapper
./mvnw test

## Ou
mvn test

### Documentação da API e Endpoints

A documentação completa e interativa da API está disponível através do Swagger UI. Após iniciar a aplicação, acesse o seguinte endereço no seu navegador:

`http://localhost:8080/swagger-ui.html`

Lá você poderá visualizar todos os endpoints, os modelos de dados para requisições e respostas, além de testar os endpoints diretamente pela interface.

### Visão Geral dos Endpoints Principais

A seguir, uma lista dos principais grupos de endpoints da aplicação:

---

#### Autenticação e Usuários (`/user`)
* **`POST /user/login`**: Realiza o login de um usuário no sistema.
    * **Requisição**: `LoginRequest` (name, password)
    * **Resposta (Sucesso)**: `LoginResponse` (name, token)
    * **Controlador**: `UserController`
* **`POST /user/register`**: Registra um novo usuário ADMIN no sistema.
    * Este endpoint requer um token de registro específico no corpo da requisição para autorizar a criação do usuário ADMIN.
    * **Requisição**: `RegisterDTO` (name, password, enterprise, token)
    * **Resposta (Sucesso)**: `RegisterResponse` (name, role)
    * **Controlador**: `UserController`

---

#### Operações de Administrador (`/admin`)
*Estes endpoints requerem autenticação e que o usuário possua a role `ADMIN`.*

* **`POST /admin/user`**: Cria um novo colaborador (usuário com role `USER`).
    * **Requisição**: `CreateUserRequest` (name, password, enterpriseId)
    * **Resposta (Sucesso)**: `UserResponse` (name, role, empresa)
    * **Controlador**: `AdminController`
* **`POST /admin/benefit`**: Cria um novo benefício.
    * **Requisição**: `BenefitRequest` (description, supplierEnterpriseId, category)
    * **Resposta (Sucesso)**: `BenefitResponse` (id, description, nameSupplierEnterprise, category)
    * **Controlador**: `AdminController`
* **`POST /admin/enterprise`**: Cria uma nova empresa.
    * **Requisição**: `EnterpriseRequest` (name, cnpj)
    * **Resposta (Sucesso)**: `EnterpriseResponse` (id, name, cnpj)
    * **Controlador**: `AdminController`
* **`POST /admin/partnership`**: Cria uma nova parceria entre empresas.
    * **Requisição**: `PartnershipRequest` (supplierEnterpriseId, consumerEnterpriseId)
    * **Resposta (Sucesso)**: `PartnershipResponse` (id, namePartnershipEnterprise, nameSupplierEnterprise)
    * **Controlador**: `AdminController`

---

#### Benefícios (`/benefits`)
*Estes endpoints requerem autenticação.*

* **`GET /benefits/benefits-by-enterprise`**: Retorna os benefícios oferecidos pela empresa do usuário autenticado.
    * **Resposta (Sucesso)**: Lista de `BenefitResponse`
    * **Controlador**: `BenefitController`
* **`GET /benefits/user-benefits`**: Retorna os benefícios das empresas parceiras da empresa do usuário autenticado.
    * **Resposta (Sucesso)**: Lista de `BenefitResponse`
    * **Controlador**: `BenefitController`
* **`GET /benefits/category/{category}`**: Retorna os benefícios (de empresas parceiras) filtrados por uma categoria específica.
    * **Parâmetro de Path**: `category` (ex: `MARKET`, `HEALTH`)
    * **Resposta (Sucesso)**: Lista de `BenefitResponse`
    * **Controlador**: `BenefitController`

---

#### Inteligência Artificial - Dúvidas (`/ia`)
*Este endpoint requer autenticação.*

* **`POST /ia/doubt`**: Cria uma nova dúvida/pergunta para o sistema de IA.
    * **Requisição**: `DoubtRequest` (question)
    * **Resposta (Sucesso)**: `DoubtResponse` (doubt, user, answered)
    * **Controlador**: `IAController`

---

#### API Geral (`/api`)
*Este endpoint é público.*

* **`GET /api/health`**: Verifica a saúde da aplicação.
    * **Resposta (Sucesso)**: String indicando que a API está rodando.
    * **Controlador**: `DemoController`

---

#### Endpoints de Teste/Visualização (`/user`)
*Estes endpoints são públicos e parecem ser para visualização rápida de dados durante o desenvolvimento.*

* **`GET /user/enterprise`**: Lista todas as empresas cadastradas.
    * **Resposta (Sucesso)**: Lista de `EnterpriseResponse`
* **`GET /user/users`**: Lista todos os usuários cadastrados.
    * **Resposta (Sucesso)**: Lista de `UserResponse`
* **`GET /user/benefit`**: Lista todos os benefícios cadastrados.
    * **Resposta (Sucesso)**: Lista de `BenefitResponse`
* **Controlador**: `TesteController`

---

**Observações de Segurança:**
* Os endpoints sob `/user/**` e `/api/**`, bem como os caminhos do Swagger, estão configurados para permitir acesso público (`permitAll`).
* Todos os outros endpoints (`/admin/**`, `/benefits/**`, `/ia/**`) requerem autenticação.
* A autenticação é baseada em Token JWT, que deve ser enviado no header `Authorization` como `Bearer <token>`.
