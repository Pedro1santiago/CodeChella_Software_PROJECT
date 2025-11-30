-- Cria a tabela usuario_admin caso não exista
CREATE TABLE IF NOT EXISTS usuario_admin (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(30) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE,
    senha VARCHAR(100) NOT NULL,
    tipo_usuario VARCHAR(30) NOT NULL CHECK (tipo_usuario IN ('ADMIN'))
);

-- Ajusta registros antigos para seguir a nova constraint
UPDATE usuario_admin
SET tipo_usuario = 'ADMIN'
WHERE tipo_usuario = 'ADMINISTRADOR';

-- Ajusta a constraint (caso exista)
ALTER TABLE usuario_admin
DROP CONSTRAINT IF EXISTS usuario_admin_tipo_usuario_check;

ALTER TABLE usuario_admin
ADD CONSTRAINT usuario_admin_tipo_usuario_check
CHECK (tipo_usuario IN ('ADMIN'));

-- Adiciona coluna de relacionamento com usuario
ALTER TABLE usuario_admin
ADD COLUMN IF NOT EXISTS id_usuario BIGINT REFERENCES usuario(id) ON DELETE CASCADE;

-- Insere o admin padrão caso ainda não exista
INSERT INTO usuario_admin (id, nome, email, senha, tipo_usuario)
VALUES (1, 'Administrador Padrão', 'admin@codechella.com', '123456', 'ADMIN')
ON CONFLICT (id) DO NOTHING;
