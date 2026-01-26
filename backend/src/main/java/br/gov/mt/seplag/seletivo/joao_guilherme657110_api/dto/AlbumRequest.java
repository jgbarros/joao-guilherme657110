package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlbumRequest {
    @NotBlank(message = "O título é obrigatório")
    private String titulo;

    @NotNull(message = "O artista é obrigatório")
    private Long artistaId;

    @Min(value = 1901, message = "O ano de lançamento deve ser superior a 1900")
    private Integer anoLancamento;

    private String genero;
    private String capaUrl;
    private String faixas;
    private Long regionalId;
}
