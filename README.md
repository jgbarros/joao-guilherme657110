# Repositório: joao-guilherme657110

# João Guilherme Barros - 657110 - Full Stack Sênior SEPLAG-MT 🚀

**Processo Seletivo 001/2026 - Anexo II-C: Implementação Full Stack Java + React** [file:1]

[![Backend](https://img.shields.io/badge/Backend-Spring%20Boot%204.0.1-brightgreen)] [![Frontend](https://img.shields.io/badge/Frontend-React%2018-blue)] [![DB](https://img.shields.io/badge/DB-PostgreSQL%2016-orange)] [![Object](https://img.shields.io/badge/Object-MinIO-red)]

## 🔧 Status Atual (30/01/2026)
- ✅ **Infra Docker**: Postgres + MinIO + Backend UP
- ✅ **Flyway**: Schema + seed data (users, artistas, albuns, regionais)
- ✅ **JPA**: Entities Artista/Album/Regional/User
- ✅ **CRUD**: Controllers + Services + Repos (paginação/filtros)
- ✅ **JWT**: Auth completa com BCrypt + Roles (USER/ADMIN)
- ✅ **MinIO**: Upload arquivos (capas artistas) com presigned URL
- ✅ **WebSocket**: Real-time artistas/álbuns (broadcast)
- ✅ **Frontend**: Frontend React

## 🚀 Como rodar o Docker

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
| **Backend API** | [localhost:8080/](http://localhost:8080) | `admin` / `admin123` |
| **Frontend** | [localhost:5173](http://localhost:5173) | `admin` / `admin123` |
| **Swagger UI** | [localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html) |



## 🚀 **Endpoints API REST**

### **Autenticação**

```
POST /api/auth/authenticate
Body: {"username":"admin","password":"admin123"}
Response: {"token":"eyJhbGciOiJIUzUxMiJ9..."}
```

### **Artistas** (Protegido ROLE_USER+)

```
GET    /api/artistas               Listar artistas (paginado)
GET    /api/artistas/{id}           Buscar por ID
GET    /api/artistas/count          Contar artistas
POST   /api/artistas                Criar artista (→ WebSocket)
PUT    /api/artistas/{id}           Atualizar artista
DELETE /api/artistas/{id}           Deletar artista
```

### **Álbuns** (Protegido ROLE_USER+)

```
GET    /api/albuns                  Listar álbuns (paginado)
GET    /api/albuns/{id}             Buscar por ID
GET    /api/albuns/count            Contar álbuns
POST   /api/albuns                  Criar álbum (→ WebSocket)
PUT    /api/albuns/{id}             Atualizar álbum
DELETE /api/albuns/{id}             Deletar álbum
POST   /api/albuns/{id}/upload      Upload capa (MinIO) (Protegido ROLE_ADMIN)
```

### **Regionais** (Protegido ROLE_USER+)

```
GET    /api/regionais             Listar regionais
GET    /api/regionais/{id}        Buscar por ID
GET    /api/regionais/count       Contar regionais
GET    /api/regionais/ativas      Listar regionais ativas
```

## **WebSocket Real-Time** (Simples TextWebSocketHandler)

### **Endpoints WebSocket:**

```
ws://localhost:8080/ws/artistas  ← Artistas
ws://localhost:8080/ws/albuns    ← Álbuns
```


### **Notificações Postman WebSocket:**

```
1. New → WebSocket Request
2. URL: ws://localhost:8080/ws/artistas → Connect
3. Message: Mensagens text/JSON de broadcast
SUBSCRIBE
id:sub-0
destination:/topic/artistas

^@
4. Envia texto → Echo nos logs → Send


```

**Exemplo fluxo:**

```
Cliente1: ws://localhost:8080/ws/artistas (Conecta)
Cliente2: POST /api/artistas "Novo Artista" 
→ Service chama artistaHandler.broadcast("Artista criado!")
→ Cliente1 recebe JSON real-time!
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


## ⚠️ **Observações Importantes**

ℹ️ Nota sobre Regionais: A tabela Regionais é alimentada automaticamente na inicialização da aplicação via integração com o WS externo https://integrador-argus-api.geia.vip/v1/regionais. Isso garante dados atualizados sem intervenção manual (via CommandLineRunner ou @PostConstruct). Logs de inicialização mostram o progresso.

➖ d) Rate limit: máximo 10 requisições por minuto por usuário na API. 
```
 - Precisou aumentar para 100 pois fica mais viável para utilização, uma vez que para carregar a página do Dashboard já consome quase as 10 requisições.
```

ℹ️ MinIO pré-assinados (presigned URL)

➖ i) Recuperar imagens via links pré-assinados (presigned URL) com expiração de 30 minutos
```
 - Apresentou o erro SignatureDoesNotMatch. A URL foi gerada internamente para o Docker (minio:9000) foi assinada, mas a URL externa (localhost:9001) não estava com a mesma assinatura. Não foi possível implementar o reverse proxy para resolver o problema sem comprometer a entrega da solução já implementada.
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