package com.example.springaiguide.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Modelos de datos para la respuesta de la API meteorológica de Open-Meteo.
 * Documentación: https://open-meteo.com/en/docs
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WeatherData {

    private double latitude;
    private double longitude;
    private String timezone;

    @JsonProperty("current")
    private CurrentWeather current;

    @JsonProperty("daily")
    private DailyWeather daily;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public CurrentWeather getCurrent() {
        return current;
    }

    public void setCurrent(CurrentWeather current) {
        this.current = current;
    }

    public DailyWeather getDaily() {
        return daily;
    }

    public void setDaily(DailyWeather daily) {
        this.daily = daily;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentWeather {

        @JsonProperty("temperature_2m")
        private double temperature;

        @JsonProperty("relative_humidity_2m")
        private int humidity;

        @JsonProperty("weather_code")
        private int weatherCode;

        @JsonProperty("wind_speed_10m")
        private double windSpeed;

        public double getTemperature() {
            return temperature;
        }

        public void setTemperature(double temperature) {
            this.temperature = temperature;
        }

        public int getHumidity() {
            return humidity;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }

        public int getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(int weatherCode) {
            this.weatherCode = weatherCode;
        }

        public double getWindSpeed() {
            return windSpeed;
        }

        public void setWindSpeed(double windSpeed) {
            this.windSpeed = windSpeed;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class DailyWeather {

        private List<String> time;

        @JsonProperty("temperature_2m_max")
        private List<Double> temperatureMax;

        @JsonProperty("temperature_2m_min")
        private List<Double> temperatureMin;

        @JsonProperty("precipitation_sum")
        private List<Double> precipitationSum;

        @JsonProperty("weather_code")
        private List<Integer> weatherCode;

        public List<String> getTime() {
            return time;
        }

        public void setTime(List<String> time) {
            this.time = time;
        }

        public List<Double> getTemperatureMax() {
            return temperatureMax;
        }

        public void setTemperatureMax(List<Double> temperatureMax) {
            this.temperatureMax = temperatureMax;
        }

        public List<Double> getTemperatureMin() {
            return temperatureMin;
        }

        public void setTemperatureMin(List<Double> temperatureMin) {
            this.temperatureMin = temperatureMin;
        }

        public List<Double> getPrecipitationSum() {
            return precipitationSum;
        }

        public void setPrecipitationSum(List<Double> precipitationSum) {
            this.precipitationSum = precipitationSum;
        }

        public List<Integer> getWeatherCode() {
            return weatherCode;
        }

        public void setWeatherCode(List<Integer> weatherCode) {
            this.weatherCode = weatherCode;
        }
    }
}
