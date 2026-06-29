package com.example.springaiguide;

import com.example.springaiguide.model.GeocodingResult;
import com.example.springaiguide.model.WeatherData;
import com.example.springaiguide.service.WeatherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios para WeatherService.
 * Usamos Mockito para simular las llamadas HTTP y no depender
 * de la conectividad de red durante las pruebas.
 */
class WeatherServiceTest {

    private RestTemplate restTemplateMock;
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        restTemplateMock = mock(RestTemplate.class);
        weatherService = new WeatherService(restTemplateMock);
    }

    @Test
    void buildWeatherReport_withValidData_returnsFormattedReport() {
        GeocodingResult.GeoLocation location = new GeocodingResult.GeoLocation();
        location.setName("Bogotá");
        location.setLatitude(4.711);
        location.setLongitude(-74.0721);
        location.setCountry("Colombia");

        WeatherData weatherData = new WeatherData();
        weatherData.setTimezone("America/Bogota");

        WeatherData.CurrentWeather current = new WeatherData.CurrentWeather();
        current.setTemperature(18.5);
        current.setHumidity(75);
        current.setWeatherCode(2);
        current.setWindSpeed(12.0);
        weatherData.setCurrent(current);

        WeatherData.DailyWeather daily = new WeatherData.DailyWeather();
        daily.setTime(List.of("2024-01-01", "2024-01-02"));
        daily.setTemperatureMax(Arrays.asList(20.0, 22.0));
        daily.setTemperatureMin(Arrays.asList(10.0, 12.0));
        daily.setPrecipitationSum(Arrays.asList(0.0, 5.0));
        daily.setWeatherCode(Arrays.asList(0, 61));
        weatherData.setDaily(daily);

        String report = weatherService.buildWeatherReport(location, weatherData);

        assertNotNull(report);
        assertTrue(report.contains("Bogotá"));
        assertTrue(report.contains("18.5 °C"));
        assertTrue(report.contains("75%"));
        assertTrue(report.contains("REPORTE METEOROLÓGICO"));
        assertTrue(report.contains("Pronóstico 7 Días"));
    }

    @Test
    void geocode_withValidCity_returnsMockedLocation() {
        GeocodingResult mockResult = new GeocodingResult();
        GeocodingResult.GeoLocation geoLocation = new GeocodingResult.GeoLocation();
        geoLocation.setName("Medellín");
        geoLocation.setLatitude(6.2518);
        geoLocation.setLongitude(-75.5636);
        geoLocation.setCountry("Colombia");
        mockResult.setResults(List.of(geoLocation));

        when(restTemplateMock.getForObject(anyString(), eq(GeocodingResult.class)))
                .thenReturn(mockResult);

        GeocodingResult.GeoLocation result = weatherService.geocode("Medellín");

        assertNotNull(result);
        assertEquals("Medellín", result.getName());
        assertEquals(6.2518, result.getLatitude(), 0.001);
    }

    @Test
    void geocode_withUnknownCity_throwsException() {
        GeocodingResult emptyResult = new GeocodingResult();
        emptyResult.setResults(List.of());

        when(restTemplateMock.getForObject(anyString(), eq(GeocodingResult.class)))
                .thenReturn(emptyResult);

        assertThrows(IllegalArgumentException.class,
                () -> weatherService.geocode("XyzCiudadNoExiste123"));
    }

    @Test
    void getWeatherReport_withValidLocation_returnsReport() {
        // Mock geocoding
        GeocodingResult mockGeoResult = new GeocodingResult();
        GeocodingResult.GeoLocation geoLocation = new GeocodingResult.GeoLocation();
        geoLocation.setName("Cartagena");
        geoLocation.setLatitude(10.3910);
        geoLocation.setLongitude(-75.4794);
        geoLocation.setCountry("Colombia");
        mockGeoResult.setResults(List.of(geoLocation));

        // Mock weather data
        WeatherData mockWeather = new WeatherData();
        mockWeather.setTimezone("America/Bogota");
        WeatherData.CurrentWeather current = new WeatherData.CurrentWeather();
        current.setTemperature(32.0);
        current.setHumidity(80);
        current.setWeatherCode(0);
        current.setWindSpeed(8.0);
        mockWeather.setCurrent(current);

        WeatherData.DailyWeather daily = new WeatherData.DailyWeather();
        daily.setTime(List.of("2024-01-01"));
        daily.setTemperatureMax(List.of(34.0));
        daily.setTemperatureMin(List.of(28.0));
        daily.setPrecipitationSum(List.of(0.0));
        daily.setWeatherCode(List.of(0));
        mockWeather.setDaily(daily);

        when(restTemplateMock.getForObject(anyString(), eq(GeocodingResult.class)))
                .thenReturn(mockGeoResult);
        when(restTemplateMock.getForObject(anyString(), eq(WeatherData.class)))
                .thenReturn(mockWeather);

        String report = weatherService.getWeatherReport("Cartagena");

        assertNotNull(report);
        assertFalse(report.isBlank());
        assertTrue(report.contains("Cartagena"));
    }
}
