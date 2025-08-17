# 🎬 Movies Platform

A modular Java-based platform for managing and exploring movie data.
Built with **Spring Boot**, containerized with **Docker Compose**, and designed for extensibility.

---

## Overview

The platform is organized into:
- **`apis/`** – Shared API contracts (OpenAPI specifications, DTOs, etc.).
- **`services/movies/`** – Movie service handling CRUD operations, search, and integration with external APIs.
- **`services/users/`** – Users service.

This is a service-oriented architecture intended to run locally via `docker compose` and scale into multiple services.

---

## Features

- **Contract-first API design** with shared API definitions.
- **Modular services** (currently: Movies service).
- **Containerized environment** with `docker-compose`.
- **Extensible architecture** for future services (users, reviews, recommendations).
- Health checks & actuator endpoints.

---

## Tech Stack

- **Java** 21 + **Spring Boot**
- **Maven** (multi-module setup)
- **Docker Compose**
- **PostgreSQL** (or chosen DB)

---

## Useful Commands
This will start the PostgreSQL database for local development.
```bash
  docker compose -f dev-postgres.yaml up -d
```

Run flyway migrations:
```bash
  mvn clean flyway:migrate \
    -Dflyway.url=jdbc:postgresql://localhost:5432/movies \
    -Dflyway.user=movies_user \
    -Dflyway.password=iddqd

  mvn clean flyway:migrate \
    -Dflyway.url=jdbc:postgresql://localhost:5431/users \
    -Dflyway.user=users_user \
    -Dflyway.password=idkfa
```



## License

This project is licensed under the MIT License.
