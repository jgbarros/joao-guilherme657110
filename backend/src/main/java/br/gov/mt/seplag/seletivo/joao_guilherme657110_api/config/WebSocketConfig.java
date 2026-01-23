package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.config;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.config.handler.AlbumWebSocketHandler;
import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.config.handler.ArtistaWebSocketHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    private final ArtistaWebSocketHandler artistaHandler;
    private final AlbumWebSocketHandler albumHandler;

    public WebSocketConfig(ArtistaWebSocketHandler artistaHandler, AlbumWebSocketHandler albumHandler) {
        this.artistaHandler = artistaHandler;
        this.albumHandler = albumHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(artistaHandler, "/ws/artistas")
                .setAllowedOrigins("*");
        registry.addHandler(albumHandler, "/ws/albuns")
                .setAllowedOrigins("*");
    }
}
