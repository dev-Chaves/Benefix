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

