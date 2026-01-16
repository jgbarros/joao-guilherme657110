# joao-guilherme657110

# João Guilherme Barros - 657110 - Full Stack Sênior SEPLAG-MT 🚀

**Processo Seletivo 001/2026 - Anexo II-C: Implementação Full Stack Java + React** [file:1]

[![Backend](https://img.shields.io/badge/Backend-Spring%20Boot%203.2-brightgreen)] [![Frontend](https://img.shields.io/badge/Frontend-React%2018-blue)] [![DB](https://img.shields.io/badge/DB-PostgreSQL%2016-orange)] [![Object](https://img.shields.io/badge/Object-MinIO-red)]

## 🔧 Status Atual (16/01/2026)
- ✅ **Infra Docker**: Postgres + MinIO + Backend UP
- ✅ **Flyway**: Schema Artista/Album/Regional + seed data
- ✅ **Endpoints**: `/actuator/health`
- ⏳ **Próximo**: JPA Entities + CRUD Controllers + JWT

## 🚀 Como rodar (2min)

```bash
git clone https://github.com/jgbarros/joao-guilherme657110
cd joao-guilherme657110
docker compose up -d
```

### Serviços
| Serviço | URL | Credenciais |
|---------|-----|-------------|
| **PostgreSQL** | `localhost:5432` | `musicdb` / `postgres` / `postgres` |
| **MinIO Console** | [localhost:9001](http://localhost:9001) | `minioadmin` / `minioadmin` |
| **MinIO Browser** | [localhost:9001/browser](http://localhost:9001) | `minioadmin` / `minioadmin` |
| **Backend API** | `localhost:8080` | - |
