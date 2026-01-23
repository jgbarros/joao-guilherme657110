package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.dto.ws;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumNotification {
    private Long albumId;
    private String albumTitle;
    private String artistName;
    private LocalDateTime createdAt;
    private String message;
}
