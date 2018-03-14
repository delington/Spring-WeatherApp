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
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.model.DarkSkyWrapper;
import com.weather.model.Geolocation;
import com.weather.model.Geolocation.Result;
import com.weather.model.Geolocation.Result.Geometry;
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
    GoogleApiRepository googleRepo;
    
    @Mock
    DarkSkyApiRepository darkSkyRepo;
    
    @Mock
    OpenWeatherApiRepository weatherRepo;
    
    @InjectMocks
    PublicServiceHelper underTest;
    
    @Before
    public void setup() {
        ReflectionTestUtils.setField(underTest, "OPEN_WEATHER_APPID", "3d33157b92c33f9c5afa4c9a37b13715");
        ReflectionTestUtils.setField(underTest, "OPEN_WEATHER_FORMAT", "json");
        ReflectionTestUtils.setField(underTest, "GEOLOCATION_APPID", "AIzaSyC3uvCOYFAgPG3e33QFozetMUh69oRBxrg");
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
    public void checkGetGeolocation() throws JsonProcessingException, IOException {
        //GIVEN
        String city = "Szeged";
        Mockito.when(googleRepo.getInformationAndMapToObject(BDDMockito.contains(city)))
            .then(new Answer<Geolocation>() {
            
            @Override
            public Geolocation answer(InvocationOnMock invoke) {
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
                
                return geo;
            }
        });
        //WHEN
        Location locationRes = underTest.getGeolocation(city);
        //THEN
        assertThat(locationRes.getLatitude(), containsString("46.2530102"));
        assertThat(locationRes.getLongitude(), containsString("20.1414254"));
        
        Mockito.verify(googleRepo, Mockito.times(1)).getInformationAndMapToObject(
                BDDMockito.contains(city));
    }

    @Test
    public void checkOpenWeatherProviderProcess() throws JsonProcessingException, IOException {
        //GIVEN
        String city = "Debrecen";
        
        Mockito.when(weatherRepo.getInformationAndMapToObject(BDDMockito.contains(city)))
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
        
        Mockito.verify(weatherRepo, Mockito.times(1)).getInformationAndMapToObject(
                BDDMockito.contains(city));
    }
    
    @Test
    public void checkDarkSkyProviderProcess() throws JsonProcessingException, IOException {
        //GIVEN
        String city = "Budapest";
        
        Mockito.when(darkSkyRepo.getInformationAndMapToObject(BDDMockito.contains(city)))
            .then(new Answer<DarkSkyWrapper>() {
            
            @Override
            public DarkSkyWrapper answer(InvocationOnMock invoke) {
                DarkSkyWrapper wrapper = new DarkSkyWrapper();
                
                CurrentWeather currentWeather = new CurrentWeather();
                HourlyWeather hourlyWeather = new HourlyWeather();
                
                currentWeather.setTemperature("20");
                hourlyWeather.setSummary("Sunny with some cloudy breaks.");
                
                return wrapper;
            }
        });
        
        Mockito.when(underTest.getGeolocation(city)).then(new Answer<Location>() {

            @Override
            public Location answer(InvocationOnMock invoke) {
                Location stubLocation = new Location();
                stubLocation.setLatitude("47.4979");
                stubLocation.setLongitude("19.0402");
                return stubLocation;
            }
        });
        
        Mockito.when(googleRepo.getInformationAndMapToObject(BDDMockito.anyString()))
            .then(new Answer<Geolocation>() {
            
            @Override
            public Geolocation answer(InvocationOnMock invoke) {
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
                
                return geo;
            }
        });
        //WHEN
        Map<String, String> map = underTest.darkSkyProviderProcess(city);
        //THEN
        assertThat(map.get("desc") , containsString("Windy with rain"));
        assertThat(map.get("temp") , containsString("14"));
        assertThat(map.get("imgUrl") , is(""));
        
        Mockito.verify(darkSkyRepo, Mockito.times(1)).getInformationAndMapToObject(
                BDDMockito.contains("Budapest"));
    }
    
}
