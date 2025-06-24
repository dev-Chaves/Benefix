# Relatório de Testes de Carga – Login e Consulta de Benefícios

Este repositório documenta os resultados de três testes de carga realizados utilizando o JMeter, simulando o fluxo de login (POST) seguido de consulta de benefícios (GET). O objetivo foi avaliar o desempenho e a escalabilidade do sistema sob diferentes níveis de concorrência.

## Cenário dos Testes

- **Fluxo testado:** Login (POST) + Consulta de Benefícios (GET)
- **Ferramenta:** JMeter
- **Duração de cada teste:** 60 segundos
- **Variável:** Número de usuários virtuais (threads)

## Resultados

| Teste      | Threads | Amostras Totais | Tempo Médio (ms) | Mediana (ms) | 90% ≤ (ms) | 99% ≤ (ms) | Máx (ms) | Erro (%) | Throughput (req/s) |
|------------|---------|-----------------|------------------|--------------|------------|------------|----------|----------|--------------------|
| thread10   | 10      | 14.872          | 39               | 38           | 57         | 73         | 100      | 0        | 247,8              |
| thread100  | 100     | 24.568          | 238              | 222          | 455        | 727        | 1.394    | 0        | 407,8              |
| thread1000 | 1000    | 23.449          | 2.605            | 2.355        | 4.152      | 5.930      | 10.891   | 0        | 365,8              |

## Análise

- **10 threads:** Sistema respondeu rapidamente (média de 39 ms), sem erros e com throughput de 247,8 req/s.
- **100 threads:** Tempo médio subiu para 238 ms, throughput aumentou para 407,8 req/s, sem erros.
- **1000 threads:** Tempo médio de resposta aumentou para 2.605 ms, throughput caiu para 365,8 req/s, sem erros, mas com sinais claros de saturação.

## Conclusões

- O sistema é estável, sem apresentar erros mesmo sob alta carga.
- Escala bem até 100 usuários simultâneos, mantendo tempos de resposta aceitáveis.
- Acima de 100 usuários, há degradação significativa no tempo de resposta, indicando saturação do sistema.
- Recomenda-se manter a concorrência abaixo de 100 usuários simultâneos para garantir boa experiência ao usuário, ou investir em otimizações para suportar cargas maiores.

## Gráficos

![10 Threads Simultâneas](/docs/images/thread10-60s-csvarrumado.png "thread10")
![100 Threads Simultâneas](/docs/images/thread100-hash-60s.png "thread100")
![1000 Threads Simultâneas](/docs/images/thread1000-hash-60s.png "thread1000")

![1000 Threads Simultâneas](/docs/images/gráfico.png "gráfico")





---

*Relatório gerado automaticamente a partir dos resultados dos testes de carga.*