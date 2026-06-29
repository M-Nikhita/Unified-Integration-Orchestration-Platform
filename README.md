# Unified Integration Orchestration Platform

A lightweight workflow orchestration engine and monitoring dashboard for API-based processes. Define a sequence of API calls as a **workflow**, execute it step-by-step through a Spring Boot backend, and watch execution flow live as a graph with real-time status, logs, and run history.

Think of it as a minimal version of tools like AWS Step Functions or Apache Airflow — built to demonstrate centralized orchestration instead of hard-coded service-to-service calls.

## Features

- **Sequential workflow execution** — steps run in order through a backend orchestration engine; a step only proceeds after the previous one succeeds
- **Connector-based API integration** — each step is linked to a reusable connector definition (endpoint, method, configuration) instead of hard-coded API logic
- **Workflow visualization** — execution flow rendered as a Directed Acyclic Graph (DAG) using Cytoscape.js, with nodes color-coded by step status
- **Execution logging** — every step's status, response, timing, and retry count is persisted to MySQL
- **Run history & analytics** — past runs grouped by run ID, with success/failure stats charted via Chart.js
- **Project-scoped workflows** — workflows are organized under projects for multi-tenant style grouping

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot, Spring Data JPA, Hibernate |
| Database | MySQL |
| Frontend | HTML, CSS, vanilla JavaScript |
| Visualization | Cytoscape.js (DAG rendering), Chart.js (stats) |
| Build tool | Maven (with included wrapper) |

## Project Structure

```
.
├── backend/
│   ├── pom.xml
│   ├── mvnw, mvnw.cmd              # Maven wrapper — no local Maven install needed
│   ├── test.http                   # sample API requests (REST Client format)
│   └── src/
│       └── main/
│           ├── java/com/uip/orchestration/
│           │   ├── OrchestrationApplication.java
│           │   ├── controller/     # REST endpoints
│           │   ├── model/          # JPA entities
│           │   ├── repository/     # Spring Data repositories
│           │   └── service/        # business logic
│           └── resources/
│               └── application.properties
└── frontend/
    ├── landing.html                # entry / splash page
    ├── index.html                  # orchestration dashboard
    ├── script.js                   # dashboard logic (graph render, execution, polling)
    └── style.css
```

## Architecture

```
landing.html → index.html (dashboard)
                    │
                    ▼  fetch()
        Spring Boot REST API  (:8080)
                    │
                    ▼  Spring Data JPA
              MySQL (orchestration_db)
```

The frontend calls the backend directly via the native `fetch()` API. The backend retrieves workflow steps and connector configuration, executes each step in order, records the outcome, and exposes the results back through REST endpoints that the dashboard polls and renders.

