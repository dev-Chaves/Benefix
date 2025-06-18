# Desafio Hackaton API

API para gerenciamento de benefícios corporativos, parcerias entre empresas e integração com sistema de dúvidas via IA.

## Índice
- [Sobre o Projeto](#sobre-o-projeto)
- [Funcionalidades](#funcionalidades)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Pré-requisitos](#pré-requisitos)
- [Configuração e Execução](#configuração-e-execução)
  - [Clonando o Repositório](#clonando-o-repositório)
  - [Variáveis de Ambiente](#variáveis-de-ambiente)
  - [Build do Projeto](#build-do-projeto)
  - [Executando a Aplicação](#executando-a-aplicação)
- [Banco de Dados](#banco-de-dados)
- [Testes](#testes)
- [Documentação da API](#documentação-da-api)
  - [Swagger UI](#swagger-ui)
  - [Principais Endpoints](#principais-endpoints)
- [Licença](#licença)

---

## Sobre o Projeto

Esta API permite:
- Cadastro de empresas, colaboradores e benefícios.
- Parcerias entre empresas para compartilhamento de benefícios.
- Autenticação via JWT.
- Sistema de dúvidas integrado à IA.

## Funcionalidades

- **Autenticação:**
  - Login de usuários (colaboradores/admin).
  - Registro de usuário ADMIN (com token de registro).
- **Administração:**
  - Cadastro de colaboradores, empresas, benefícios e parcerias.
- **Usuário:**
  - Visualização de benefícios próprios e de parceiros.
  - Filtro de benefícios por categoria.
  - Envio de dúvidas para IA.
- **Outros:**
  - Health Check.
  - CORS amplo (desenvolvimento).
  - Tratamento global de exceções.
  - Dados de exemplo para desenvolvimento/teste.

## Tecnologias Utilizadas

- **Backend:** Java 21, Spring Boot 3.4.5, Spring Web, Spring Data JPA, Spring Security
- **Banco de Dados:** PostgreSQL
- **Autenticação:** JWT (`java-jwt`), BCrypt
- **Documentação:** Swagger/OpenAPI (Springdoc)
- **Build:** Maven 3.9.9+
- **Contêinerização:** Docker, Docker Compose
- **Testes:** JUnit 5, Mockito, Spring Test
- **Migração de Banco:** Flyway
- **Load Balancer:** Nginx
- **Queries:** SQL Nativo, JPA/Hibernate

## Pré-requisitos

- Java JDK 21+
- Maven 3.9.x+ (ou Maven Wrapper)
- Docker e Docker Compose
- Git

## Configuração e Execução

### Clonando o Repositório
```bash
git clone <url-do-repositorio>
cd HACKATON-REPO
```

### Variáveis de Ambiente

Crie um arquivo `.env` ou configure as variáveis conforme abaixo:

#### Banco de Dados (docker-compose)
```
SPRING_DATASOURCE_URL=jdbc:postgresql://database:5432/hackathondb
SPRING_DATASOURCE_USERNAME=hackathonuser
SPRING_DATASOURCE_PASSWORD=hackathonpass
POSTGRES_USER=hackathonuser
POSTGRES_PASSWORD=hackathonpass
POSTGRES_DB=hackathondb
```

#### JWT
```
API_SECURITY_TOKEN_SECRET=seu-segredo-super-secreto-aqui
```

### Build do Projeto
```bash
mvn clean install
```

### Executando a Aplicação
```bash
docker-compose up
```

## Banco de Dados

### Migrações com Flyway

O projeto utiliza Flyway para controle de versão do banco de dados. As migrações estão localizadas em `src/main/resources/db/migration/`:

- `V1__Create_Initial_Tables.sql`: Criação inicial das tabelas
- `V2__add_performance_indexes_to_benefit_entity.sql`: Adição de índices de performance

As migrações são executadas automaticamente quando a aplicação inicia.

### Queries SQL

O projeto utiliza uma combinação de:
- JPA/Hibernate para operações CRUD básicas
- Queries SQL nativas para operações complexas
- Índices otimizados para melhor performance

Os scripts SQL iniciais podem ser encontrados em:
- `data.sql`: Dados iniciais para desenvolvimento
- `src/main/resources/db/migration/`: Scripts de migração

## Infraestrutura

### Load Balancing com Nginx

A aplicação utiliza Nginx como load balancer, com as seguintes características:

- Configuração em `src/main/resources/nginx/`
- Balanceamento entre múltiplas instâncias da aplicação
- Configuração de proxy reverso
- Health checks integrados

### Escalabilidade

A aplicação está configurada para rodar com:
- Múltiplas réplicas (2 por padrão)
- Load balancing automático
- Persistência de dados em volume Docker
- Health checks para garantir disponibilidade

## Testes

Os testes podem ser executados com o seguinte comando:
```bash
mvn test
```

## Documentação da API

### Swagger UI

Após iniciar a aplicação, acesse:
```
http://localhost:8080/swagger-ui.html
```

### Principais Endpoints

#### Autenticação e Usuários (`/user`)
- `POST /user/login`: Login de usuário
- `POST /user/register`: Registro de ADMIN (requer token)

#### Administração (`/admin`)
- `POST /admin/user`: Criar colaborador
- `POST /admin/benefit`: Criar benefício
- `POST /admin/enterprise`: Criar empresa
- `POST /admin/partnership`: Criar parceria

#### Benefícios (`/benefits`)
- `GET /benefits/benefits-by-enterprise`: Benefícios da empresa do usuário
- `GET /benefits/user-benefits`: Benefícios de empresas parceiras
- `GET /benefits/category/{category}`: Benefícios por categoria

#### Dúvidas IA (`/ia`)
- `POST /ia/doubt`: Enviar dúvida para IA

#### API Geral (`/api`)
- `GET /api/health`: Health check

#### Endpoints de Teste (`/user`)
- `GET /user/enterprise`: Listar empresas
- `GET /user/users`: Listar usuários
- `GET /user/benefit`: Listar benefícios

#### Segurança
- Endpoints `/user/**`, `/api/**` e Swagger: acesso público
- Endpoints `/admin/**`, `/benefits/**`, `/ia/**`: requerem autenticação JWT
- Envie o token JWT no header: `Authorization: Bearer <token>`

## Licença

Este projeto está licenciado sob a licença MIT. Veja o arquivo `LICENSE` para mais detalhes.
