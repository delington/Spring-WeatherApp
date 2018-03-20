package com.weather.model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class OpenWeatherWrapper {
    
    @JsonProperty("weather")
    private List<Weather> weatherList = new ArrayList<>();
    
    @JsonProperty("main")
    private MainInformation mainInfromation;
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Weather {
        
        private String description;
        
        private String icon;

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        @Override
        public String toString() {
            return "Weather [description=" + description + ", icon=" + icon + "]";
        }
    }
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class MainInformation {
        
        private String temp;

        public String getTemp() {
            return temp;
        }

        public void setTemp(String temp) {
            this.temp = temp;
        }

        @Override
        public String toString() {
            return "MainInformation [temp=" + temp + "]";
        }
    }

    public List<Weather> getWeatherList() {
        return weatherList;
    }

    public void setWeatherList(List<Weather> weatherList) {
        this.weatherList = weatherList;
    }

    public MainInformation getMainInfromation() {
        return mainInfromation;
    }

    public void setMainInfromation(MainInformation mainInfromation) {
        this.mainInfromation = mainInfromation;
    }

    @Override
    public String toString() {
        return "OpenWeatherWrapper [weatherList=" + weatherList + ", mainInfromation="
                + mainInfromation + "]";
    }

}
