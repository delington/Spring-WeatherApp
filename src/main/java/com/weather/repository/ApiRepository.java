package com.weather.repository;

public interface ApiRepository<T> {
    
    public T getData(String url);
    
}
