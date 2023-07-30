package com.weather.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@AllArgsConstructor
public class PublicService {

    public static final String SEVEN_TIMER_IMG_PREFIX_URL = "http://www.7timer.info/img/misc/about_civil_";
    private final NominatimService nominatimService;
    private SevenTimerService sevenTimerService;

    public Map<String, String> process(String cityName)
            throws IOException {

        Map<String, String> attrMap = new HashMap<>();

        final var geoLocation = nominatimService.getGeolocation(cityName);
        final var weatherData = sevenTimerService.getWeatherData(geoLocation.getLat(), geoLocation.getLon());
        log.info("Weather data: " + weatherData);

        attrMap.put("cityName", cityName);
        attrMap.put("desc", weatherData.getWeather());
        attrMap.put("imgUrl", SEVEN_TIMER_IMG_PREFIX_URL + weatherData.getWeather() + ".png");

        final var avgTemp = (weatherData.getTemperature().getMax() + weatherData.getTemperature().getMin()) / 2;
        attrMap.put("temp", String.valueOf(avgTemp));

        return attrMap;
    }
    
}
