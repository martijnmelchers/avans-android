package com.example.weatherinator;

import android.os.AsyncTask;
import android.util.JsonReader;

import com.example.weatherinator.models.Coord;
import com.example.weatherinator.models.Weather;
import com.example.weatherinator.models.WeatherLocation;
import com.example.weatherinator.models.WeatherMain;

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

public class OpenWeatherApi {
    private static String API_KEY = "7efca39694c9a2fd264aa8167338322f";

    public OpenWeatherApi() {

    }

    public WeatherLocation GetWeatherByCoords(Coord coord) {
        WeatherLocation wLoc = null;
        URL url = null;
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/weather?lat=" + coord.GetLat() + "&lon=" + coord.GetLong() + "&appid=" + API_KEY + "&units=metric&lang=nl");
        } catch (MalformedURLException e) {
        }

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            wLoc = readLocation(in);

            urlConnection.disconnect();
        } catch (IOException e) {
            System.out.println("");
        }

        return wLoc;
    }

    public WeatherLocation GetWeatherByString(String location) {

        WeatherLocation wLoc = null;
        URL url = null;
        try {
            url = new URL("https://api.openweathermap.org/data/2.5/weather?q=" + location + "&appid=" + API_KEY + "&units=metric&lang=nl");
        } catch (MalformedURLException e) {
        }

        try {
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());

            wLoc = readLocation(in);

            urlConnection.disconnect();
        } catch (IOException e) {
            System.out.println("");
        }

        return wLoc;
    }

    private WeatherLocation readLocation(InputStream in) throws IOException {
        //Read from a file, or a HttpRequest, or whatever.
        JsonReader reader = new JsonReader(new InputStreamReader(in));
        List<Weather> weathers = new ArrayList<>();
        String cityname = null;
        Coord coord = null;
        WeatherMain wMain = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "weather": {
                    weathers = readWeathers(reader);
                    break;
                }
                case "name":
                    cityname = reader.nextString();
                    break;
                case "coord": {
                    coord = readCoord(reader);
                    break;
                }
                case "main": {
                    wMain = readMain(reader);
                    break;
                }
                default: {
                    reader.skipValue();
                    break;
                }
            }
        }
        reader.endObject();
        return new WeatherLocation(cityname, weathers, coord, wMain);
    }


    public List<Weather> readWeathers(JsonReader reader) throws IOException {
        List<Weather> weathers = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            weathers.add(readWeather(reader));
        }

        reader.endArray();

        return weathers;
    }


    public Weather readWeather(JsonReader reader) throws IOException {
        int id = -1;
        String main = null;
        String description = null;
        reader.beginObject();
        while (reader.hasNext()) {
            String name = reader.nextName();
            switch (name) {
                case "id":
                    id = reader.nextInt();
                    break;
                case "main":
                    main = reader.nextString();
                    break;
                case "description":
                    description = reader.nextString();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }
        reader.endObject();
        return new Weather(id, main, description);
    }



    private WeatherMain readMain(JsonReader reader) throws IOException {
        float temp = -1;
        float feels_like = -1;
        float temp_min = -1;
        float temp_max = -1;
        float pressure = -1;
        float humidity = -1;


        reader.beginObject();

        while(reader.hasNext()){
            String name = reader.nextName();

            switch (name){
                case "temp":
                    temp = (float)reader.nextDouble();
                    break;
                case "feels_like":
                    feels_like = (float)reader.nextDouble();
                    break;
                case "temp_max":
                    temp_max = (float)reader.nextDouble();
                    break;
                case "temp_min":
                    temp_min = (float)reader.nextDouble();
                    break;
                case "pressure":
                    pressure = (float)reader.nextInt();
                    break;
                case "humidity":
                    humidity = (float)reader.nextInt();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();
        return new WeatherMain(temp,feels_like,temp_min, temp_max,pressure,humidity);
    }


    private Coord readCoord(JsonReader reader) throws IOException {
        double latitude = 0;
        double longitude = 0;


        reader.beginObject();

        while (reader.hasNext()) {
            String name = reader.nextName();

            switch (name) {
                case "lat":
                    latitude = reader.nextDouble();
                    break;
                case "lon":
                    longitude = reader.nextDouble();
                    break;
                default:
                    reader.skipValue();
                    break;
            }
        }

        reader.endObject();
        return new Coord((float) latitude, (float) longitude);
    }
}
