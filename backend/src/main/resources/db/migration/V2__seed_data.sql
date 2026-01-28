-- Regionais (simule sync inicial; integre API depois)
INSERT INTO regionais (nome, ativo) VALUES
('NÃO SE APLICA - ACADEPOL', true), ('NÃO SE APLICA - CORREGEPOL', true);

-- Artistas e álbuns exemplos
INSERT INTO artistas (nome, nacionalidade, data_nascimento, biografia) VALUES 
('Serj Tankian', 'Armênia', '1972-08-21', 'Vocalista do System of a Down'),
('Guns N Roses', 'EUA', '1985-06-01', 'Banda de hard rock');

INSERT INTO albuns (artista_id, titulo, ano_lancamento, genero, capa_url, faixas, regional_id) VALUES 
(1, 'Harakiri', 2012, 'Alternative Metal', NULL, '["Imperium", "Ching Chime"]', 1),
(2, 'Use Your Illusion I', 1991, 'Hard Rock', NULL, '["November Rain"]', 1);
