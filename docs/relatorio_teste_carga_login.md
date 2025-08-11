
# 📋 Relatório de Teste de Carga - Login API

## ✅ Informações Gerais

- **Endpoint testado:** `user/login`
- **Tipo de requisição:** `POST`
- **Payload:** JSON com credenciais de login
- **Ferramenta:** Apache JMeter
- **Data do teste:** `20/06/2025`
- **Ambiente:** Docker

## ✅ Objetivo do Teste

O objetivo do teste foi avaliar o desempenho da API de login sob carga, simulando **16.335 requisições consecutivas**, verificando o tempo de resposta médio, picos, estabilidade (desvio padrão) e taxa de erros.

## ✅ Configuração do Teste

- **Número total de requisições (samples):** `16.335`
- **Tipo de carga:** Contínua
- **Simulação de usuários simultâneos:** (Informe aqui quantas threads usou no JMeter)

## ✅ Resultados

### 📈 Métricas do Summary Report:

| Métrica              | Valor             |
|----------------------|------------------|
| Tempo médio (Average) | **1.538 ms** |
| Tempo mínimo (Min) | **1 ms** |
| Tempo máximo (Max) | **7.011 ms** |
| Desvio padrão (Std Dev) | **888,08 ms** |
| Taxa de erros (Error %) | **0,99%** |
| Throughput | **54,1 requisições/segundo** |
| Dados recebidos | **32,36 KB/s** |
| Dados enviados | **15,16 KB/s** |
| Tamanho médio da resposta | **612,1 bytes** |

### 📊 Métricas do Aggregate Report (com Percentis):

| Métrica | Valor |
|---|---|
| Mediana (P50) | **1.659 ms** |
| 90% das requisições (P90) | **2.568 ms** |
| 95% das requisições (P95) | **3.169 ms** |
| 99% das requisições (P99) | **4.110 ms** |
| Máximo | **7.011 ms** |

## ✅ Análise de Performance

- **Desempenho médio:** Aceitável, mas pode melhorar.  
- **Latência:** A maioria das requisições ficaram abaixo de 3 segundos, porém, **1% dos usuários receberam resposta entre 4 e 7 segundos**, o que pode ser crítico dependendo do SLA.
- **Taxa de erro:** **0,99%** → Limite de tolerância para muitos sistemas. **Recomenda-se investigar os motivos desses erros (HTTP 500, 400, etc)**.
- **Estabilidade:** O **desvio padrão alto (888 ms)** indica que o desempenho não foi consistente. Pode haver picos de latência.

## ✅ Recomendação Técnica

- **Otimizar o tempo de resposta médio e reduzir a variância:**  
  Revisar performance do backend, banco de dados, caches e filas de processamento.

- **Analisar os 1% mais lentos:**  
  Verificar logs e monitoramentos durante o teste (GC? CPU? Queries lentas?).

- **Investigar os erros:**  
  Avaliar se os 0,99% de falhas são aceitáveis ou se indicam problema de escalabilidade.

## ✅ Próximos passos sugeridos

- Rodar o mesmo teste com carga maior (se o ambiente permitir).
- Executar testes de endurance (longa duração).
- Ajustar configurações de infraestrutura (threads, pools, etc).
