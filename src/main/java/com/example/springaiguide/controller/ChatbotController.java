package com.example.springaiguide.controller;

import com.example.springaiguide.model.RecommendationRequest;
import com.example.springaiguide.model.RecommendationResponse;
import com.example.springaiguide.service.RecommendationService;
import com.example.springaiguide.service.WeatherService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST que expone el chatbot de recomendaciones de viaje.
 *
 * <p>Flujo principal:
 * <ol>
 *   <li>El cliente envía el nombre de un destino.</li>
 *   <li>WeatherService consulta la API gratuita de Open-Meteo para obtener el clima.</li>
 *   <li>RecommendationService envía el reporte a OpenAI (via Spring AI).</li>
 *   <li>Se devuelve la recomendación generada por la IA.</li>
 * </ol>
 * </p>
 *
 * <p>Ejemplo de petición:
 * <pre>
 * POST /api/recommend
 * {
 *   "location": "Cartagena de Indias",
 *   "additionalContext": "Viaje familiar con niños pequeños, verano"
 * }
 * </pre>
 * </p>
 */
@RestController
@RequestMapping("/api")
public class ChatbotController {

    private static final Logger logger = LoggerFactory.getLogger(ChatbotController.class);

    private final WeatherService weatherService;
    private final RecommendationService recommendationService;

    public ChatbotController(WeatherService weatherService,
                             RecommendationService recommendationService) {
        this.weatherService = weatherService;
        this.recommendationService = recommendationService;
    }

    /**
     * Endpoint principal: recibe un destino, consulta el clima y pide a la IA
     * que recomiende si vale la pena visitarlo.
     *
     * @param request objeto con el nombre del destino y contexto adicional opcional
     * @return recomendación generada por OpenAI junto con el resumen del clima
     */
    @PostMapping("/recommend")
    public ResponseEntity<RecommendationResponse> recommend(@RequestBody RecommendationRequest request) {
        logger.info("Solicitud de recomendación para: {}", request.getLocation());

        String weatherReport = weatherService.getWeatherReport(request.getLocation());
        String aiRecommendation = recommendationService.getRecommendation(
                weatherReport, request.getAdditionalContext());

        RecommendationResponse response = new RecommendationResponse(
                request.getLocation(), weatherReport, aiRecommendation);

        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint GET alternativo para pruebas rápidas desde el navegador o curl.
     *
     * <p>Ejemplo: GET /api/recommend?location=Bogota&context=fin+de+semana</p>
     *
     * @param location nombre del destino (requerido)
     * @param context  contexto adicional (opcional)
     */
    @GetMapping("/recommend")
    public ResponseEntity<RecommendationResponse> recommendGet(
            @RequestParam String location,
            @RequestParam(required = false) String context) {

        logger.info("Solicitud GET de recomendación para: {}", location);

        String weatherReport = weatherService.getWeatherReport(location);
        String aiRecommendation = recommendationService.getRecommendation(weatherReport, context);

        RecommendationResponse response = new RecommendationResponse(
                location, weatherReport, aiRecommendation);

        return ResponseEntity.ok(response);
    }
}
