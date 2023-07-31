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
    public static final String TEXT_CITY_NAME = "cityName";
    public static final String TEXT_DESC = "desc";
    public static final String TEXT_IMG_URL = "imgUrl";
    public static final String TEXT_TEMP = "temp";
    public static final String EXTENSION_PNG = ".png";

    private final NominatimService nominatimService;
    private SevenTimerService sevenTimerService;

    public Map<String, String> process(String cityName)
            throws IOException {

        Map<String, String> attrMap = new HashMap<>();

        final var geoLocation = nominatimService.getGeolocation(cityName);
        final var weatherData = sevenTimerService.getWeatherData(geoLocation.getLat(), geoLocation.getLon());
        log.info("Weather data: " + weatherData);

        attrMap.put(TEXT_CITY_NAME, cityName);
        attrMap.put(TEXT_DESC, weatherData.getWeather());
        attrMap.put(TEXT_IMG_URL, SEVEN_TIMER_IMG_PREFIX_URL + weatherData.getWeather() + EXTENSION_PNG);

        final var avgTemp = (weatherData.getTemperature().getMax() + weatherData.getTemperature().getMin()) / 2;
        attrMap.put(TEXT_TEMP, String.valueOf(avgTemp));

        return attrMap;
    }
    
}
