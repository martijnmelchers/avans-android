package com.example.weatherinator.models;

public class LocalLocation {
    private String name;
    private WeatherLocation weather = null;

    public LocalLocation(String name){
        this.name = name;
    }

    public String GetName(){
        return this.name;
    }

    public WeatherLocation GetWeatherLocation() {
        return this.weather;
    }

    public void setWeather(WeatherLocation weather){
        this.weather = weather;
    }
    public boolean HasWeather(){
        return this.weather != null;
    }
}
