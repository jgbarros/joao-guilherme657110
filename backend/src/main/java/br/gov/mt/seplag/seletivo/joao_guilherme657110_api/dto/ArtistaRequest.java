package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ArtistaRequest {

    @NotBlank(message = "O nome do artista é obrigatório.")
    @Size(max = 200, message = "O nome do artista deve ter no máximo 200 caracteres.")
    private String nome;

    @Size(max = 100, message = "A nacionalidade deve ter no máximo 100 caracteres.")
    private String nacionalidade;

    private LocalDate dataNascimento;

    private LocalDate dataMorte;

    private String biografia;
}
