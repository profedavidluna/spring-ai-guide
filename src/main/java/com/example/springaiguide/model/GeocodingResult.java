package com.example.springaiguide.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Modelos de datos para la API de geocodificación de Open-Meteo.
 * Open-Meteo es completamente gratuita y no requiere API key.
 * Documentación: https://open-meteo.com/en/docs/geocoding-api
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class GeocodingResult {

    private List<GeoLocation> results;

    public List<GeoLocation> getResults() {
        return results;
    }

    public void setResults(List<GeoLocation> results) {
        this.results = results;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class GeoLocation {

        private String name;
        private double latitude;
        private double longitude;
        private String country;
        private String timezone;

        @JsonProperty("admin1")
        private String region;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

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

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getTimezone() {
            return timezone;
        }

        public void setTimezone(String timezone) {
            this.timezone = timezone;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        @Override
        public String toString() {
            return name + (region != null ? ", " + region : "") + ", " + country;
        }
    }
}
