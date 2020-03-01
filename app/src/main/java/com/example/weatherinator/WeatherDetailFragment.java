package com.example.weatherinator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.weatherinator.models.LocalLocation;

public class WeatherDetailFragment extends Fragment {

    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.weather_detail_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        int currentItem = getArguments().getInt("localId", 0);
        LocalLocation item = ((MainActivity)getActivity()).savedLocations.get(currentItem);

        ((TextView)getActivity().findViewById(R.id.detailName)).setText(item.GetName());
        ((TextView)getActivity().findViewById(R.id.detailTemperature)).setText(item.GetWeatherLocation().getWeatherMain().getTemp() + getString(R.string.celciusText));

        String description = item.GetWeatherLocation().GetCurrentWeather().GetDescription();
        String descriptionUppercase = description.substring(0, 1).toUpperCase() + description.substring(1);

        ((TextView)getActivity().findViewById(R.id.detailSummary)).setText(descriptionUppercase);

        ((TextView)getActivity().findViewById(R.id.detailFeelsLike)).setText(item.GetWeatherLocation().getWeatherMain().getFeels_like() + getString(R.string.celciusText));
        ((TextView)getActivity().findViewById(R.id.detailMinimum)).setText(item.GetWeatherLocation().getWeatherMain().getTemp_min() + getString(R.string.celciusText));
        ((TextView)getActivity().findViewById(R.id.detailMaximum)).setText(item.GetWeatherLocation().getWeatherMain().getTemp_max() + getString(R.string.celciusText));
    }
}
