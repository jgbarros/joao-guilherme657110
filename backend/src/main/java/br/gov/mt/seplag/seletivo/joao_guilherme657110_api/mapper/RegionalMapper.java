package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.mapper;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.RegionalResponse;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.entity.Regional;
import org.springframework.stereotype.Component;

@Component
public class RegionalMapper {

    public Regional toEntity(RegionalResponse response) {
        if (response == null) {
            return null;
        }
        Regional regional = new Regional();
        regional.setId(response.getId());
        regional.setNome(response.getNome());
        regional.setAtivo(response.getAtivo());
        return regional;
    }

    public RegionalResponse toResponse(Regional entity) {
        if (entity == null) {
            return null;
        }
        return RegionalResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .ativo(entity.getAtivo())
                .build();
    }
}
