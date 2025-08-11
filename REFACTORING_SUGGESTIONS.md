# Proposta de Refatoração e Melhorias no Projeto

Este documento descreve uma série de refatorações e melhorias sugeridas para o projeto, com o objetivo de aprimorar a qualidade do código, a segurança, o desempenho e a manutenibilidade.

## 1. Dependências e Configuração (`pom.xml`)

### 1.1. Atualizar Versão do Spring Boot
A versão atual do Spring Boot (`3.4.5`) não é uma versão estável. Recomenda-se o uso da última versão estável para garantir compatibilidade e segurança.

**Ação:** Atualizar a versão do `spring-boot-starter-parent` no `pom.xml`.

```xml
<!-- Antes -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.4.5</version>
    <relativePath/>
</parent>

<!-- Depois (Exemplo com uma versão estável) -->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>3.2.5</version>
    <relativePath/>
</parent>
```

### 1.2. Remover Dependência Legada do OAuth2
A dependência `spring-security-oauth2` é legada e não é mais necessária com o Spring Boot 3, que possui suporte nativo aprimorado para OAuth2 e JWT.

**Ação:** Remover a seguinte dependência do `pom.xml`:

```xml
<dependency>
    <groupId>org.springframework.security.oauth</groupId>
    <artifactId>spring-security-oauth2</artifactId>
    <version>2.5.2.RELEASE</version>
</dependency>
```

### 1.3. Consolidar Dependências do Swagger/OpenAPI
O projeto inclui `springdoc-openapi-starter-webmvc-ui` e `swagger-annotations`. A primeira já inclui as anotações necessárias, tornando a segunda redundante.

**Ação:** Remover a dependência `swagger-annotations` do `pom.xml`:

```xml
<dependency>
    <groupId>io.swagger</groupId>
    <artifactId>swagger-annotations</artifactId>
    <version>1.6.15</version>
</dependency>
```

## 2. Configuração da Aplicação (`application.yaml`)

### 2.1. Simplificar Configuração do Springdoc
A configuração `springdoc.group-configs.packages-to-scan` é excessivamente longa e, na maioria dos casos, desnecessária. O Spring Boot pode detectar controllers e DTOs automaticamente se a estrutura de pacotes padrão for seguida.

**Ação:** Remover ou simplificar drasticamente a seção `springdoc.group-configs` do `application.yaml`.

```yaml
# Remover esta seção inteira
springdoc:
  group-configs:
    - group: public
      packages-to-scan:
        - com.hackaton.desafio.controller
        - com.hackaton.desafio.dto
        # ... e muitas outras linhas
```

## 3. Código e Arquitetura

### 3.1. `SecurityConfig.java`

#### 3.1.1. Aumentar a Força do `BCryptPasswordEncoder`
A força de criptografia `8` é considerada baixa. O padrão recomendado é `10` ou superior.

**Ação:** Alterar o valor no construtor do `BCryptPasswordEncoder`.

```java
// Antes
@Bean
public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(8);
}

// Depois
@Bean
public PasswordEncoder passwordEncoder(){
    return new BCryptPasswordEncoder(10); // Ou o padrão, sem argumentos
}
```

#### 3.1.2. Revisar Permissões de Endpoint
Vários endpoints estão abertos com `permitAll()`, o que pode ser um risco de segurança.

**Ação:** Revisar as regras em `authorizeHttpRequests` para garantir que apenas os endpoints genuinamente públicos fiquem abertos.

```java
// Revisar estas regras
.authorizeHttpRequests(authorize -> authorize
    .requestMatchers("/user/**").permitAll() // Realmente tudo aqui é público?
    .requestMatchers("/api/**").permitAll()   // API inteira pública?
    // ...
)
```

### 3.2. `TokenService.java`

#### 3.2.1. Externalizar Tempo de Expiração do Token
O tempo de expiração do token está fixo no código. É uma boa prática torná-lo configurável.

**Ação:** Mover o valor para o `application.yaml` e injetá-lo com `@Value`.

```java
// application.yaml
jwt:
  secret: ${API_SECURITY_TOKEN_SECRET}
  expiration-time: 86400000 # 24 horas em ms

// TokenService.java
@Value("${jwt.expiration-time}")
private long expirationTime;
```

### 3.3. `BenefitService.java` e `BenefitController.java`

#### 3.3.1. Separar Responsabilidades (Controller vs. Service)
O serviço (`BenefitService`) está retornando `ResponseEntity`, misturando a lógica de negócio com a camada de apresentação (HTTP). O serviço deve retornar os dados (DTOs), e o controller deve montar a `ResponseEntity`.

**Ação:** Refatorar os métodos no serviço para retornarem `List<BenefitResponse>` e ajustar o controller.

```java
// BenefitService.java (Exemplo)
public List<BenefitResponse> getBenefitOfPartneship() {
    // ... lógica de negócio
    return benefits.stream().map(...).toList();
}

// BenefitController.java (Exemplo)
@GetMapping("user-benefits")
public ResponseEntity<List<BenefitResponse>> getBenefitOfPartneship() {
    List<BenefitResponse> benefits = benefitService.getBenefitOfPartneship();
    return ResponseEntity.ok(benefits);
}
```

### 3.4. `BenefitRepository.java`

#### 3.4.1. Substituir Queries Nativas
As queries nativas podem ser substituídas por métodos de query do Spring Data JPA, que são mais seguros (evitam SQL injection) e mais fáceis de manter.

**Ação:** Substituir as queries nativas por declarações de método no repositório.

```java
// Antes
@Query(value = "SELECT * FROM tb_benefit WHERE supplier_enterprise_id = :enterpriseId", nativeQuery = true)
List<BenefitEntity> findByEnterpriseId(@Param("enterpriseId") Long enterpriseId);

// Depois (O Spring Data JPA implementa isso automaticamente)
List<BenefitEntity> findBySupplierEnterpriseId(Long enterpriseId);
```
```java
// Antes
@Query(value = "SELECT * FROM tb_benefit WHERE supplier_enterprise_id IN :enterpriseIds", nativeQuery = true)
List<BenefitEntity> findBySupplierEnterpriseIdIn(@Param("enterpriseIds") List<Long> enterpriseIds);

// Depois
List<BenefitEntity> findBySupplierEnterpriseIdIn(List<Long> enterpriseIds);
```

## Plano de Ação Recomendado

1.  **Limpeza do `pom.xml`:** Aplicar as mudanças de dependência.
2.  **Configuração:** Simplificar o `application.yaml`.
3.  **Segurança:** Refatorar `SecurityConfig.java` e `TokenService.java`.
4.  **Código:** Refatorar as camadas de Controller, Service e Repository para seguir as melhores práticas de separação de responsabilidades e uso do Spring Data JPA.
5.  **Testes:** Após cada refatoração, rodar os testes existentes para garantir que nenhuma funcionalidade foi quebrada.
