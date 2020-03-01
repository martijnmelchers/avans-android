package com.example.weatherinator.models;

public class WeatherMain {
    private float temp;
    private float feels_like;

    private float temp_min;
    private float temp_max;

    private float pressure;
    private float humidity;


    public WeatherMain(float temp, float feels_like, float temp_min, float temp_max, float pressure, float humidity) {
        this.temp = temp;
        this.feels_like = feels_like;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.pressure = pressure;
        this.humidity = humidity;
    }

    public float getTemp() {
        return temp;
    }
    public float getTemp_min() {
        return temp_min;
    }

    public float getPressure() {
        return pressure;
    }

    public float getHumidity() {
        return humidity;
    }

    public float getTemp_max() {
        return temp_max;
    }

    public float getFeels_like() {
        return feels_like;
    }
}
