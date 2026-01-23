# joao-guilherme657110

# João Guilherme Barros - 657110 - Full Stack Sênior SEPLAG-MT 🚀

**Processo Seletivo 001/2026 - Anexo II-C: Implementação Full Stack Java + React** [file:1]

[![Backend](https://img.shields.io/badge/Backend-Spring%20Boot%203.2-brightgreen)] [![Frontend](https://img.shields.io/badge/Frontend-React%2018-blue)] [![DB](https://img.shields.io/badge/DB-PostgreSQL%2016-orange)] [![Object](https://img.shields.io/badge/Object-MinIO-red)]

## 🔧 Status Atual (16/01/2026)
- ✅ **Infra Docker**: Postgres + MinIO + Backend UP
- ✅ **Flyway**: Schema + seed data (users, artistas, albuns, regionais)
- ✅ **JPA**: Entities Artista/Album/Regional/User
- ✅ **CRUD**: Controllers + Services + Repos (paginação/filtros)
- ✅ **JWT**: Auth completa com BCrypt + Roles (USER/ADMIN)
- ✅ **MinIO**: Upload arquivos (capas artistas) com presigned URL
- ✅ **WebSocket**: Real-time artistas/álbuns (broadcast)
- ⏳ **Próximo**: Frontend React

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



## 🚀 **Endpoints API REST**

### **Autenticação**

```
POST /api/auth/authenticate
Body: {"username":"admin","password":"admin123"}
Response: {"token":"eyJhbGciOiJIUzUxMiJ9..."}
```


### **Artistas** (Protegido ROLE_USER+)

```
GET    /api/artistas?page=0&size=10&filtro=Serj
POST   /api/artistas (cria → dispara WebSocket)
POST   /api/artistas/upload (multipart file)
PUT    /api/artistas/{id}
DELETE /api/artistas/{id}
```


### **Álbuns** (Similar CRUD)

```
GET/POST/PUT/DELETE /api/albuns + paginação/filtros
```


## 🌐 **WebSocket Real-Time** (Simples TextWebSocketHandler)

### **Endpoints WebSocket:**

```
ws://localhost:8080/ws/artistas  ← Artistas
ws://localhost:8080/ws/albuns    ← Álbuns
```


### **Teste Postman WebSocket:**

```
1. New → WebSocket Request
2. URL: ws://localhost:8080/ws/artistas → Connect
3. Recebe mensagens text/JSON de broadcast
4. Envia texto → Echo nos logs
```

**Exemplo fluxo:**

```
Cliente1: ws://localhost:8080/ws/artistas (conecta)
Cliente2: POST /api/artistas "Novo Artista" 
→ Service chama artistaHandler.broadcast("Artista criado!")
→ Cliente1 recebe JSON real-time!
```


## 🛠 **Infra Docker Compose**

```yaml
services:
  postgres:
    image: postgres:16
    environment:
      POSTGRES_DB: seplag_music
      POSTGRES_USER: seplag
      POSTGRES_PASSWORD: seplag123
    ports: ["5432:5432"]

  minio:
    image: minio/minio:latest
    ports: ["9000:9000", "9001:9001"]
    environment:
      MINIO_ROOT_USER: minioadmin
      MINIO_ROOT_PASSWORD: minioadmin
    command: server /data --console-address ":9001"

  backend:
    build: .
    ports: ["8080:8080"]
    depends_on:
      - postgres
      - minio
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/seplag_music
      MINIO_URL: http://minio:9000
```


## 🔑 **Credenciais Teste**

```
Admin: username=admin, password=admin123 (ROLE_ADMIN)
User:  username=user,  password=user123  (ROLE_USER)
```


## 📱 **Postman Collection**

```
1. Auth: POST /api/auth/authenticate → Salva {{jwt_token}}
2. Artistas: GET/POST com Authorization: Bearer {{jwt_token}}
3. Upload: POST /api/artistas/upload (form-data file)
4. WebSocket: ws://localhost:8080/ws/artistas (SUBSCRIBE /topic/artistas)
4. WebSocket: ws://localhost:8080/ws/albuns (SUBSCRIBE /topic/albuns)
```


## 🧪 **Teste Fluxo Completo**

```
1. Postman WS: ws://localhost:8080/ws/artistas (connect)
2. Auth → GET token
3. POST /api/artistas {"nome":"Serj Teste"}
→ WS recebe: {"artistName":"Serj Teste","message":"Novo artista!"}
4. POST /api/artistas/upload (imagem capa)
→ Salva URL MinIO no artista
```


## 📋 **Próximos Passos Pendentes**

```
⏳ Frontend React:
  - Login JWT + Axios interceptor
  - Listagem paginada + filtros
  - Upload MinIO drag&drop
  - WebSocket STOMP/SockJS notificações
⏳ Deploy: Docker + Kubernetes?
```


## 🔗 **Dependências Principais**

```
spring-boot-starter-web
spring-boot-starter-data-jpa
spring-boot-starter-security
spring-boot-starter-websocket
minio
flyway-core
jjwt-api/impl/jackson
lombok
```