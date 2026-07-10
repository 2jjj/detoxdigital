# Detox Digital

> Pare de procrastinar. Volte ao foco.

Sistema completo para combater a procrastinacao online, com dashboard de estatisticas, gerenciamento de sites distrativos e extensao Chrome para monitoramento automatico.

## Funcionalidades

- **Dashboard de Foco** — Visualize visitas, alertas e tempo gasto em sites distrativos (hoje e na semana)
- **Gerenciamento de Sites** — Adicione, remova e configure limites de tempo por sessao
- **Extensao Chrome** — Monitora automaticamente o tempo gasto em sites distrativos e envia alertas
- **Alertas Inteligentes** — Notificacao quando o tempo limite e atingido

## Stack

| Componente | Tecnologia |
|------------|-----------|
| Backend | Spring Boot 4.1.0 + Java 21 |
| Banco de dados | H2 (file-based) |
| Frontend | Thymeleaf + Bootstrap 5 |
| Extensao | Chrome Extension (Manifest V3) |
| ORM | Spring Data JPA + Hibernate |

## Como Rodar

```bash
# Execute com o Maven Wrapper
./mvnw spring-boot:run
```

O servidor inicia na porta **3000**. Acesse:

- http://localhost:3000 — Pagina inicial
- http://localhost:3000/dashboard — Dashboard de estatisticas
- http://localhost:3000/sites — Gerenciar sites distrativos
- http://localhost:3000/h2-console — Console do banco de dados

## Instalando a Extensao Chrome

1. Abra `chrome://extensions/`
2. Ative o "Modo desenvolvedor"
3. Clique em "Carregar extensao compactada"
4. Selecione a pasta `extension/` deste projeto
5. A extensao conecta automaticamente com o backend em `localhost:3000`

## Estrutura do Projeto

```
├── src/main/java/com/detoxdigital/
│   ├── controller/      # Endpoints REST e rotas web
│   ├── model/           # Entidades JPA (SiteVisit, DistractingSite, AlertEvent)
│   ├── repository/      # Repositorios Spring Data
│   └── service/         # Logica de negocio
├── src/main/resources/
│   ├── templates/       # Paginas Thymeleaf (index, dashboard, sites)
│   └── application.properties
├── extension/           # Extensao Chrome (Manifest V3)
└── pom.xml
```

## API

| Metodo | Rota | Descricao |
|--------|------|-----------|
| GET | `/api/check?domain=` | Verifica se um site e distrativo |
| POST | `/api/track` | Registra uma visita a um site |
| POST | `/api/alert` | Registra um alerta disparado |
| GET | `/api/sites` | Lista todos os sites distrativos |
| GET | `/api/sites/active` | Lista sites ativos |
| POST | `/api/sites` | Cria um novo site distrativo |
| DELETE | `/api/sites/{id}` | Remove um site |
| GET | `/api/stats/today` | Estatisticas do dia |
| GET | `/api/stats/week` | Estatisticas da semana |

## Licenca

MIT
