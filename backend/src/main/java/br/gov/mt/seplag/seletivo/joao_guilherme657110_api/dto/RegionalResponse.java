package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegionalResponse {
    private Long id;
    private String nome;
    private Boolean ativo;
}
