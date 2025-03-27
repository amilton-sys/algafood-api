CREATE TABLE forma_pagamento (
    id BIGINT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(60) NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE grupo (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(60) NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE grupo_permissao (
    grupo_id BIGINT NOT NULL,
    permissao_id BIGINT NOT NULL,
    PRIMARY KEY (grupo_id , permissao_id)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE permissao (
    id BIGINT NOT NULL AUTO_INCREMENT,
    descricao VARCHAR(60) NOT NULL,
    nome VARCHAR(100) NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE produto (
    id BIGINT NOT NULL AUTO_INCREMENT,
    restaurante_id BIGINT NOT NULL,
    nome VARCHAR(80) NOT NULL,
    descricao TEXT NOT NULL,
    preco DECIMAL(10 , 2 ) NOT NULL,
    ativo TINYINT(1) NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE restaurante (
    id BIGINT NOT NULL AUTO_INCREMENT,
    cozinha_id BIGINT NOT NULL,
    nome VARCHAR(80) NOT NULL,
    taxa_frete DECIMAL(10 , 2 ) NOT NULL,
    data_atualizacao DATETIME NOT NULL,
    data_cadastro DATETIME NOT NULL,
    endereco_cidade_id BIGINT,
    endereco_cep VARCHAR(9),
    endereco_logradouro VARCHAR(100),
    endereco_numero VARCHAR(20),
    endereco_complemento VARCHAR(60),
    endereco_bairro VARCHAR(60),
    PRIMARY KEY (id)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE restaurante_forma_pagamento (
    restaurante_id BIGINT NOT NULL,
    forma_pagamento_id BIGINT NOT NULL,
    PRIMARY KEY (restaurante_id , forma_pagamento_id)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE usuario (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nome VARCHAR(80) NOT NULL,
    email VARCHAR(255) NOT NULL,
    senha VARCHAR(255) NOT NULL,
    data_cadastro DATETIME NOT NULL,
    PRIMARY KEY (id)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

CREATE TABLE usuario_grupo (
    usuario_id BIGINT NOT NULL,
    grupo_id BIGINT NOT NULL,
    PRIMARY KEY (usuario_id , grupo_id)
)  ENGINE=INNODB DEFAULT CHARSET=UTF8MB4;

alter table grupo_permissao add constraint fk_grupo_permissao_permissao foreign key (permissao_id) references permissao (id);

alter table grupo_permissao add constraint fk_grupo_permissao_grupo foreign key (grupo_id) references grupo (id);

alter table produto add constraint fk_produto_restaurante foreign key (restaurante_id) references restaurante (id);

alter table restaurante add constraint fk_restaurante_cozinha foreign key (cozinha_id) references cozinha (id);

alter table restaurante add constraint fk_restaurante_cidade foreign key (endereco_cidade_id) references cidade (id);

alter table restaurante_forma_pagamento add constraint fk_rest_forma_pagto_forma_pagto foreign key (forma_pagamento_id) references forma_pagamento (id);

alter table restaurante_forma_pagamento add constraint fk_rest_forma_pagto_restaurante foreign key (restaurante_id) references restaurante (id);

alter table usuario_grupo add constraint fk_usuario_grupo_grupo foreign key (grupo_id) references grupo (id);

alter table usuario_grupo add constraint fk_usuario_grupo_usuario foreign key (usuario_id) references usuario (id);