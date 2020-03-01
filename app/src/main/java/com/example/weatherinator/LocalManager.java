package com.example.weatherinator;

import android.content.SharedPreferences;

import com.example.weatherinator.models.LocalLocation;

import java.util.ArrayList;
import java.util.List;

public class LocalManager {
    public static List<LocalLocation> GetLocations(SharedPreferences sharedPreferences) {
        List<LocalLocation> savedLocations = new ArrayList<>();
        for (String location : sharedPreferences.getString("saved_locations", "").split(","))
            if (!location.equals(""))
                savedLocations.add(new LocalLocation(location));

        return savedLocations;
    }
}
