package com.weather.repository;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import com.weather.model.OpenWeatherWrapper;

@Slf4j
@Repository
public class OpenWeatherApiRepository implements ApiRepository<OpenWeatherWrapper> {
    
    @Override
    public OpenWeatherWrapper getData(String url) {
        log.info("OpenWeatherApiRepository.getInformation invoked. Calling openWeather api...");
        return new RestTemplate().getForObject(url, OpenWeatherWrapper.class);
    }
}
