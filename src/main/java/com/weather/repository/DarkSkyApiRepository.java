package com.weather.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.weather.model.DarkSkyWrapper;

@Repository
public class DarkSkyApiRepository implements ApiRepository<DarkSkyWrapper> {
    
    private static final Logger log = LoggerFactory.getLogger(DarkSkyApiRepository.class);
    
    @Override
    public DarkSkyWrapper getInformationAndMapToObject(String url) {
        log.info("DarkSkyApiRepository.getInformation invoked. Calling darkSky api...");
        return new RestTemplate().getForObject(url, DarkSkyWrapper.class);
    }
}
