package com.example.weatherinator.models;

public class Weather {
    private int id;
    private String main;
    private String description;

    public Weather(int id, String main, String description){
        this.id = id;
        this.main = main;
        this.description = description;
    }
}
