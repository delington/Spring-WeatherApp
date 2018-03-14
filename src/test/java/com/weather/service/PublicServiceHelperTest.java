package com.weather.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.model.Geolocation;
import com.weather.model.Geolocation.Result;
import com.weather.model.Geolocation.Result.Geometry;
import com.weather.model.Geolocation.Result.Geometry.Location;
import com.weather.repository.GoogleApiRepository;

@RunWith(MockitoJUnitRunner.class)
public class PublicServiceHelperTest {
    
    @Mock
    GoogleApiRepository googleRepo;
    
    @InjectMocks
    PublicServiceHelper serviceHelper;
    
    @Before
    public void setup() {
        ReflectionTestUtils.setField(serviceHelper, "OPEN_WEATHER_APPID", "3d33157b92c33f9c5afa4c9a37b13715");
        ReflectionTestUtils.setField(serviceHelper, "OPEN_WEATHER_FORMAT", "json");
        ReflectionTestUtils.setField(serviceHelper, "GEOLOCATION_APPID", "AIzaSyC3uvCOYFAgPG3e33QFozetMUh69oRBxrg");
    }
    
    @Test
    public void checkGetOpenWeatherUrl() throws UnsupportedEncodingException {
        //GIVEN 
        String city = "Szeged";
        //WHEN
        String result = serviceHelper.getOpenWeatherUrl(city);
        //THEN
        assertThat(result, containsString(
            "http://api.openweathermap.org/data/2.5/weather?q=Szeged&units=metric"
            + "&APPID=3d33157b92c33f9c5afa4c9a37b13715&mode=json"));
    }
    
    @Test
    public void checkGetGeolocation() throws JsonProcessingException, IOException {
        //GIVEN
        Geolocation geo = new Geolocation();
        Result result = new Result();
        Geometry geometry = new Geometry();
        Location location = new Location();
        
        ArrayList<Result> resultList = new ArrayList<>();
        
        location.setLatitude("46.2530102");
        location.setLongitude("20.1414254");
        geometry.setLocation(location);
        result.setGeometry(geometry);
        resultList.add(result);
        geo.setResults(resultList);
        
        Mockito.when(googleRepo.getInformationAndMapToObject(BDDMockito.anyString())).thenReturn(geo);
        //WHEN
        Location locationRes = serviceHelper.getGeolocation("Szeged");
        //THEN
        assertThat(locationRes.getLatitude(), containsString("46.2530102"));
        assertThat(locationRes.getLongitude(), containsString("20.1414254"));
    }
    
}
