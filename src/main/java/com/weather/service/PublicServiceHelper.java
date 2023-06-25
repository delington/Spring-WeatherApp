package com.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.model.DarkSkyWrapper;
import com.weather.model.Geolocation.Result.Geometry.Location;
import com.weather.model.OpenWeatherWrapper;
import com.weather.repository.DarkSkyApiRepository;
import com.weather.repository.OpenWeatherApiRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class PublicServiceHelper {
    
    @Value("${api.api-key.open-weather}")
    private String OPEN_WEATHER_APPID;

    @Value("${api.format.open-weather}")
    private String OPEN_WEATHER_FORMAT;

    @Value("${api.api-key.dark-sky}")
    private String DARK_SKY_APPID;

    @Value("${api.api-key.google-maps}")
    private String GEOLOCATION_APPID;
    
    private final GoogleService googleService;
    
    private final OpenWeatherApiRepository openApiRepo;
    
    private final DarkSkyApiRepository darkSkyRepo;
    
    public PublicServiceHelper(GoogleService googleService, OpenWeatherApiRepository openApiRepo, DarkSkyApiRepository darkSkyRepo) {
        this.googleService = googleService;
        this.openApiRepo = openApiRepo;
        this.darkSkyRepo = darkSkyRepo;
    }

    public Map<String, String> openWeatherProviderProcess(String url, String cityName)
            throws JsonProcessingException, IOException {

        final OpenWeatherWrapper wrapper = openApiRepo.getData(url);
        Assert.notNull(wrapper, "OpenWeather repository returned with null!");

        final String imgUrl = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("openweathermap.org")
                .path(String.format("/img/w/%s.png", wrapper.getWeatherList().get(0).getIcon()))
                .build().encode(StandardCharsets.UTF_8).toUriString();
                        
        final String temp = wrapper.getMainInfromation().getTemp();
        final String desc = wrapper.getWeatherList().get(0).getDescription();
        
        final Map<String, String> attributeMap = new HashMap<>();
        attributeMap.put("imgUrl", imgUrl);
        attributeMap.put("temp", temp);
        attributeMap.put("desc", desc);
        
        log.info(String.format("OpenWeatherProvider called: temp=[%s], desc=[%s],"
                + " cityName=[%s]", temp, desc, cityName));
        
        return attributeMap;
    }
    
    //TODO finish
    public Map<String, String> openWeatherProviderProcessWithXml(String url, String cityName)
            throws JsonProcessingException, IOException {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        return null;
    }
    
    public Map<String, String> darkSkyProviderProcess(String cityName) throws JsonProcessingException, IOException {
        final Location location = googleService.getGeolocation(cityName);

        final String url = UriComponentsBuilder.newInstance().scheme("https")
                .host("api.darksky.net").path("/forecast")
                .path("/" + DARK_SKY_APPID)
                .path(String.format("/%s,%s", location.getLatitude(), location.getLongitude()))
                .queryParam("units", "si")
                .build().encode(StandardCharsets.UTF_8).toUriString();

        final DarkSkyWrapper wrapper = darkSkyRepo.getData(url);
        Assert.notNull(wrapper, "DarkSky repository returned with null!");
        
        final String temp = wrapper.getCurrentWeather().getTemperature();
        final String desc = wrapper.getHourWeather().getSummary();
        
        final Map<String, String> attributeMap = new HashMap<>();
        attributeMap.put("imgUrl", "");
        attributeMap.put("temp", temp);
        attributeMap.put("desc", desc);
        
        log.info(String.format("DarkSky API called and returned: temp=[%s], desc=[%s],"
                + " cityName=[%s]", temp, desc, cityName));
        
        return attributeMap;
    }

    public String getOpenWeatherUrl(final String cityName) {
        return UriComponentsBuilder.newInstance().scheme("http")
                .host("api.openweathermap.org").path("/data/2.5/weather").queryParam("q", cityName)
                .queryParam("units", "metric").queryParam("APPID", OPEN_WEATHER_APPID)
                .queryParam("mode", OPEN_WEATHER_FORMAT).build().encode(StandardCharsets.UTF_8).toUriString();
    }
}
