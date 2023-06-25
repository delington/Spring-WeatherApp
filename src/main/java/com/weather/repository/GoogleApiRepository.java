package com.weather.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.weather.model.Geolocation;

@Repository
public class GoogleApiRepository implements ApiRepository<Geolocation> {
    
    private static final Logger log = LoggerFactory.getLogger(GoogleApiRepository.class);

    @Override
    public Geolocation getData(String url) {
        log.info("GoogleApiRepository.getGeolocation invoked. Calling google api...");
        return new RestTemplate().getForObject(url, Geolocation.class);
    }

}
