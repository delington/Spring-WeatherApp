package com.weather.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import com.weather.model.Geolocation.Result.Geometry.Location;
import com.weather.repository.GoogleApiRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PublicServiceTest {
    
    PublicService service;
    
    @Before
    public void setup() {
        service = new PublicService();
        ReflectionTestUtils.setField(service, "OPEN_WEATHER_APPID", "3d33157b92c33f9c5afa4c9a37b13715");
        ReflectionTestUtils.setField(service, "OPEN_WEATHER_FORMAT", "json");
        ReflectionTestUtils.setField(service, "GEOLOCATION_APPID", "AIzaSyC3uvCOYFAgPG3e33QFozetMUh69oRBxrg");
        ReflectionTestUtils.setField(service, "googleApiRepo", new GoogleApiRepository());
    }
    
    @Test
    public void checkGetOpenWeatherUrl() {
        String result = ReflectionTestUtils.invokeMethod(service, "getOpenWeatherUrl", "Szeged");
        
        assertThat(result, containsString(
            "http://api.openweathermap.org/data/2.5/weather?q=Szeged&units=metric"
            + "&APPID=3d33157b92c33f9c5afa4c9a37b13715&mode=json"));
    }
    
    @Test
    public void checkGetGeolocation() {
        Location location = ReflectionTestUtils.invokeMethod(service, "getGeolocation", "Szeged");
        
        assertThat(location.getLatitude(), containsString("46.253"));
        assertThat(location.getLongitude(), containsString("20.1414"));
    }
    
}
