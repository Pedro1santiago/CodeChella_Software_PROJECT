# 📋 Sistema de Permissões CodeChella

## 🎯 Visão Geral

Sistema de controle de permissões de três níveis implementado na aplicação CodeChella, permitindo que usuários tenham diferentes graus de acesso e responsabilidades.

---

## 👥 Tipos de Usuário

### 1. **Usuário Normal (USER)**
- ✅ Pode visualizar e filtrar eventos
- ✅ Pode comprar ingressos para eventos
- ✅ Pode solicitar permissão para virar Admin
- ✅ Pode acompanhar status de suas solicitações

### 2. **Administrador (ADMIN)**
- ✅ Tudo que o Usuário Normal faz
- ✅ Pode pesquisar e gerenciar usuários
- ✅ Pode criar eventos
- ✅ Pode definir número de ingressos disponíveis
- ✅ Pode atualizar seus eventos
- ❌ **SÓ PODE EXCLUIR seus próprios eventos** (isolamento de dados)
- ✅ Pode ver eventos de outros admins

### 3. **Super Admin (SUPER)**
- ✅ Tudo que o Admin faz
- ✅ Pode gerenciar permissões (aprovar/negar solicitações)
- ✅ Pode promover usuários para Admin
- ✅ Pode rebaixar Admin para User
- ✅ Pode excluir QUALQUER evento
- ✅ Pode remover usuários do sistema
- ✅ Pode remover Admins do sistema
- ✅ Acesso total ao sistema

---

## 🔐 Endpoints de Autenticação (Usuário Normal)

### Registrar Novo Usuário
```http
POST /auth/usuario/registrar
Content-Type: application/json

{
  "nome": "João Silva",
  "email": "joao@example.com",
  "senha": "senha123",
  "tipoUsuario": "USER"
}

Response: 201 Created
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "tipoUsuario": "USER",
  "criadoEm": "2024-01-15T10:30:00"
}
```

### Login de Usuário
```http
POST /auth/usuario/login?email=joao@example.com&senha=senha123

Response: 200 OK
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "tipoUsuario": "USER",
  "criadoEm": "2024-01-15T10:30:00"
}
```

### Obter Dados do Usuário
```http
GET /auth/usuario/1

Response: 200 OK
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "tipoUsuario": "USER",
  "criadoEm": "2024-01-15T10:30:00"
}
```

### Atualizar Perfil
```http
PUT /auth/usuario/1
Content-Type: application/json
usuario-id: 1

{
  "nome": "João Silva Atualizado",
  "email": "joao.novo@example.com",
  "senha": "novaSenha123"
}
```

### Deletar Usuário
```http
DELETE /auth/usuario/1
usuario-id: 1
```

---

## 🎫 Endpoints de Ingressos

### Visualizar Eventos (Todos podem)
```http
GET /eventos

Response: 200 OK (Stream de eventos)
[
  {
    "id": 1,
    "tipo": "SHOW",
    "nome": "Festival de Música",
    "data": "2024-02-20",
    "descricao": "Festival de música ao vivo",
    "statusEvento": "ABERTO",
    "idAdminCriador": 5,
    "numeroIngressosDisponiveis": 100
  },
  ...
]
```

### Filtrar Eventos por Tipo (Todos podem)
```http
GET /eventos/categoria/SHOW

Response: 200 OK (Stream de eventos filtrados)
```

### Buscar Evento por ID (Todos podem)
```http
GET /eventos/1

Response: 200 OK
{
  "id": 1,
  "tipo": "SHOW",
  "nome": "Festival de Música",
  "data": "2024-02-20",
  "descricao": "Festival de música ao vivo",
  "statusEvento": "ABERTO",
  "idAdminCriador": 5,
  "numeroIngressosDisponiveis": 100
}
```

### Comprar Ingresso (Apenas Usuário Normal)
```http
POST /ingressos/comprar?eventoId=1&quantidade=2
usuario-id: 1

Response: 200 OK
[
  {
    "id": 1,
    "eventoId": 1,
    "status": "VENDIDO",
    "quantidadeTotal": 1
  },
  {
    "id": 2,
    "eventoId": 1,
    "status": "VENDIDO",
    "quantidadeTotal": 1
  }
]

Error: 403 Forbidden (se for Admin ou Super)
{
  "message": "Apenas usuários normais podem comprar ingressos"
}
```

