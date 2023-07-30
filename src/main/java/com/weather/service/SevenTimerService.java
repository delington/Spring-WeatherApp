package com.weather.service;

import com.weather.client.SevenTimerClient;
import com.weather.model.SevenTimerResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class SevenTimerService {

    private SevenTimerClient sevenTimerClient;

    public SevenTimerResponse.WeatherData getWeatherData(float lat, float lon) throws IOException {
        var response = sevenTimerClient.getData(lat, lon).execute().body();
        log.info("Seventimer client called, response: " + response);

        assert response != null;
        return response.getDataseries().get(0);
    }
}
