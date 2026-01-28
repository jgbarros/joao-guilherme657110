package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ArtistaDetalheResponse {
    private Long id;
    private String nome;
    private String nacionalidade;
    private List<AlbumSummary> albuns;

    @Data
    @Builder
    public static class AlbumSummary {
        private Long id;
        private String titulo;
        private Integer anoLancamento;
        private String capaUrl;
    }
}
