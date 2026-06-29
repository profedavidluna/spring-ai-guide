package com.example.springaiguide.model;

/**
 * Petición para obtener una recomendación sobre un lugar.
 * El usuario envía el nombre de una ciudad o destino y, opcionalmente,
 * un contexto adicional (época del año, tipo de viaje, etc.).
 */
public class RecommendationRequest {

    private String location;
    private String additionalContext;

    public RecommendationRequest() {}

    public RecommendationRequest(String location, String additionalContext) {
        this.location = location;
        this.additionalContext = additionalContext;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAdditionalContext() {
        return additionalContext;
    }

    public void setAdditionalContext(String additionalContext) {
        this.additionalContext = additionalContext;
    }
}
