package com.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.exception.NoDataException;
import com.weather.model.Geolocation;
import com.weather.model.Geolocation.Result.Geometry.Location;
import com.weather.repository.GoogleApiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Validated
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
                .queryParam("key", GEOLOCATION_APPID).build().encode(StandardCharsets.UTF_8).toUriString();

        Geolocation geolocation = googleApiRepo.getInformationAndMapToObject(url);

        log.info(String.format("Geolocation asked from Google Map API. Geolocation=[%s].",
                geolocation));
        
        Assert.notNull(geolocation, "Google repository returned with null!");

        checkGeolocation(geolocation);
        return geolocation.getResults().get(0).getGeometry().getLocation();
    }

    private void checkGeolocation(Geolocation geolocation) {
        if (geolocation == null) {
            throw new NoDataException("Geolocation call returned with null!");
        }
        var geolocationResultList = geolocation.getResults();
        if (CollectionUtils.isEmpty(geolocationResultList)) {
            throw new NoDataException("Geolocation result list was empty!");
        }
    }
}
