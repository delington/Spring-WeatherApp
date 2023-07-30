package com.weather.service;

import com.weather.client.NominatimClient;
import com.weather.exception.NoDataException;
import com.weather.model.Place;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class NominatimService {
    
    private NominatimClient nominatimClient;

    public Place getGeolocation(String cityName) throws IOException {

        final List<Place> places = nominatimClient.listPlaces(cityName).execute().body();

        if (places == null || places.isEmpty()) {
            throw new NoDataException("Returned list is null or empty!");
        }

        final Place firstPlace = places.get(0);
        log.info("NominatimClient called, place: " + firstPlace);

        return firstPlace;
    }
}
