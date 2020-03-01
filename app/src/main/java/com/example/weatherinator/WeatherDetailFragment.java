package com.example.weatherinator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.weatherinator.models.LocalLocation;

public class WeatherDetailFragment extends Fragment {
    private int currentIndex;

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
        final int currentItem = getArguments().getInt("localId", 0);
        LocalLocation item = ((MainActivity)getActivity()).savedLocations.get(currentItem);

        ((TextView)getActivity().findViewById(R.id.detailName)).setText(item.GetName());
        ((TextView)getActivity().findViewById(R.id.detailTemperature)).setText(item.GetWeatherLocation().getWeatherMain().getTemp() + getString(R.string.celciusText));

        String description = item.GetWeatherLocation().GetCurrentWeather().GetDescription();
        String descriptionUppercase = description.substring(0, 1).toUpperCase() + description.substring(1);

        ((TextView)getActivity().findViewById(R.id.detailSummary)).setText(descriptionUppercase);

        ((TextView)getActivity().findViewById(R.id.detailFeelsLike)).setText(item.GetWeatherLocation().getWeatherMain().getFeels_like() + getString(R.string.celciusText));
        ((TextView)getActivity().findViewById(R.id.detailMinimum)).setText(item.GetWeatherLocation().getWeatherMain().getTemp_min() + getString(R.string.celciusText));
        ((TextView)getActivity().findViewById(R.id.detailMaximum)).setText(item.GetWeatherLocation().getWeatherMain().getTemp_max() + getString(R.string.celciusText));
        ((ImageView)getActivity().findViewById(R.id.detailsImage)).setImageURI(item.GetImageSource());

        this.currentIndex = currentItem;
        view.findViewById(R.id.setBackgroundButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent, "Selecteer een achtergrond"), 100);
            }
        });


        view.findViewById(R.id.detailDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = ((MainActivity)getActivity());
                activity.savedLocations.remove(currentIndex);
                currentIndex = -1;
                activity.CommitLocations();
                NavHostFragment.findNavController(WeatherDetailFragment.this).navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100){
            Uri uri=data.getData();
            int index = this.currentIndex;
            MainActivity activity = ((MainActivity)getActivity());
            activity.savedLocations.get(index).setImageSource(uri);
            activity.CommitLocations();
            this.Refresh();
        }
    }

    private void Refresh(){
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}
