package com.weather.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Geolocation {
    
    private List<Result> results;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        
        private Geometry geometry;

        @Data
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Geometry {
            
            private Location location;

            @Data
            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Location {
                
                @JsonProperty("lat")
                private String latitude;
                
                @JsonProperty("lng")
                private String longitude;
            }
        }
    }

    @Override
    public String toString() {
        return "Geolocation [results=" + results + "]";
    }
}
