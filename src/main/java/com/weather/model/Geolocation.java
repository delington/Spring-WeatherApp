package com.weather.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Geolocation {
    
    private List<Result> results;
    
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Result {
        
        private Geometry geometry;
        
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Geometry {
            
            private Location location;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Location {
                
                @JsonProperty("lat")
                private String latitude;
                
                @JsonProperty("lng")
                private String longitude;

                public String getLatitude() {
                    return latitude;
                }

                public void setLatitude(String latitude) {
                    this.latitude = latitude;
                }

                public String getLongitude() {
                    return longitude;
                }

                public void setLongitude(String longitude) {
                    this.longitude = longitude;
                }

                @Override
                public String toString() {
                    return "Location [latitude=" + latitude + ", longitude=" + longitude + "]";
                }
            }
            
            public Location getLocation() {
                return location;
            }

            public void setLocation(Location location) {
                this.location = location;
            }

            @Override
            public String toString() {
                return "Geometry [location=" + location + "]";
            }
        }

        public Geometry getGeometry() {
            return geometry;
        }

        public void setGeometry(Geometry geometry) {
            this.geometry = geometry;
        }

        @Override
        public String toString() {
            return "Result [geometry=" + geometry + "]";
        }
    }

    public List<Result> getResults() {
        return results;
    }

    public void setResults(List<Result> results) {
        this.results = results;
    }

    @Override
    public String toString() {
        return "Geolocation [results=" + results + "]";
    }
    
}
