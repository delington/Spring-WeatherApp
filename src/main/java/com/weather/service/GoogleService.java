package com.weather.service;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.model.Geolocation;
import com.weather.model.Geolocation.Result.Geometry.Location;
import com.weather.repository.GoogleApiRepository;

@Service
public class GoogleService {
    
    private static final Logger log = LoggerFactory.getLogger(GoogleService.class);
    
    @Value("${api.api-key.google-maps}")
    private String GEOLOCATION_APPID;
    
    private GoogleApiRepository googleApiRepo;

    @Autowired
    public void setGoogleRepo(GoogleApiRepository googleRepo) {
        this.googleApiRepo = googleRepo;
    }
    
    public Location getGeolocation(String cityName) throws JsonProcessingException, IOException {

        String url = UriComponentsBuilder.newInstance().scheme("https").host("maps.googleapis.com")
                .path("/maps/api/geocode/json").queryParam("address", cityName)
                .queryParam("key", GEOLOCATION_APPID).build().encode("UTF-8").toUriString();

        Geolocation geolocation = googleApiRepo.getInformationAndMapToObject(url);

        log.info(String.format("Geolocation asked from Google Map API. Geolocation=[%s].",
                geolocation));
        
        if (geolocation == null) {
            throw new NullPointerException("Geolocation returned with null!");
        }
        
        return geolocation.getResults().get(0).getGeometry().getLocation();
    }
}
