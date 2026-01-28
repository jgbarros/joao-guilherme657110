package br.gov.mt.seplag.seletivo.joao_guilherme657110_api.config;

import br.gov.mt.seplag.seletivo.joao_guilherme657110_api.service.RegionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RegionalService regionalService;

    @Override
    public void run(String... args) throws Exception {
        System.out.println("Inicializando banco de dados com regionais da API externa...");
        try {
            regionalService.syncFromExternalApi();
            System.out.println("Sincronização de regionais concluída com sucesso.");
        } catch (Exception e) {
            System.err.println("Erro ao sincronizar regionais na inicialização: " + e.getMessage());
        }
    }
}
