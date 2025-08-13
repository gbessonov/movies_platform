# 🎬 Movies Platform

A modular Java-based platform for managing and exploring movie data.
Built with **Spring Boot**, containerized with **Docker Compose**, and designed for extensibility.

---

## 📜 Overview

The platform is organized into:
- **`apis/`** – Shared API contracts (OpenAPI specifications, DTOs, etc.).
- **`services/movies/`** – Movie service handling CRUD operations, search, and integration with external APIs.

This is a service-oriented architecture intended to run locally via `docker compose` and scale into multiple services.

---

## 🚀 Features

- 📄 **Contract-first API design** with shared API definitions.
- 🗄️ **Modular services** (currently: Movies service).
- 🐳 **Containerized environment** with `docker-compose`.
- 📊 **Extensible architecture** for future services (users, reviews, recommendations).
- ✅ Health checks & actuator endpoints.

---

## 🛠️ Tech Stack

- **Java** 21 + **Spring Boot**
- **Maven** (multi-module setup)
- **Docker Compose**
- **PostgreSQL** (or chosen DB)

---

## 📦 Getting Started

### 1. Clone the repo
```bash
git clone https://github.com/gbessonov/movies_platform.git
cd movies_platform
```

### Build the project
```bash
./mvnw clean install
```
If you don’t have Maven Wrapper, run `mvn clean install`.

## 📜 License

This project is licensed under the MIT License.
