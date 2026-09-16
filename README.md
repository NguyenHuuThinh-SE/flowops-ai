# FlowOps AI

> AI-powered Enterprise Workflow & IT Service Management Platform

FlowOps AI is a modern enterprise workflow platform that helps organizations manage internal requests, automate approval processes, and improve IT service operations with Artificial Intelligence.

## Architecture

```text
React + TypeScript
        │
        ▼
Spring Cloud Gateway
        │
 ┌──────┼───────────┐
 │      │           │
 ▼      ▼           ▼
Auth  Workflow   AI Service
Service Service   (FastAPI)
        │
        ▼
 PostgreSQL • Redis • Kafka
```

## Features

- JWT Authentication & Role-Based Access Control
- Workflow & Ticket Management
- AI Ticket Classification
- Priority Prediction
- Department Recommendation
- AI Ticket Summarization
- Knowledge Base with RAG
- Analytics Dashboard
- Dockerized Microservices

## Tech Stack

### Frontend

- React 19
- TypeScript
- Vite
- Tailwind CSS

### Backend

- Java 21
- Spring Boot 3
- Spring Security
- Spring Cloud Gateway
- Spring Data JPA
- PostgreSQL
- Redis
- Kafka

### AI

- Python
- FastAPI
- LLM API
- Sentence Transformers
- RAG (Retrieval-Augmented Generation)
- Vector Database

### DevOps

- Docker & Docker Compose
- Kubernetes (planned)
- GitHub Actions
- Prometheus & Grafana

## Project Structure

```text
flowops-ai/
│
├── backend/
│   ├── auth-service/
│   ├── workflow-service/
│   ├── gateway-service/
│   └── notification-service/
│
├── frontend/
│   └── web/
│
├── ai-service/
│
├── docker/
│
├── docs/
│
└── README.md
```

## User Workflow

1. Employee creates a request
2. AI classifies the ticket
3. System assigns the responsible department
4. Technician processes the request
5. Manager approves the resolution
6. AI generates a summary and analytics report

## Roles

| Role | Permissions |
|------|-------------|
| Employee | Create & track requests |
| Technician | Resolve assigned tickets |
| Manager | Approve workflows |
| Admin | System administration |

## Getting Started

```bash
git clone https://github.com/NguyenHuuThinh-SE/flowops-ai.git

cd flowops-ai

docker compose up -d
```

## Roadmap

- [x] Repository initialization
- [ ] Docker infrastructure
- [ ] Authentication Service
- [ ] Workflow Service
- [ ] API Gateway
- [ ] React Dashboard
- [ ] AI Classification
- [ ] RAG Knowledge Base
- [ ] Kafka Notification
- [ ] Monitoring & CI/CD

## Author

**Nguyen Huu Thinh**

Computer Science Student • Backend & AI Developer
