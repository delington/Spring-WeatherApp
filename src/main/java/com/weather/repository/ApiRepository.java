package com.weather.repository;

public interface ApiRepository<T> {
    
    public T getInformationAndMapToObject(String url);
    
}
