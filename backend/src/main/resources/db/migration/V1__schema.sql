CREATE TABLE regionais (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(100) UNIQUE NOT NULL,
    ativo BOOLEAN DEFAULT true
);

CREATE TABLE artistas (
    id BIGSERIAL PRIMARY KEY,
    nome VARCHAR(200) UNIQUE NOT NULL,
    nacionalidade VARCHAR(100),
    data_nascimento DATE,
    data_morte DATE,
    biografia TEXT
);

CREATE TABLE albuns (
    id BIGSERIAL PRIMARY KEY,
    artista_id BIGINT REFERENCES artistas(id) ON DELETE CASCADE,
    titulo VARCHAR(300) NOT NULL,
    ano_lancamento INTEGER CHECK (ano_lancamento > 1900),
    genero VARCHAR(100),
    capa_url VARCHAR(500),
    faixas JSONB,
    regional_id BIGINT REFERENCES regionais(id)
);

-- Índices para performance (paginação/busca)
CREATE INDEX idx_artistas_nome ON artistas(nome);
CREATE INDEX idx_albuns_artista ON albuns(artista_id);
CREATE INDEX idx_albuns_titulo ON albuns(titulo);
CREATE INDEX idx_albuns_ano ON albuns(ano_lancamento);