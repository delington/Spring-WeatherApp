package com.weather.form;

import javax.validation.constraints.Pattern;

public class CityForm {

    @Pattern(regexp = "[a-zA-Z]+")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CityForm [name=" + name + "]";
    }

}
