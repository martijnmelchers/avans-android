package com.example.weatherinator.models;

import java.util.List;

public class WeatherLocation {
    private List<Weather> weather;
    private float latitude;
    private float longitude;
    private String name;


    public WeatherLocation(String name, List<Weather> weather, float longitude, float latitude){
        this.name = name;
        this.weather = weather;
        this.longitude = longitude;
        this.latitude = latitude;
    }
    public Weather GetCurrentWeather(){
        return weather.get(0);
    }
    public List<Weather> GetAllWeather(){
        return this.weather;
    }
}
