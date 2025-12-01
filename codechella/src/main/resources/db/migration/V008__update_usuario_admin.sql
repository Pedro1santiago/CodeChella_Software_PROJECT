-- Modificar a tabela usuario_admin para usar tipo_usuario correto e adicionar id_usuario
ALTER TABLE usuario_admin
DROP CONSTRAINT IF EXISTS usuario_admin_tipo_usuario_check;

ALTER TABLE usuario_admin
ADD CONSTRAINT usuario_admin_tipo_usuario_check
CHECK (tipo_usuario IN ('ADMIN'));

-- Adicionar coluna de id_usuario da tabela usuario (relacionamento)
ALTER TABLE usuario_admin
ADD COLUMN id_usuario BIGINT REFERENCES usuario(id) ON DELETE CASCADE;
