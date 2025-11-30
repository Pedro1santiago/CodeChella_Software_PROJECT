# ✅ Super Admin Teste - Implementação Completa

## 🎯 O que foi feito

Criado um endpoint de login para Super Admin com credenciais de teste pré-inseridas no banco de dados.

---

## 📧 CREDENCIAIS DE TESTE

### **Email:**
```
codechelle.superadmin@gmail.com
```

### **Senha:**
```
12345CDS
```

### **Tipo:**
```
SUPER (Super Admin)
```

---

## 🔌 Endpoint de Login

### URL
```
POST /auth/super-admin/login
```

### Request Body
```json
{
  "email": "codechelle.superadmin@gmail.com",
  "senha": "12345CDS"
}
```

### Response (200 OK)
```json
{
  "id": 1,
  "nome": "CodeChella SuperAdmin",
  "email": "codechelle.superadmin@gmail.com",
  "senha": "12345CDS",
  "tipoUsuario": "SUPER"
}
```

---

## 📁 Arquivos Criados/Atualizados

### 1. **Migration SQL** (V011__insert_super_admin_teste.sql)
- Insere automaticamente o Super Admin no banco de dados
- Email: `codechelle.superadmin@gmail.com`
- Senha: `12345CDS`

### 2. **SuperAdminLoginRequest.java**
- Classe para receber email e senha

### 3. **SuperAdminAuthService.java**
- Serviço com lógica de login
- Valida email e senha
- Retorna SuperAdminDTO

### 4. **AuthController.java** (Atualizado)
- Novo endpoint: `POST /auth/super-admin/login`
- Mantém endpoints de usuário normal

### 5. **Documentação** (CREDENCIAIS_TESTE_SUPERADMIN.md)
- Guia rápido para testar

---

## 🚀 Como Testar no Frontend

### 1. Fazer Login
```javascript
const response = await fetch('http://localhost:8080/auth/super-admin/login', {
  method: 'POST',
  headers: {
    'Content-Type': 'application/json'
  },
  body: JSON.stringify({
    email: 'codechelle.superadmin@gmail.com',
    senha: '12345CDS'
  })
});

const superAdmin = await response.json();
console.log(superAdmin); // { id: 1, nome: "...", email: "...", ... }
```

### 2. Guardar o ID
```javascript
const superAdminId = superAdmin.id; // 1
```

### 3. Usar em Requisições Subsequentes
```javascript
// Listar todos os admins
fetch('http://localhost:8080/super-admin/listar/admins', {
  headers: {
    'super-admin-id': superAdminId.toString()
  }
});

// Aprovar permissão
fetch(`http://localhost:8080/permissoes/${permissaoId}/aprovar`, {
  method: 'PUT',
  headers: {
    'super-admin-id': superAdminId.toString()
  }
});
```

---

## 📊 Fluxo de Autenticação

```
┌─────────────────────────────────────┐
│ Frontend envia login                │
│ email + senha                       │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│ POST /auth/super-admin/login        │
│ AuthController.loginSuperAdmin()    │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│ SuperAdminAuthService.login()       │
│ busca no banco por email            │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│ Valida senha                        │
│ Se incorreta → 401 Unauthorized     │
│ Se encontrado → SuperAdminDTO       │
└────────────┬────────────────────────┘
             │
             ▼
┌─────────────────────────────────────┐
│ Frontend recebe:                    │
│ {id, nome, email, tipoUsuario}      │
│ Guarda super-admin-id no header     │
└─────────────────────────────────────┘
```

---

## 🔄 Endpoints Conexos Já Funcionando

Com o Super Admin logado (usando `super-admin-id: 1`):

| Ação | Endpoint | Método |
|------|----------|--------|
| Listar Admins | `/super-admin/listar/admins` | GET |
| Listar Usuários | `/super-admin/listar/usuarios` | GET |
| Aprovar Permissão | `/permissoes/{id}/aprovar` | PUT |
| Negar Permissão | `/permissoes/{id}/negar` | PUT |
| Excluir Evento | `/super-admin/eventos/{id}` | DELETE |
| Promover Admin | `/super-admin/promover/admin/{id}` | PUT |
| Rebaixar User | `/super-admin/rebaixar/user/{id}` | PUT |

---

## ✨ Status Final

✅ Login Super Admin implementado
✅ Credenciais inseridas no banco via migration
✅ Endpoints testáveis
✅ Documentação completa

**Pronto para testar no frontend! 🎉**
