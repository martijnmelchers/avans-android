package com.example.weatherinator.models;

public class Coord {
    private float latitude;
    private float longitude;

    public Coord(float latitude, float longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public float GetLat(){
        return this.latitude;
    }

    public float GetLong(){
        return this.longitude;
    }
}
