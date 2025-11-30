# 🔑 Credenciais de Teste - Super Admin

## Email e Senha para Login

```
Email: codechelle.superadmin@gmail.com
Senha: 12345CDS
Tipo:  SUPER (Super Admin)
```

---

## 🚀 Como Fazer Login do Super Admin

### Via cURL
```bash
curl -X POST http://localhost:8080/auth/super-admin/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "codechelle.superadmin@gmail.com",
    "senha": "12345CDS"
  }'
```

### Response (Sucesso 200 OK)
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

## 📋 Dados do Super Admin Inserido

```
ID:           1 (gerado automaticamente)
Nome:         CodeChella SuperAdmin
Email:        codechelle.superadmin@gmail.com
Senha:        12345CDS
TipoUsuario:  SUPER
```

---

## 💡 Depois do Login

Use o `super-admin-id: 1` em seus headers para:

### Listar Admins
```bash
curl -X GET http://localhost:8080/super-admin/listar/admins \
  -H "super-admin-id: 1"
```

### Listar Usuários
```bash
curl -X GET http://localhost:8080/super-admin/listar/usuarios \
  -H "super-admin-id: 1"
```

### Aprovar Permissão
```bash
curl -X PUT http://localhost:8080/permissoes/1/aprovar \
  -H "super-admin-id: 1"
```

### Excluir Qualquer Evento
```bash
curl -X DELETE http://localhost:8080/super-admin/eventos/1 \
  -H "super-admin-id: 1"
```

---

## 📝 Endpoints de Autenticação Atualizados

| Método | Endpoint | Descrição |
|--------|----------|-----------|
| POST | `/auth/usuario/register` | Registrar novo usuário |
| POST | `/auth/usuario/login` | Login de usuário normal |
| POST | `/auth/super-admin/login` | **Login de Super Admin** ✨ |

---

## ⚠️ Importante

- Todos os dados são salvos no banco de dados via migration
- O email e senha NÃO são criptografados (para teste)
- Use o `super-admin-id: 1` como header em requisições do Super Admin
- Para produção, implemente criptografia de senha com BCrypt

---

**Credenciais de teste pronta para usar! ✅**
