package com.example.weatherinator;

import android.content.SharedPreferences;
import android.net.Uri;

import com.example.weatherinator.models.LocalLocation;

import java.util.ArrayList;
import java.util.List;

public class LocalManager {
    public static List<LocalLocation> GetLocations(SharedPreferences sharedPreferences) {
        List<LocalLocation> savedLocations = new ArrayList<>();
        for (String dataString : sharedPreferences.getString("saved_locations", "").split(",")) {
            if (dataString.equals(""))
                continue;

            String[] data = dataString.split("~");
            String name = data[0];
            String image = data[1];

            LocalLocation location = new LocalLocation(name);

            if(!image.equals("none"))
                location.setImageSource(new Uri.Builder().appendPath(image).build());

            savedLocations.add(location);
        }

        return savedLocations;
    }

    public static void AddLocation(SharedPreferences sharedPreferences, LocalLocation location) {
        List<LocalLocation> existingLocations = GetLocations(sharedPreferences);

        existingLocations.add(location);

        StringBuilder encodedString = new StringBuilder();

        for (int i = 0; i < existingLocations.size(); i++) {
            encodedString.append(existingLocations.get(i).GetName());
            encodedString.append("~");

            if(existingLocations.get(i).GetImageSource() != null)
                encodedString.append(existingLocations.get(i).GetImageSource().toString());
            else
                encodedString.append("none");

            if(i != existingLocations.size() - 1)
                encodedString.append(",");
        }

        sharedPreferences.edit().putString("saved_locations", encodedString.toString()).apply();

    }
}