### Cancelar Ingresso
```http
PUT /ingressos/cancelar/1

Response: 200 OK
{
  "id": 1,
  "eventoId": 1,
  "status": "DISPONIVEL",
  "quantidadeTotal": 2
}
```

---

## 📝 Endpoints de Permissões

### Solicitar Permissão para ser Admin
```http
POST /permissoes/solicitar
usuario-id: 1

Response: 201 Created
{
  "id": 1,
  "idUsuario": 1,
  "nomeUsuario": "João Silva",
  "tipoPermissao": "ADMIN",
  "status": "PENDENTE",
  "motivoNegacao": null,
  "criadoEm": "2024-01-15T10:30:00",
  "atualizadoEm": "2024-01-15T10:30:00"
}

Error: 400 Bad Request (se já for Admin)
{
  "message": "Você já é um administrador"
}
```

### Listar Minhas Solicitações
```http
GET /permissoes/minhas-solicitacoes
usuario-id: 1

Response: 200 OK
[
  {
    "id": 1,
    "idUsuario": 1,
    "nomeUsuario": "João Silva",
    "tipoPermissao": "ADMIN",
    "status": "PENDENTE",
    "motivoNegacao": null,
    "criadoEm": "2024-01-15T10:30:00",
    "atualizadoEm": "2024-01-15T10:30:00"
  },
  ...
]
```

### Listar Solicitações Pendentes (Super Admin)
```http
GET /permissoes/pendentes
super-admin-id: 10

Response: 200 OK (Stream de solicitações)
[
  {
    "id": 1,
    "idUsuario": 1,
    "nomeUsuario": "João Silva",
    "tipoPermissao": "ADMIN",
    "status": "PENDENTE",
    "motivoNegacao": null,
    "criadoEm": "2024-01-15T10:30:00",
    "atualizadoEm": "2024-01-15T10:30:00"
  },
  ...
]
```

### Aprovar Solicitação (Super Admin)
```http
PUT /permissoes/1/aprovar
super-admin-id: 10

Response: 200 OK
{
  "id": 1,
  "idUsuario": 1,
  "nomeUsuario": "João Silva",
  "tipoPermissao": "ADMIN",
  "status": "APROVADO",
  "motivoNegacao": null,
  "criadoEm": "2024-01-15T10:30:00",
  "atualizadoEm": "2024-01-15T11:00:00"
}

// Neste ponto, o usuário 1 foi automaticamente promovido para ADMIN
```

### Negar Solicitação (Super Admin)
```http
PUT /permissoes/1/negar?motivo=Falta%20de%20experiência
super-admin-id: 10

Response: 200 OK
{
  "id": 1,
  "idUsuario": 1,
  "nomeUsuario": "João Silva",
  "tipoPermissao": "ADMIN",
  "status": "NEGADO",
  "motivoNegacao": "Falta de experiência",
  "criadoEm": "2024-01-15T10:30:00",
  "atualizadoEm": "2024-01-15T11:05:00"
}
```

---

## 🎭 Endpoints de Eventos (Admin/Super Admin)

### Criar Evento (Admin ou Super)
```http
POST /eventos
admin-id: 5

Content-Type: application/json
{
  "tipo": "SHOW",
  "nome": "Festival de Música",
  "data": "2024-02-20",
  "descricao": "Festival de música ao vivo",
  "numeroIngressosDisponiveis": 500
}

Response: 201 Created
{
  "id": 1,
  "tipo": "SHOW",
  "nome": "Festival de Música",
  "data": "2024-02-20",
  "descricao": "Festival de música ao vivo",
  "statusEvento": "ABERTO",
  "idAdminCriador": 5,
  "numeroIngressosDisponiveis": 500
}
```

### Atualizar Evento (Admin - seu próprio evento / Super - qualquer um)
```http
PUT /eventos/1
admin-id: 5

Content-Type: application/json
{
  "tipo": "SHOW",
  "nome": "Festival de Música 2024",
  "data": "2024-02-21",
  "descricao": "Festival de música ao vivo - Edição 2024",
  "numeroIngressosDisponiveis": 600
}

Response: 200 OK
{
  "id": 1,
  "tipo": "SHOW",
  "nome": "Festival de Música 2024",
  "data": "2024-02-21",
  "descricao": "Festival de música ao vivo - Edição 2024",
  "statusEvento": "ABERTO",
  "idAdminCriador": 5,
  "numeroIngressosDisponiveis": 600
}

Error: 403 Forbidden (se Admin tentar atualizar evento de outro)
{
  "message": "Você só pode atualizar eventos que criou"
}
```

