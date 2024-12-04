INSERT INTO USUARIOS (id, username, password, role) VALUES (100, 'ana@gmail.com', '$2a$12$BSWj2bLClN7wJniwoyQugOXd/mwaQnFhQ0RI5DKFn7yM24m3j6Eca', 'ROLE_ADMIN');
INSERT INTO USUARIOS (id, username, password, role) VALUES (101, 'marcos@gmail.com', '$2a$12$BSWj2bLClN7wJniwoyQugOXd/mwaQnFhQ0RI5DKFn7yM24m3j6Eca', 'ROLE_CLIENTE');
INSERT INTO USUARIOS (id, username, password, role) VALUES (102, 'bia@gmail.com', '$2a$12$BSWj2bLClN7wJniwoyQugOXd/mwaQnFhQ0RI5DKFn7yM24m3j6Eca', 'ROLE_CLIENTE');
INSERT INTO USUARIOS (id, username, password, role) VALUES (103, 'toby@gmail.com', '$2a$12$BSWj2bLClN7wJniwoyQugOXd/mwaQnFhQ0RI5DKFn7yM24m3j6Eca', 'ROLE_CLIENTE');

INSERT INTO CLIENTES(id, nome, cpf, id_usuario) VALUES (10, 'Marcos Antonio Porto Matos', '92740337027', 101);
INSERT INTO CLIENTES(id, nome, cpf, id_usuario) VALUES (20, 'Bianca Silva', '37448384040', 102);
