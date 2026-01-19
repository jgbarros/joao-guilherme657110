package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.mapper;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.AlbumResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Album;
import org.springframework.stereotype.Component;

@Component
public class AlbumMapper {

    public AlbumResponse toResponse(Album entity) {
        if (entity == null) {
            return null;
        }
        return AlbumResponse.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .anoLancamento(entity.getAnoLancamento())
                .genero(entity.getGenero())
                .capaUrl(entity.getCapaUrl())
                .faixas(entity.getFaixas())
                .artistaId(entity.getArtista() != null ? entity.getArtista().getId() : null)
                .artistaNome(entity.getArtista() != null ? entity.getArtista().getNome() : null)
                .regionalId(entity.getRegional() != null ? entity.getRegional().getId() : null)
                .regionalNome(entity.getRegional() != null ? entity.getRegional().getNome() : null)
                .build();
    }
}
