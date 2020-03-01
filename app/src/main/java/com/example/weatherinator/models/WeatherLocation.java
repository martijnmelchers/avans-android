package com.example.weatherinator.models;

import java.util.List;

public class WeatherLocation {
    private List<Weather> weather;
    private Coord coord;
    private String name;
    private WeatherMain main;

    public WeatherLocation(String name, List<Weather> weather, Coord coord, WeatherMain main){
        this.name = name;
        this.weather = weather;
        this.main = main;
        this.coord = coord;
    }
    public Weather GetCurrentWeather(){
        return weather.get(0);
    }
    public List<Weather> GetAllWeather(){
        return this.weather;
    }

    public String getName() {
        return name;
    }


    public WeatherMain getWeatherMain() {
        return this.main;
    }
}
