package com.example.springaiguide.service;

import com.example.springaiguide.model.GeocodingResult;
import com.example.springaiguide.model.WeatherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Servicio para obtener información meteorológica usando la API gratuita de Open-Meteo.
 *
 * <p>Open-Meteo es completamente gratuita, no requiere registro ni API key.
 * Documentación: https://open-meteo.com/en/docs</p>
 *
 * <p>Flujo:
 * <ol>
 *   <li>Geocodificación: convertir el nombre de la ciudad a coordenadas.</li>
 *   <li>Pronóstico: obtener el clima actual y el pronóstico de 7 días.</li>
 * </ol>
 * </p>
 */
@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private static final String GEOCODING_URL = "https://geocoding-api.open-meteo.com/v1/search";
    private static final String WEATHER_URL = "https://api.open-meteo.com/v1/forecast";

    private final RestTemplate restTemplate;

    public WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Obtiene un reporte meteorológico completo para la ciudad indicada.
     *
     * @param cityName nombre de la ciudad (p.ej. "Bogotá", "Paris", "Tokyo")
     * @return reporte de clima en formato texto legible por la IA
     */
    public String getWeatherReport(String cityName) {
        GeocodingResult.GeoLocation location = geocode(cityName);
        WeatherData weatherData = fetchWeather(location.getLatitude(), location.getLongitude());
        return buildWeatherReport(location, weatherData);
    }

    /**
     * Convierte el nombre de una ciudad a coordenadas geográficas.
     */
    public GeocodingResult.GeoLocation geocode(String cityName) {
        String url = UriComponentsBuilder.fromHttpUrl(GEOCODING_URL)
                .queryParam("name", cityName)
                .queryParam("count", 1)
                .queryParam("language", "es")
                .queryParam("format", "json")
                .toUriString();

        logger.debug("Geocodificando '{}': {}", cityName, url);
        GeocodingResult result = restTemplate.getForObject(url, GeocodingResult.class);

        if (result == null || result.getResults() == null || result.getResults().isEmpty()) {
            throw new IllegalArgumentException("No se encontró la ciudad: " + cityName);
        }
        return result.getResults().get(0);
    }

    /**
     * Obtiene datos meteorológicos actuales y pronóstico de 7 días para las coordenadas dadas.
     */
    public WeatherData fetchWeather(double latitude, double longitude) {
        String url = UriComponentsBuilder.fromHttpUrl(WEATHER_URL)
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .queryParam("current", "temperature_2m,relative_humidity_2m,weather_code,wind_speed_10m")
                .queryParam("daily", "temperature_2m_max,temperature_2m_min,precipitation_sum,weather_code")
                .queryParam("timezone", "auto")
                .queryParam("forecast_days", 7)
                .toUriString();

        logger.debug("Consultando clima en ({}, {}): {}", latitude, longitude, url);
        return restTemplate.getForObject(url, WeatherData.class);
    }

    /**
     * Construye un reporte de texto estructurado a partir de los datos meteorológicos.
     * Este texto será enviado a la IA para que genere la recomendación.
     */
    public String buildWeatherReport(GeocodingResult.GeoLocation location, WeatherData data) {
        StringBuilder report = new StringBuilder();
        report.append("=== REPORTE METEOROLÓGICO ===\n");
        report.append("Destino: ").append(location).append("\n");
        report.append("Zona horaria: ").append(data.getTimezone()).append("\n\n");

        WeatherData.CurrentWeather current = data.getCurrent();
        if (current != null) {
            report.append("--- Condiciones Actuales ---\n");
            report.append("Temperatura: ").append(current.getTemperature()).append(" °C\n");
            report.append("Humedad relativa: ").append(current.getHumidity()).append("%\n");
            report.append("Velocidad del viento: ").append(current.getWindSpeed()).append(" km/h\n");
            report.append("Código de clima: ").append(describeWeatherCode(current.getWeatherCode())).append("\n\n");
        }

        WeatherData.DailyWeather daily = data.getDaily();
        if (daily != null && daily.getTime() != null) {
            report.append("--- Pronóstico 7 Días ---\n");
            List<String> days = daily.getTime();
            for (int i = 0; i < days.size(); i++) {
                report.append(days.get(i)).append(": ");
                report.append("Máx ").append(safeGet(daily.getTemperatureMax(), i)).append("°C, ");
                report.append("Mín ").append(safeGet(daily.getTemperatureMin(), i)).append("°C, ");
                report.append("Precipitación ").append(safeGet(daily.getPrecipitationSum(), i)).append(" mm, ");
                report.append(describeWeatherCode(safeGetInt(daily.getWeatherCode(), i)));
                report.append("\n");
            }
        }
        return report.toString();
    }

    /**
     * Traduce el código WMO de clima a una descripción legible.
     * Referencia: https://open-meteo.com/en/docs#weathervariables
     */
    private String describeWeatherCode(int code) {
        if (code == 0) return "Despejado";
        if (code <= 2) return "Parcialmente nublado";
        if (code == 3) return "Nublado";
        if (code <= 49) return "Niebla";
        if (code <= 59) return "Llovizna";
        if (code <= 69) return "Lluvia";
        if (code <= 79) return "Nevada";
        if (code <= 84) return "Chubascos";
        if (code <= 99) return "Tormenta";
        return "Desconocido (" + code + ")";
    }

    private <T> T safeGet(List<T> list, int index) {
        if (list == null || index >= list.size()) return null;
        return list.get(index);
    }

    private int safeGetInt(List<Integer> list, int index) {
        if (list == null || index >= list.size()) return -1;
        Integer val = list.get(index);
        return val != null ? val : -1;
    }
}
