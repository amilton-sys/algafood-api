CREATE TABLE estado (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(80),
    PRIMARY KEY (id)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

insert into estado (nome) select distinct nome_estado from cidade;

alter table cidade add column estado_id bigint not null;

set sql_safe_updates=0;

UPDATE cidade c 
SET 
    c.estado_id = (SELECT 
            e.id
        FROM
            estado e
        WHERE
            e.nome = c.nome_estado);

alter table cidade add constraint fk_cidade_estado 
foreign key (estado_id) references estado (id);

alter table cidade drop column nome_estado;

alter table cidade change nome_cidade nome varchar(80) not null; 

