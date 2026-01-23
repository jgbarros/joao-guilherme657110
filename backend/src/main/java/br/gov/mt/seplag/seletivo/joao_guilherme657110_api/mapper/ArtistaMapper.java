package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.mapper;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ArtistaResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Artista;
import org.springframework.stereotype.Component;

@Component
public class ArtistaMapper {

    public Artista toEntity(ArtistaResponse response) {
        if (response == null) {
            return null;
        }
        Artista artista = new Artista();
        artista.setId(response.getId());
        artista.setNome(response.getNome());
        artista.setNacionalidade(response.getNacionalidade());
        artista.setDataNascimento(response.getDataNascimento());
        artista.setDataMorte(response.getDataMorte());
        artista.setBiografia(response.getBiografia());
        return artista;
    }

    public ArtistaResponse toResponse(Artista entity) {
        if (entity == null) {
            return null;
        }
        return ArtistaResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .nacionalidade(entity.getNacionalidade())
                .dataNascimento(entity.getDataNascimento())
                .dataMorte(entity.getDataMorte())
                .biografia(entity.getBiografia())
                .albumCount(entity.getAlbuns() != null ? (long) entity.getAlbuns().size() : 0L)
                .build();
    }
}
