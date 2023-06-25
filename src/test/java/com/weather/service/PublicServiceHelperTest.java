package com.weather.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;
import static org.mockito.AdditionalMatchers.and;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.model.DarkSkyWrapper;
import com.weather.model.Geolocation.Result.Geometry.Location;
import com.weather.model.OpenWeatherWrapper;
import com.weather.model.DarkSkyWrapper.CurrentWeather;
import com.weather.model.DarkSkyWrapper.HourlyWeather;
import com.weather.model.OpenWeatherWrapper.MainInformation;
import com.weather.model.OpenWeatherWrapper.Weather;
import com.weather.repository.DarkSkyApiRepository;
import com.weather.repository.GoogleApiRepository;
import com.weather.repository.OpenWeatherApiRepository;

@RunWith(MockitoJUnitRunner.class)
public class PublicServiceHelperTest {
    
    @Mock
    private GoogleService googleService;
    
    @Mock
    private GoogleApiRepository googleRepo;
    
    @Mock
    private DarkSkyApiRepository darkSkyRepo;
    
    @Mock
    private OpenWeatherApiRepository weatherRepo;
    
    @InjectMocks
    private PublicServiceHelper underTest;
    
    @Before
    public void setup() {
        ReflectionTestUtils.setField(
                underTest, "OPEN_WEATHER_APPID", "3d33157b92c33f9c5afa4c9a37b13715");
        ReflectionTestUtils.setField(underTest, "OPEN_WEATHER_FORMAT", "json");
        ReflectionTestUtils.setField(
                underTest, "GEOLOCATION_APPID", "AIzaSyC3uvCOYFAgPG3e33QFozetMUh69oRBxrg");
        ReflectionTestUtils.setField(
                googleService, "GEOLOCATION_APPID", "AIzaSyC3uvCOYFAgPG3e33QFozetMUh69oRBxrg");
    }
    
    @Test
    public void checkGetOpenWeatherUrl() throws UnsupportedEncodingException {
        //GIVEN 
        String city = "Szeged";
        //WHEN
        String result = underTest.getOpenWeatherUrl(city);
        //THEN
        assertThat(result, containsString(
            "http://api.openweathermap.org/data/2.5/weather?q=Szeged&units=metric"
            + "&APPID=3d33157b92c33f9c5afa4c9a37b13715&mode=json"));
    }
    
    @Test
    public void checkOpenWeatherProviderProcess() throws JsonProcessingException, IOException {
        //GIVEN
        String city = "Debrecen";
        
        Mockito.when(weatherRepo.getData(Mockito.contains(city)))
            .then(new Answer<OpenWeatherWrapper>() {
            
            @Override
            public OpenWeatherWrapper answer(InvocationOnMock invoke) {
                OpenWeatherWrapper wrapper = new OpenWeatherWrapper();
                List<Weather> weatherList = new ArrayList<>();
                Weather weather = new Weather();
                MainInformation info = new MainInformation();
                
                weather.setDescription("Windy with rain.");
                weatherList.add(weather);
                wrapper.setWeatherList(weatherList);
                
                info.setTemp("14");
                wrapper.setMainInfromation(info);
                
                return wrapper;
            }
        });
        //WHEN
        Map<String, String> map = underTest.openWeatherProviderProcess(city, city);
        //THEN
        assertThat(map.get("desc") , containsString("Windy with rain"));
        assertThat(map.get("temp") , containsString("14"));
        
        Mockito.verify(weatherRepo, Mockito.times(1)).getData(
                Mockito.contains(city));
    }
    
    @Test
    public void checkDarkSkyProviderProcess() throws JsonProcessingException, IOException {
        //GIVEN
        String city = "Budapest";
        
        Mockito.when(darkSkyRepo.getData(Mockito.anyString()))
            .then(new Answer<DarkSkyWrapper>() {
            
            @Override
            public DarkSkyWrapper answer(InvocationOnMock invoke) {
                DarkSkyWrapper wrapper = new DarkSkyWrapper();
                
                CurrentWeather currentWeather = new CurrentWeather();
                HourlyWeather hourlyWeather = new HourlyWeather();
                
                currentWeather.setTemperature("20");
                hourlyWeather.setSummary("Sunny with some cloudy breaks.");
                
                wrapper.setCurrentWeather(currentWeather);
                wrapper.setHourWeather(hourlyWeather);
                
                return wrapper;
            }
        });
        
        createStubLocation(city, "47.4979", "19.0402");
        
        //WHEN
        Map<String, String> map = underTest.darkSkyProviderProcess(city);
        //THEN
        assertThat(map.get("desc") , containsString("Sunny with some cloudy breaks."));
        assertThat(map.get("temp") , containsString("20"));
        assertThat(map.get("imgUrl") , is(""));
        
        Mockito.verify(darkSkyRepo, Mockito.times(1)).getData
            (and(Mockito.contains("47.4979"), Mockito.contains("47.4979")));
    }

    private void createStubLocation(String city, String lat, String lng) 
            throws JsonProcessingException, IOException {
        Mockito.when(googleService.getGeolocation(city)).then(new Answer<Location>() {
            @Override
            public Location answer(InvocationOnMock invoke) {
                Location stubLocation = new Location();
                stubLocation.setLatitude(lat);
                stubLocation.setLongitude(lng);
                return stubLocation;
            }
        });
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void openWeatherProviderProcessShouldThrowIllegalArgumentException() 
            throws JsonProcessingException, IOException {
        //GIVEN
        String city = "Debrecen";
        
        Mockito.when(
                weatherRepo.getData(Mockito.anyString())).thenReturn(null);
        //WHEN
        underTest.openWeatherProviderProcess("url", city);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void darkSkyProcessShouldThrowIllegalArgumentException() 
            throws JsonProcessingException, IOException {
        //GIVEN
        String city = "New York";
        
        Mockito.when(
                darkSkyRepo.getData(Mockito.anyString())).thenReturn(null);
        
        createStubLocation(city, "43.00120", "19.4332");
        //WHEN
        underTest.darkSkyProviderProcess(city);
    }
    
}
