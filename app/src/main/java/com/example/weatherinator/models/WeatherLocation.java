package com.example.weatherinator.models;

import java.util.List;

public class WeatherLocation {
    private List<Weather> weather;
    private Coord coord;
    private String name;


    public WeatherLocation(String name, List<Weather> weather, Coord coord){
        this.name = name;
        this.weather = weather;
        this.coord = coord;
    }
    public Weather GetCurrentWeather(){
        return weather.get(0);
    }
    public List<Weather> GetAllWeather(){
        return this.weather;
    }
}
