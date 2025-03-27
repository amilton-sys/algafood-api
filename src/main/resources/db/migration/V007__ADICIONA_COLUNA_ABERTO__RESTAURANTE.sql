alter table restaurante add aberto tinyint(1) not null;

SET SQL_SAFE_UPDATES = 0;
UPDATE restaurante SET aberto = true;
SET SQL_SAFE_UPDATES = 1;