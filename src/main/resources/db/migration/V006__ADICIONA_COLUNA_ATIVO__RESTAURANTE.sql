alter table restaurante add ativo tinyint(1) not null;

SET SQL_SAFE_UPDATES = 0;
UPDATE restaurante SET ativo = true;
SET SQL_SAFE_UPDATES = 1;