### Excluir Evento (Admin - seu próprio evento / Super - qualquer um)
```http
DELETE /eventos/1
admin-id: 5

Response: 204 No Content

Error: 403 Forbidden (se Admin tentar excluir evento de outro)
{
  "message": "Você só pode excluir eventos que criou"
}
```

### Cancelar Evento (fechar ingressos)
```http
PUT /eventos/1/cancelar
admin-id: 5

Response: 200 OK
{
  "id": 1,
  "tipo": "SHOW",
  "nome": "Festival de Música",
  "data": "2024-02-20",
  "descricao": "Festival de música ao vivo",
  "statusEvento": "FECHADO",
  "idAdminCriador": 5,
  "numeroIngressosDisponiveis": 500
}
```

---

## 👨‍💼 Endpoints Super Admin

### Listar Todos os Admins
```http
GET /super-admin/listar/admins
super-admin-id: 10

Response: 200 OK (Stream)
[
  {
    "id": 5,
    "nome": "Admin 1",
    "email": "admin1@example.com",
    "tipoUsuario": "ADMIN"
  },
  ...
]
```

### Listar Todos os Usuários Normais
```http
GET /super-admin/listar/usuarios
super-admin-id: 10

Response: 200 OK (Stream)
[
  {
    "id": 1,
    "nome": "João Silva",
    "email": "joao@example.com",
    "tipoUsuario": "USER",
    "criadoEm": "2024-01-15T10:30:00"
  },
  ...
]
```

### Promover Usuário para Admin
```http
PUT /super-admin/promover/admin/1
super-admin-id: 10

Response: 200 OK
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@example.com",
  "tipoUsuario": "ADMIN",
  "criadoEm": "2024-01-15T10:30:00"
}
```

### Rebaixar Admin para User
```http
PUT /super-admin/rebaixar/user/5
super-admin-id: 10

Response: 200 OK
{
  "id": 5,
  "nome": "Admin 1",
  "email": "admin1@example.com",
  "tipoUsuario": "USER",
  "criadoEm": "2024-01-10T09:00:00"
}
```

### Remover Usuário do Sistema
```http
DELETE /super-admin/remover/usuario/1
super-admin-id: 10

Response: 204 No Content
```

### Remover Admin do Sistema
```http
DELETE /super-admin/remover/admin/5
super-admin-id: 10

Response: 204 No Content
```

### Super Admin Exclui Qualquer Evento
```http
DELETE /super-admin/eventos/1
super-admin-id: 10

Response: 204 No Content
```

### Criar Novo Admin (direto)
```http
POST /super-admin/criar/admin
super-admin-id: 10

Content-Type: application/json
{
  "id": 6,
  "nome": "Novo Admin",
  "email": "novoadmin@example.com",
  "senha": "senha123",
  "tipoUsuario": "ADMIN"
}

Response: 201 Created
{
  "id": 6,
  "nome": "Novo Admin",
  "email": "novoadmin@example.com",
  "tipoUsuario": "ADMIN"
}
```

---

## 🔄 Fluxo de Autorização

### Exemplo 1: Usuário → Admin
```
1. Usuário Normal (João) faz login
   POST /auth/usuario/login
   ↓
2. João solicitaPermissao para virar Admin
   POST /permissoes/solicitar (header: usuario-id: 1)
   Status: PENDENTE
   ↓
3. Super Admin vê solicitações pendentes
   GET /permissoes/pendentes (header: super-admin-id: 10)
   ↓
4. Super Admin aprova
   PUT /permissoes/1/aprovar (header: super-admin-id: 10)
   Status: APROVADO
   ↓
5. João é automaticamente promovido para ADMIN
   tipoUsuario muda de USER → ADMIN
   ↓
6. João agora pode criar eventos, pesquisar usuários, etc.
```

### Exemplo 2: Isolamento de Dados (Admin só vê seus dados)
```
Admin NIKE cria evento "Nike Festival" (idAdminCriador: 5)
Admin ADIDAS tenta excluir
   DELETE /eventos/1 (header: admin-id: 8)
   ↓
Response: 403 Forbidden
"Você só pode excluir eventos que criou"
```

