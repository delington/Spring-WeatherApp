package com.weather.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class PublicControllerTest {
    
    private MockMvc mockMvc;
    
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Ignore
    @Test
    public void checkProcessUrlWithWeatherProvider() throws Exception {
        
        String URI = "/process";
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URI)
                .param("name", "Szeged")
                .param("provider", "openWeather");
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
    }
    
    @Ignore
    @Test
    public void checkProcessUrlWithDarkSkyProvider() throws Exception {
        
        String URI = "/process";
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URI)
                .param("name", "Szeged")
                .param("provider", "darkSky");
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertThat(response.getContentAsString(), containsString("Weather in"));
    }
    
    @Ignore
    @Test
    public void checkProcessUrlNoSuchCity() throws Exception {
        
        String URI = "/process";
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URI)
                .param("name", "s")
                .param("provider", "openWeather");
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertThat(response.getContentAsString(), containsString("404 Not Found"));
    }
    
    @Ignore
    @Test
    public void checkOpenWeatherModelAttribute() throws Exception {
        String URI = "/process";
        String city = "London";
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URI)
                .param("name", city)
                .param("provider", "openWeather");
        
        mockMvc.perform(requestBuilder)
            .andExpect(model().attribute("cityName", equalTo("London")))
            .andExpect(model().attributeExists("temp"))
            .andExpect(model().attributeExists("desc"));
    }
    
    @Ignore
    @Test
    public void checkFormValidateError() throws Exception {
        String URI = "/process";
        
        RequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URI)
                .param("name", "")
                .param("provider", "openWeather");
        
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        
        assertThat(response.getContentAsString(), containsString("must match"));
    }
    
}
