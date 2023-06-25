package com.weather.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;

@Slf4j
@Service
public class PublicService {

    @Value("${api.format.open-weather}")
    private String OPEN_WEATHER_FORMAT;

    @Value("${api.api-key.google-maps}")
    private String GEOLOCATION_APPID;

    private PublicServiceHelper serviceHelper;

    public Map<String, String> process(String cityName, String apiProvider)
            throws JsonProcessingException, IOException {

        Map<String, String> attrMap = new HashMap<>();
        String url = serviceHelper.getOpenWeatherUrl(cityName);

        if (apiProvider.equals("openWeather") && OPEN_WEATHER_FORMAT.equalsIgnoreCase("json")) {
            
            log.info("OpenWeather provider called with JSON");
            attrMap = serviceHelper.openWeatherProviderProcess(url, cityName);
        } else if (apiProvider.equals("openWeather") && OPEN_WEATHER_FORMAT.equalsIgnoreCase("xml")) {
            
            log.info("OpenWeather provider called with XML");
            attrMap = serviceHelper.openWeatherProviderProcessWithXml(url, cityName);
        } else if (apiProvider.equals("darkSky")) {
            
            log.info("DarkSky provider called");
            attrMap = serviceHelper.darkSkyProviderProcess(cityName);
        }
        
        return attrMap;
    }
    
}