The API has no authentication layer yet — all endpoints are open. Workflows, steps, and connectors are currently created via direct API calls (see [API Reference](#api-reference) and `backend/test.http`); a dedicated creation UI is planned (see [Roadmap](#roadmap)).

## Prerequisites

- **Java 17** (JDK)
- **MySQL** 8.x (running locally or accessible remotely)
- A modern web browser (Chrome, Edge, or Firefox)
- No Node.js or npm required — the frontend is static HTML/CSS/JS served as plain files

## Setup & Installation

### 1. Clone the repository

```bash
git clone https://github.com/M-Nikhita/Unified-Integration-Orchestration-Platform.git
cd Unified-Integration-Orchestration-Platform
```

### 2. Create the database

Start MySQL and create the database the app expects:

```sql
CREATE DATABASE orchestration_db;
```

### 3. Configure database credentials

Open `backend/src/main/resources/application.properties` and set your own MySQL username and password:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/orchestration_db?useSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=YOUR_MYSQL_PASSWORD
```

> Tables are created automatically on startup — `spring.jpa.hibernate.ddl-auto=update` will generate the schema from the JPA entities the first time the app runs.

### 4. Run the backend

From the `backend/` directory, using the included Maven wrapper (no local Maven install needed):

**macOS/Linux:**
```bash
cd backend
./mvnw spring-boot:run
```

**Windows:**
```bash
cd backend
mvnw.cmd spring-boot:run
```

The API will start on **`http://localhost:8080`**.

Verify it's up:

```bash
curl http://localhost:8080/api/health
```

### 5. Run the frontend

The frontend is static — no build step. Open it directly or serve it with any simple static server.

**Quickest way** — just open the file in a browser:

```bash
cd frontend
open landing.html      # macOS
start landing.html     # Windows
xdg-open landing.html  # Linux
```

**Recommended way** — serve it over HTTP to avoid browser file:// restrictions (e.g. with the VS Code "Live Server" extension, or):

```bash
cd frontend
python3 -m http.server 5500
```

Then visit `http://localhost:5500/landing.html`.

> The dashboard's `script.js` points at `http://127.0.0.1:8080` for API calls — make sure the backend is running first.

## Using the Dashboard

1. Open `landing.html` and click **Open Dashboard**.
2. The dashboard loads the workflow graph for workflow ID `1` and renders it as a DAG.
3. Click **Execute Workflow** to trigger a run.
4. Watch each step animate to green (success) or red (failed) as execution progresses.
5. Check the **Run History** and **Stats** panels for past runs and aggregate success/failure rates.

> The current frontend is wired to a single workflow (ID `1`). To test with your own workflow, create one via the API first (see below), then update the workflow ID in `script.js`.

## API Reference

Base URL: `http://localhost:8080`

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/health` | Health check |
| `POST` | `/api/projects` | Create a project |
| `GET` | `/api/projects` | List all projects |
| `POST` | `/api/workflows` | Create a workflow |
| `GET` | `/api/workflows` | List all workflows |
| `GET` | `/api/workflows/project/{projectId}` | List workflows under a project |
| `GET` | `/api/workflows/{workflowId}/graph` | Get workflow steps as graph nodes/edges |
| `GET` | `/api/workflows/{id}/status` | Get aggregate execution status |
| `POST` | `/api/workflows/{workflowId}/execute` | Execute a workflow |
| `GET` | `/api/workflows/{workflowId}/logs` | Get execution logs for a workflow |
| `GET` | `/api/workflows/{workflowId}/runs` | Get run history |
| `POST` | `/api/workflow-steps` | Add a step to a workflow |
| `GET` | `/api/workflow-steps/workflow/{workflowId}` | List steps for a workflow |
| `POST` | `/api/connectors` | Create a connector |
| `GET` | `/api/connectors/workflow/{workflowId}` | List connectors for a workflow |

### Example: creating a workflow end-to-end

```http
### 1. Create a project
POST http://localhost:8080/api/projects
Content-Type: application/json

{
  "name": "E-commerce System",
  "description": "Handles order workflows"
}

### 2. Create a workflow under that project
POST http://localhost:8080/api/workflows
Content-Type: application/json

{
  "name": "Order Processing Workflow",
  "definition": "{ \"steps\": [] }",
  "project": { "id": 1 }
}

### 3. Create a connector for a step
POST http://localhost:8080/api/connectors
Content-Type: application/json

{
  "name": "Payment API",
  "type": "REST",
  "configuration": "{ \"url\": \"https://api.payment.com\", \"method\": \"POST\" }",
  "workflow": { "id": 1 }
}
```

More sample requests are available in [`backend/test.http`](backend/test.http) — works out of the box with the VS Code REST Client extension or JetBrains' built-in HTTP client.

## Roadmap

- Configurable retry-on-failure for workflow steps
- AI-based optimization of execution order and retry strategy
- In-dashboard UI for creating and editing workflows, steps, and connectors
- Real-time alerts (email/push) on workflow failure
- Mobile companion app for remote monitoring
- Role-based access control (admin / operator / viewer)
- Cloud and third-party service integrations

## License

This project is licensed under the [MIT License](LICENSE).
