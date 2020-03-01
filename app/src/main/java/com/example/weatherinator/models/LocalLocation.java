package com.example.weatherinator.models;

import android.net.Uri;

public class LocalLocation {
    private String name;
    private WeatherLocation weather = null;
    private Uri imageSource = null;

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

    public Uri GetImageSource(){
        return this.imageSource;
    }

    public void setImageSource(Uri imageSource) {
        this.imageSource = imageSource;
    }
    private void SetImageSource(Uri imageSource){
        this.imageSource = imageSource;
    }
}
