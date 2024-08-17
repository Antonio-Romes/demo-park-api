
insert into Usuarios (id,username,password,role) values(100,'ana@email.com','$2a$12$EnQ8aUs/yYrPfn0V2o5InOFSmBxNC5VLMAEt2vfgF42jlI.XLNyFq','ROLE_ADMIN');
insert into Usuarios (id,username,password,role) values(101,'bia@email.com','$2a$12$EnQ8aUs/yYrPfn0V2o5InOFSmBxNC5VLMAEt2vfgF42jlI.XLNyFq','ROLE_CLIENTE');
insert into Usuarios (id,username,password,role) values(102,'bob@email.com','$2a$12$EnQ8aUs/yYrPfn0V2o5InOFSmBxNC5VLMAEt2vfgF42jlI.XLNyFq','ROLE_CLIENTE');
insert into Usuarios (id,username,password,role) values(103,'toby@email.com','$2a$12$EnQ8aUs/yYrPfn0V2o5InOFSmBxNC5VLMAEt2vfgF42jlI.XLNyFq','ROLE_CLIENTE');


insert into Clientes (id,nome,cpf, id_usuario) values(10,'Bianca Silva','48810691016',101);
insert into Clientes (id,nome,cpf, id_usuario) values(20,'Roberto Gomes','51224756010',102); 