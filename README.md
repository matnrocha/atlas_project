# A.T.L.A.S. – Advanced Tracking Legendary Automated Spaceship

**Real-time spaceship monitoring system — Built with Spring Boot, Kafka, PostgreSQL, and React.**

## Sobre o Projeto

A.T.L.A.S. é um sistema inteligente de monitoramento em tempo real para operações espaciais. Desenvolvido como projeto principal do curso de Introdução à Engenharia de Software na Universidade de Aveiro, o sistema foi projetado para simular o acompanhamento de métricas críticas de uma espaçonave, emitir alertas sobre anomalias e apresentar dados de forma clara e acessível.

Este projeto destaca-se pela arquitetura robusta, uso de tecnologias modernas e ênfase em boas práticas de engenharia de software.

## Funcionalidades

- **Monitoramento em tempo real:** posição, velocidade, trajetória e recursos internos da nave (energia, oxigênio, etc).
- **Alertas inteligentes:** notificações visuais e sonoras para situações críticas (temperatura, oxigênio, falhas de comunicação, etc).
- **Relatórios analíticos:** geração de resumos de missão e gráficos históricos.
- **Interface amigável:** painel com dados dinâmicos e responsivos via WebSockets.
- **Personas realistas:** sistema orientado a diferentes perfis de usuário (CEO, engenheira de bordo, médico, diretora de voo).

## Tecnologias Utilizadas

| Camada        | Tecnologias                     |
|---------------|----------------------------------|
| Backend       | Java, Spring Boot, WebSockets    |
| Frontend      | React, HTML, CSS                |
| Mensageria    | Apache Kafka                    |
| Banco de Dados| PostgreSQL                      |
| Infra         | NGINX (proxy reverso)           |

## Arquitetura

A arquitetura modular garante escalabilidade e manutenção simplificada:

- **Data Generator:** simula sensores da nave e envia dados para o Kafka.
- **Kafka:** gerencia o fluxo assíncrono de dados de telemetria.
- **Backend (Spring Boot):** processa dados, emite alertas, e disponibiliza APIs REST.
- **Frontend (React):** exibe métricas, alertas e gráficos em tempo real.
- **PostgreSQL:** armazena dados persistentes da missão.

<!-- Se tiver diagrama, descomente abaixo -->
<!-- ![Diagrama de Arquitetura](./docs/architecture-diagram.png) -->

## Como Executar

> Requisitos: Docker, Java 17+, Node.js 16+

```bash
# 1. Clone o repositório
git clone https://github.com/seu-usuario/atlas-monitoring-system.git
cd atlas-monitoring-system

# 2. Suba os containers (Kafka, PostgreSQL)
docker-compose up -d
```
