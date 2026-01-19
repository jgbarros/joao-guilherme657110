package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RegionalRequest {
    @NotBlank(message = "O nome é obrigatório")
    private String nome;
    
    private Boolean ativo = true;
}
