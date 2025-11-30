-- Adicionar coluna id_admin_criador em eventos para rastrear qual admin criou
ALTER TABLE eventos 
ADD COLUMN id_admin_criador BIGINT NOT NULL DEFAULT 1 REFERENCES usuario_admin(id) ON DELETE CASCADE;

-- Adicionar coluna numero_ingressos_disponiveis
ALTER TABLE eventos 
ADD COLUMN numero_ingressos_disponiveis INT DEFAULT 0;
