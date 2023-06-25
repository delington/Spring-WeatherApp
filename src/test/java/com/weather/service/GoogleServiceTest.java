package com.weather.service;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather.model.Geolocation;
import com.weather.model.Geolocation.Result;
import com.weather.model.Geolocation.Result.Geometry;
import com.weather.model.Geolocation.Result.Geometry.Location;
import com.weather.repository.GoogleApiRepository;

@RunWith(MockitoJUnitRunner.class)
public class GoogleServiceTest {
    
    @InjectMocks
    private GoogleService underTest;

    @Mock
    private GoogleApiRepository googleRepo;
    
    @Test
    public void checkGetGeolocation() throws JsonProcessingException, IOException {
        //GIVEN
        String city = "Szeged";
        Mockito.when(googleRepo.getData(Mockito.contains(city)))
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
              
            Mockito.verify(googleRepo, Mockito.times(1)).getData(
                    Mockito.contains(city));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowIllegalArgumentExceptionIfCantGetGeolocation() throws JsonProcessingException, IOException {
        //GIVEN
        String city = "Szeged";
        
        Mockito.when(googleRepo.getData(Mockito.anyString()))
            .thenReturn(null);
        //WHEN
        underTest.getGeolocation(city);
    }
    
}
