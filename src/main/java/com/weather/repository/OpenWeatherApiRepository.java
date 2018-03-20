package com.weather.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.weather.model.OpenWeatherWrapper;

@Repository
public class OpenWeatherApiRepository implements ApiRepository<OpenWeatherWrapper> {
    
    private static final Logger log = LoggerFactory.getLogger(OpenWeatherApiRepository.class);

    @Override
    public OpenWeatherWrapper getInformationAndMapToObject(String url) {
        log.info("OpenWeatherApiRepository.getInformation invoked. Calling openWeather api...");
        return new RestTemplate().getForObject(url, OpenWeatherWrapper.class);
    }
}
