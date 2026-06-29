package com.example.springaiguide.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * Configuración principal de la aplicación.
 * Aquí registramos los beans compartidos.
 */
@Configuration
public class AppConfig {

    /**
     * RestTemplate para llamadas HTTP a APIs externas (Open-Meteo).
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
