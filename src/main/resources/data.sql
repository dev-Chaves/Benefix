INSERT INTO tb_enterprise (id, enterprise, cnpj, created_at)
VALUES (1, 'Empresa Padrão', '12345678000100', now());

INSERT INTO tb_user (id, name, password, enterprise_id, role, created_at)
VALUES (1, 'admin', 'admin', 1, 'ADMIN', now());
