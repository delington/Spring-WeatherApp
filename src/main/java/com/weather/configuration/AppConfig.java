package com.weather.configuration;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.weather.client.NominatimClient;
import com.weather.client.SevenTimerClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class AppConfig {

    public static final String NOMINATIM_BASE_URL = "https://nominatim.openstreetmap.org";
    public static final String SEVEN_TIMER_BASE_URL = "http://www.7timer.info";

    @Bean
    public NominatimClient nominatimClient() {
        final var objectMapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(NOMINATIM_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        NominatimClient client = retrofit.create(NominatimClient.class);
        return client;
    }

    @Bean
    public SevenTimerClient sevenTimerClient() {
        final var objectMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SEVEN_TIMER_BASE_URL)
                .addConverterFactory(JacksonConverterFactory.create(objectMapper))
                .build();

        SevenTimerClient client = retrofit.create(SevenTimerClient.class);
        return client;
    }
}