### Exemplo 3: Super Admin tem acesso total
```
Super Admin tenta excluir evento de NIKE
   DELETE /super-admin/eventos/1 (header: super-admin-id: 10)
   ↓
Response: 204 No Content
Evento deletado com sucesso
```

---

## 📊 Estrutura de Dados

### Tabelas Criadas

#### usuario
```sql
- id (PK)
- nome
- email (UNIQUE)
- senha
- tipo_usuario (USER, ADMIN, SUPER)
- created_at
```

#### usuario_admin
```sql
- id (PK)
- id_usuario (FK)
- nome
- email (UNIQUE)
- senha
- tipo_usuario (ADMIN)
```

#### super_admin
```sql
- id (PK)
- nome
- email (UNIQUE)
- senha
- tipo_usuario (SUPER)
- created_at
```

#### eventos
```sql
- id (PK)
- tipo
- nome
- data
- descricao
- status_evento
- id_admin_criador (FK) ← Rastreia qual admin criou
- numero_ingressos_disponiveis
```

#### solicitacao_permissao
```sql
- id (PK)
- id_usuario (FK)
- tipo_permissao (ADMIN)
- status (PENDENTE, APROVADO, NEGADO)
- motivo_negacao
- created_at
- updated_at
```

---

## 🚀 Como Usar

### 1. Registre um novo usuário normal
```bash
curl -X POST http://localhost:8080/auth/usuario/registrar \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@example.com",
    "senha": "senha123",
    "tipoUsuario": "USER"
  }'
```

### 2. Faça login
```bash
curl -X POST "http://localhost:8080/auth/usuario/login?email=joao@example.com&senha=senha123"
```

### 3. Solicite permissão
```bash
curl -X POST http://localhost:8080/permissoes/solicitar \
  -H "usuario-id: 1"
```

### 4. Super Admin aprova
```bash
curl -X PUT http://localhost:8080/permissoes/1/aprovar \
  -H "super-admin-id: 10"
```

### 5. Agora você é Admin! Crie um evento
```bash
curl -X POST http://localhost:8080/eventos \
  -H "admin-id: 1" \
  -H "Content-Type: application/json" \
  -d '{
    "tipo": "SHOW",
    "nome": "Meu Evento",
    "data": "2024-02-20",
    "descricao": "Descrição do evento",
    "numeroIngressosDisponiveis": 100
  }'
```

---

## ✅ Resumo de Permissões

| Ação | USER | ADMIN | SUPER |
|------|------|-------|-------|
| Ver eventos | ✅ | ✅ | ✅ |
| Filtrar eventos | ✅ | ✅ | ✅ |
| Comprar ingressos | ✅ | ❌ | ❌ |
| Solicitar permissão | ✅ | ❌ | ❌ |
| Criar evento | ❌ | ✅ | ✅ |
| Atualizar seu evento | ❌ | ✅ | ✅ |
| Excluir seu evento | ❌ | ✅ | ✅ |
| Excluir evento de outro | ❌ | ❌ | ✅ |
| Pesquisar usuários | ❌ | ✅ | ✅ |
| Aprovar permissões | ❌ | ❌ | ✅ |
| Negar permissões | ❌ | ❌ | ✅ |
| Promover para Admin | ❌ | ❌ | ✅ |
| Rebaixar de Admin | ❌ | ❌ | ✅ |
| Remover usuários | ❌ | ❌ | ✅ |

---

## 🔒 Segurança

- ✅ Validação de tipo de usuário em cada endpoint
- ✅ Isolamento de dados (Admin só vê/altera seus dados)
- ✅ Headers personalizados para identificação (usuario-id, admin-id, super-admin-id)
- ✅ Permissões granulares por ação
- ✅ Fluxo de aprovação para escalação de privilégios

---

## 📝 Migrations Criadas

- `V006__create_table_usuario.sql` - Tabela de usuários normais
- `V007__create_table_super_admin.sql` - Tabela de super admins
- `V008__update_usuario_admin.sql` - Atualização de relacionamentos
- `V009__update_eventos_admin_criador.sql` - Rastreamento de criador
- `V010__create_table_solicitacao_permissao.sql` - Solicitações de permissão

---

**Sistema criado para oferecer controle granular de permissões e isolamento de dados! 🎉**
