
insert into Usuarios (id,username,password,role) values(100,'ana@email.com','$2a$12$EnQ8aUs/yYrPfn0V2o5InOFSmBxNC5VLMAEt2vfgF42jlI.XLNyFq','ROLE_ADMIN');
insert into Usuarios (id,username,password,role) values(101,'bia@email.com','$2a$12$EnQ8aUs/yYrPfn0V2o5InOFSmBxNC5VLMAEt2vfgF42jlI.XLNyFq','ROLE_CLIENTE');
insert into Usuarios (id,username,password,role) values(102,'beatriz@email.com','$2a$12$EnQ8aUs/yYrPfn0V2o5InOFSmBxNC5VLMAEt2vfgF42jlI.XLNyFq','ROLE_CLIENTE');

insert into Clientes (id,nome,cpf, id_usuario) values(21,'Bianca Silva','48810691016',101);
insert into Clientes (id,nome,cpf, id_usuario) values(22,'Roberto Gomes','51224756010',102); 

insert into Vagas (id, codigo,status) values (100, 'A-01', 'OCUPADA');
insert into Vagas (id, codigo,status) values (200, 'A-02', 'OCUPADA');
insert into Vagas (id, codigo,status) values (300, 'A-03', 'OCUPADA');
insert into Vagas (id, codigo,status) values (400, 'A-04', 'LIVRE');
insert into Vagas (id, codigo,status) values (500, 'A-05', 'LIVRE');

INSERT INTO CLIENTES_TEM_VAGAS (numero_recibo, placa, marca, modelo,cor, data_entrada, id_cliente, id_vaga)
    VALUES ('20230313-101300','FIT-1020','FIAT','PALIO','VERDE','2023-03-13 10:15:00', 22 ,100);

INSERT INTO CLIENTES_TEM_VAGAS (numero_recibo, placa, marca, modelo,cor, data_entrada, id_cliente, id_vaga)
    VALUES ('20233014-101400','SIE-1020','FIAT','SIENA','BRANCO','2023-03-14 10:15:00', 21 ,200);

INSERT INTO CLIENTES_TEM_VAGAS (numero_recibo, placa, marca, modelo,cor, data_entrada, id_cliente, id_vaga)
    VALUES ('20230315-101500','FIT-1020','FIT','PALIO','VERDE','2023-03-13 10:15:00', 22 ,300);