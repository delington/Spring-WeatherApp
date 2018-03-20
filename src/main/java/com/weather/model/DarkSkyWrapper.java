package com.weather.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class DarkSkyWrapper {
    
    @JsonProperty("currently")
    private CurrentWeather currentWeather;
    
    @JsonProperty("hourly")
    private HourlyWeather hourWeather;
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class CurrentWeather {
        private String temperature;

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        @Override
        public String toString() {
            return "CurrentWeather [temperature=" + temperature + "]";
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class HourlyWeather {
        private String summary;

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        @Override
        public String toString() {
            return "HourlyWeather [summary=" + summary + "]";
        }
    }

    public CurrentWeather getCurrentWeather() {
        return currentWeather;
    }

    public void setCurrentWeather(CurrentWeather currentWeather) {
        this.currentWeather = currentWeather;
    }

    public HourlyWeather getHourWeather() {
        return hourWeather;
    }

    public void setHourWeather(HourlyWeather hourWeather) {
        this.hourWeather = hourWeather;
    }

    @Override
    public String toString() {
        return "DarkSkyWrapper [currentWeather=" + currentWeather + ", hourWeather=" + hourWeather
                + "]";
    }

}
