package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ArtistaResponse {
    private Long id;
    private String nome;
    private String nacionalidade;
    private LocalDate dataNascimento;
    private LocalDate dataMorte;
    private String biografia;
    private Long albumCount;
}
