-- Clientes sem indicador
INSERT INTO cliente (
    fk_indicador, nome, email, rg, cpf, cnpj, data_nascimento, data_inicio,
    endereco, cep, descricao, inscricao_estadual, is_pro_bono, is_ativo, is_juridico
)
VALUES
(NULL, 'João da Silva', 'joao.silva@email.com', '1234567890', '12345678909', NULL, '1990-05-10', '2023-01-01',
 'Rua das Laranjeiras, 100', '12345678', 'Cliente regular', '123456789', FALSE, TRUE, FALSE),

(NULL, 'Empresa X Ltda', 'contato@empresax.com', NULL, NULL, '12345678000199', '2000-01-01', '2022-06-15',
 'Av. Paulista, 1500', '87654321', 'Cliente PJ padrão', '987654321', TRUE, TRUE, TRUE),

(NULL, 'Lucas Pereira', 'lucas.pereira@email.com', '9876543210', '98765432100', NULL, '1988-12-30', '2023-08-12',
 'Rua do Mercado, 300', '44556677', 'Cliente Pro Bono', '555666777', TRUE, TRUE, FALSE),

(NULL, 'Tech Solutions ME', 'contato@techsolutions.com', NULL, NULL, '44556677000188', '1995-07-20', '2021-09-01',
 'Rua das Flores, 900', '33445566', 'Empresa de tecnologia', '223344556', FALSE, TRUE, TRUE);

-- Clientes com indicador (supondo que os IDs dos clientes acima vão de 1 a 4)
INSERT INTO cliente (
    fk_indicador, nome, email, rg, cpf, cnpj, data_nascimento, data_inicio,
    endereco, cep, descricao, inscricao_estadual, is_pro_bono, is_ativo, is_juridico
)
VALUES
(1, 'Maria Oliveira', 'maria.oliveira@email.com', '0987654321', '98765432100', NULL, '1985-09-25', '2024-03-10',
 'Rua do Sol, 200', '11223344', 'Indicada pelo João', '111222333', FALSE, TRUE, FALSE),

(2, 'Serviços Gerais LTDA', 'servicos@gerais.com', NULL, NULL, '11223344000155', '2010-02-01', '2020-05-20',
 'Av. Central, 505', '77889900', 'Indicada pela Empresa X', '332211000', FALSE, TRUE, TRUE),

(3, 'Fernanda Lima', 'fernanda.lima@email.com', '1029384756', '19283746500', NULL, '1992-11-11', '2023-11-01',
 'Rua Esperança, 101', '55667788', 'Indicada por Lucas', '889900112', TRUE, TRUE, FALSE);

-- Clientes inativos
INSERT INTO cliente (
    fk_indicador, nome, email, rg, cpf, cnpj, data_nascimento, data_inicio,
    endereco, cep, descricao, inscricao_estadual, is_pro_bono, is_ativo, is_juridico
)
VALUES
(NULL, 'Carlos Souza', 'carlos.souza@email.com', '0192837465', '56473829100', NULL, '1979-03-03', '2020-10-10',
 'Rua Velha, 22', '66778899', 'Cliente inativo', '777888999', FALSE, FALSE, FALSE),

(4, 'Alpha Corp', 'contato@alphacorp.com', NULL, NULL, '55667788000122', '1999-06-06', '2022-01-15',
 'Av. Business, 700', '99887766', 'Empresa inativa', '444555666', TRUE, FALSE, TRUE);


-- Notas fiscais vinculadas aos clientes acima (supondo IDs de 1 a 9)
INSERT INTO nota_fiscal (
    fk_cliente, data_criacao, etiqueta, valor, data_vencimento, descricao, url_nuvem, is_emitida
)
VALUES
(1, CURRENT_DATE, 'NF-Joao', 1500.00, '2025-07-01', 'Nota referente a serviços prestados', 'https://nuvem.com/nf1', TRUE),
(2, CURRENT_DATE, 'NF-EmpresaX', 3500.00, '2025-07-10', 'Nota de manutenção', 'https://nuvem.com/nf2', FALSE),
(3, CURRENT_DATE, 'NF-Lucas', 800.00, '2025-07-05', 'Nota de consultoria', 'https://nuvem.com/nf3', TRUE),
(5, CURRENT_DATE, 'NF-Maria', 1200.00, '2025-07-15', 'Serviços de assessoria', 'https://nuvem.com/nf4', FALSE),
(6, CURRENT_DATE, 'NF-ServicosGerais', 2200.00, '2025-08-01', 'Prestação de serviços diversos', 'https://nuvem.com/nf5', TRUE);
