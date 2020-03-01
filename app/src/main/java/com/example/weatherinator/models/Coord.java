package com.example.weatherinator.models;

public class Coord {
    private double latitude;
    private double longitude;

    public Coord(double latitude, double longitude){
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double GetLat(){
        return this.latitude;
    }

    public double GetLong(){
        return this.longitude;
    }
}
