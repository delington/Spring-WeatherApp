package com.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class SevenTimerResponse {
    private String product;
    private List<WeatherData> dataseries;

    @Data
    public static class WeatherData {

        private LocalDate date;

        private String weather;

        @JsonProperty("temp2m")
        private Temperature temperature;

        @Data
        public static class Temperature {
            private int max;
            private int min;
        }
    }
}
