alter table pedido add codigo varchar(36) not null after id;

SET SQL_SAFE_UPDATES = 0;
update pedido set codigo = uuid();
SET SQL_SAFE_UPDATES = 1;

alter table pedido add constraint uk_pedido_codigo unique(codigo);