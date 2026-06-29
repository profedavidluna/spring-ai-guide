package com.example.springaiguide.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

/**
 * Servicio que utiliza Spring AI para conectarse a OpenAI y generar
 * recomendaciones de viaje basadas en reportes meteorológicos.
 *
 * <p>Spring AI abstrae la complejidad de la integración con OpenAI:
 * solo necesitas configurar {@code spring.ai.openai.api-key} en
 * application.properties y Spring AI se encarga del resto.</p>
 *
 * <p>Documentación de Spring AI:
 * https://docs.spring.io/spring-ai/reference/</p>
 */
@Service
public class RecommendationService {

    private static final Logger logger = LoggerFactory.getLogger(RecommendationService.class);

    private static final String SYSTEM_PROMPT = """
            Eres un asistente de viajes experto y amigable.
            Tu tarea es analizar reportes meteorológicos y dar recomendaciones
            claras y útiles sobre si vale la pena visitar un destino en este momento.
            
            Tu respuesta debe:
            - Ser en español
            - Empezar con una conclusión clara (Sí recomiendo / No recomiendo / Depende)
            - Explicar los factores climáticos más relevantes
            - Sugerir qué tipo de ropa o equipo llevar
            - Mencionar actividades recomendadas o desaconsejadas según el clima
            - Ser amigable y motivadora
            """;

    private final ChatClient chatClient;

    public RecommendationService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }

    /**
     * Genera una recomendación de viaje usando OpenAI a partir del reporte meteorológico.
     *
     * @param weatherReport  reporte meteorológico en texto (generado por WeatherService)
     * @param additionalContext contexto adicional del usuario (opcional)
     * @return recomendación generada por la IA
     */
    public String getRecommendation(String weatherReport, String additionalContext) {
        String userMessage = buildUserMessage(weatherReport, additionalContext);
        logger.debug("Enviando mensaje a OpenAI...");

        String response = chatClient.prompt()
                .user(userMessage)
                .call()
                .content();

        logger.debug("Respuesta recibida de OpenAI");
        return response;
    }

    /**
     * Construye el mensaje del usuario combinando el reporte climático
     * con cualquier contexto adicional que el usuario haya proporcionado.
     */
    private String buildUserMessage(String weatherReport, String additionalContext) {
        StringBuilder message = new StringBuilder();
        message.append("Por favor analiza el siguiente reporte meteorológico ");
        message.append("y dime si vale la pena visitar este destino:\n\n");
        message.append(weatherReport);

        if (additionalContext != null && !additionalContext.isBlank()) {
            message.append("\n\nContexto adicional del viajero: ").append(additionalContext);
        }

        return message.toString();
    }
}
