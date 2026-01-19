package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumResponse {
    private Long id;
    private String titulo;
    private Integer anoLancamento;
    private String genero;
    private String capaUrl;
    private String faixas;
    private Long artistaId;
    private String artistaNome;
    private Long regionalId;
    private String regionalNome;
}
