package com.weather.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.weather.deserializer.LongToStringDeserializer;
import lombok.Data;

@Data
public class Place {

    @JsonDeserialize(using = LongToStringDeserializer.class)
    @JsonProperty("place_id")
    private String placeId;

    private float lat;
    private float lon;

    @JsonProperty("class")
    private String className;

}
