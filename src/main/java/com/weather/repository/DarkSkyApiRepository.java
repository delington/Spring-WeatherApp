package com.weather.repository;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.weather.model.DarkSkyWrapper;

@Slf4j
@Repository
public class DarkSkyApiRepository implements ApiRepository<DarkSkyWrapper> {
    
    @Override
    public DarkSkyWrapper getData(String url) {
        log.info("DarkSkyApiRepository.getInformation invoked. Calling darkSky api...");
        return new RestTemplate().getForObject(url, DarkSkyWrapper.class);
    }
}
