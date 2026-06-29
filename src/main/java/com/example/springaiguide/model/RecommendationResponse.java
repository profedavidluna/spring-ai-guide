package com.example.springaiguide.model;

/**
 * Respuesta con la recomendación generada por la IA basándose en los
 * datos meteorológicos del destino solicitado.
 */
public class RecommendationResponse {

    private String location;
    private String weatherSummary;
    private String aiRecommendation;

    public RecommendationResponse() {}

    public RecommendationResponse(String location, String weatherSummary, String aiRecommendation) {
        this.location = location;
        this.weatherSummary = weatherSummary;
        this.aiRecommendation = aiRecommendation;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeatherSummary() {
        return weatherSummary;
    }

    public void setWeatherSummary(String weatherSummary) {
        this.weatherSummary = weatherSummary;
    }

    public String getAiRecommendation() {
        return aiRecommendation;
    }

    public void setAiRecommendation(String aiRecommendation) {
        this.aiRecommendation = aiRecommendation;
    }
}
