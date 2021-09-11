package com.weather.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.model.DarkSkyWrapper;
import com.weather.model.Geolocation.Result.Geometry.Location;
import com.weather.model.OpenWeatherWrapper;
import com.weather.repository.DarkSkyApiRepository;
import com.weather.repository.OpenWeatherApiRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

@Component
public class PublicServiceHelper {
    
    private static final Logger log = LoggerFactory.getLogger(PublicServiceHelper.class);
    
    @Value("${api.api-key.open-weather}")
    private String OPEN_WEATHER_APPID;

    @Value("${api.format.open-weather}")
    private String OPEN_WEATHER_FORMAT;

    @Value("${api.api-key.dark-sky}")
    private String DARK_SKY_APPID;

    @Value("${api.api-key.google-maps}")
    private String GEOLOCATION_APPID;
    
    private GoogleService googleService;
    
    private OpenWeatherApiRepository openApiRepo;
    
    private DarkSkyApiRepository darkSkyRepo;
    
    @Autowired
    public void setGoogleService(GoogleService googleService) {
        this.googleService = googleService;
    }

    @Autowired
    public void setOpenApiRepo(OpenWeatherApiRepository openApiRepo) {
        this.openApiRepo = openApiRepo;
    }

    @Autowired
    public void setDarkSkyRepo(DarkSkyApiRepository darkSkyRepo) {
        this.darkSkyRepo = darkSkyRepo;
    }
    
    public Map<String, String> openWeatherProviderProcess(String url, String cityName)
            throws JsonProcessingException, IOException {

        OpenWeatherWrapper wrapper = openApiRepo.getInformationAndMapToObject(url);
        Assert.notNull(wrapper, "OpenWeather repository returned with null!");

        String imgUrl = UriComponentsBuilder.newInstance()
                .scheme("http")
                .host("openweathermap.org")
                .path(String.format("/img/w/%s.png", wrapper.getWeatherList().get(0).getIcon()))
                .build().encode(StandardCharsets.UTF_8).toUriString();
                        
        String temp = wrapper.getMainInfromation().getTemp();
        String desc = wrapper.getWeatherList().get(0).getDescription();
        
        Map<String, String> attributeMap = new HashMap<>();
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
    
    public Map<String, String> darkSkyProviderProcess(String cityName)
            throws JsonProcessingException, IOException {
        Location location = googleService.getGeolocation(cityName);

        String url = UriComponentsBuilder.newInstance().scheme("https")
                .host("api.darksky.net").path("/forecast")
                .path("/" + DARK_SKY_APPID)
                .path(String.format("/%s,%s", location.getLatitude(), location.getLongitude()))
                .queryParam("units", "si")
                .build().encode(StandardCharsets.UTF_8).toUriString();

        DarkSkyWrapper wrapper = darkSkyRepo.getInformationAndMapToObject(url);
        Assert.notNull(wrapper, "DarkSky repository returned with null!");
        
        String temp = wrapper.getCurrentWeather().getTemperature();
        String desc = wrapper.getHourWeather().getSummary();
        
        Map<String, String> attributeMap = new HashMap<>();
        attributeMap.put("imgUrl", "");
        attributeMap.put("temp", temp);
        attributeMap.put("desc", desc);
        
        log.info(String.format("DarkSky API called and returned: temp=[%s], desc=[%s],"
                + " cityName=[%s]", temp, desc, cityName));
        
        return attributeMap;
    }

    public String getOpenWeatherUrl(String cityName) throws UnsupportedEncodingException {
        String url = UriComponentsBuilder.newInstance().scheme("http")
                .host("api.openweathermap.org").path("/data/2.5/weather").queryParam("q", cityName)
                .queryParam("units", "metric").queryParam("APPID", OPEN_WEATHER_APPID)
                .queryParam("mode", OPEN_WEATHER_FORMAT).build().encode(StandardCharsets.UTF_8).toUriString();

        return url;
    }
}
