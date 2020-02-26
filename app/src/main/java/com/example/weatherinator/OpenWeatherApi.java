package com.example.weatherinator;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.example.weatherinator.models.Weather;
import com.example.weatherinator.models.WeatherLocation;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static android.provider.ContactsContract.CommonDataKinds.Website.URL;

public  class OpenWeatherApi extends AsyncTask<String, String, WeatherLocation> {

    private static String API_KEY = "7efca39694c9a2fd264aa8167338322f";
    public OpenWeatherApi(){

    }

    public WeatherLocation doInBackground(String... params){
        return GetWeather(params);
    }

    public WeatherLocation GetWeather(String... location){

        WeatherLocation wLoc = null;
        URL url = null;
        try{
            url = new URL("https://api.openweathermap.org/data/2.5/weather?q=London&appid=" + API_KEY);
        }
        catch (MalformedURLException e)
        {}


        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            wLoc =  readLocation(in);

            urlConnection.disconnect();
        }
        catch (IOException e){
            System.out.println("");
        }

        return wLoc;
    }

    private WeatherLocation readLocation(InputStream in) throws IOException{
         //Read from a file, or a HttpRequest, or whatever.
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        List<Weather> weathers = new ArrayList<>();
        String cityname = null;
        float latitude = -1;
        float longitude = -1;
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            switch(name){
                case "weather": {
                    weathers = readWeathers(reader);
                    break;
                }
                case "name": cityname = reader.nextString();
                    break;
                case  "coord": {
                    latitude = 0;
                    longitude = 0;
                    reader.skipValue();
                    break;
                }
                default: {
                    reader.skipValue();
                    break;
                }
            }
        }
        reader.endObject();
        return new WeatherLocation(cityname, weathers, longitude, latitude);
    }


    public List<Weather> readWeathers(JsonReader reader) throws IOException{
        List<Weather> weathers = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()){
            weathers.add(readWeather(reader));
        }

        reader.endArray();

        return weathers;
    }


    public Weather readWeather(JsonReader reader) throws  IOException {
        int id = -1;
        String main = null;
        String description = null;
        reader.beginObject();
        while(reader.hasNext()){
            String name = reader.nextName();
            switch (name){
                case "id": id = reader.nextInt();
                    break;
                case "main": main = reader.nextString();
                    break;
                case "description": description = reader.nextString();
                break;
                default: reader.skipValue();
                break;
            }
        }
        reader.endObject();
        return new Weather(id,main,description);
    }
}
