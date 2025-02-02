INSERT INTO USUARIOS (id, username, password, role) VALUES (100, 'ana@gmail.com', '$2a$12$BSWj2bLClN7wJniwoyQugOXd/mwaQnFhQ0RI5DKFn7yM24m3j6Eca', 'ROLE_ADMIN');
INSERT INTO USUARIOS (id, username, password, role) VALUES (101, 'marcos@gmail.com', '$2a$12$BSWj2bLClN7wJniwoyQugOXd/mwaQnFhQ0RI5DKFn7yM24m3j6Eca', 'ROLE_CLIENTE');
INSERT INTO USUARIOS (id, username, password, role) VALUES (102, 'bia@gmail.com', '$2a$12$BSWj2bLClN7wJniwoyQugOXd/mwaQnFhQ0RI5DKFn7yM24m3j6Eca', 'ROLE_CLIENTE');

insert into CLIENTES (id, nome, cpf, id_usuario) values (21, 'Marcos Porto', '09191773016', 101);
insert into CLIENTES (id, nome, cpf, id_usuario) values (22, 'Biatriz Rodrigues', '98401203015', 102);

insert into vagas (id, codigo, status) values (100, 'A-01', 'OCUPADA');
insert into vagas (id, codigo, status) values (200, 'A-02', 'OCUPADA');
insert into vagas (id, codigo, status) values (300, 'A-03', 'OCUPADA');
insert into vagas (id, codigo, status) values (400, 'A-04', 'LIVRE');
insert into vagas (id, codigo, status) values (500, 'A-05', 'LIVRE');

insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230313-101300', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-13 10:15:00', 22, 100);
insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230314-101400', 'SIE-1020', 'FIAT', 'SIENA', 'BRANCO', '2023-03-14 10:15:00', 21, 200);
insert into clientes_tem_vagas (numero_recibo, placa, marca, modelo, cor, data_entrada, id_cliente, id_vaga)
    values ('20230315-101500', 'FIT-1020', 'FIAT', 'PALIO', 'VERDE', '2023-03-14 10:15:00', 22, 300);