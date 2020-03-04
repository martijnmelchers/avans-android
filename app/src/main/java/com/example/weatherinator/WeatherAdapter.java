package com.example.weatherinator;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherinator.models.LocalLocation;
import com.example.weatherinator.models.WeatherLocation;

import java.util.ArrayList;
import java.util.List;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private List<LocalLocation> weatherDataset;
    private View.OnClickListener onClickListener;

    public WeatherAdapter(List<LocalLocation> weatherDataset, View.OnClickListener onClickListener){
        this.weatherDataset = weatherDataset;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.example, parent, false);
        WeatherViewHolder vh = new WeatherViewHolder(view);
        return vh;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(WeatherAdapter.WeatherViewHolder viewHolder, int position) {
        // Get the data model based on position
        LocalLocation location = this.weatherDataset.get(position);

        // Set item views based on your views and data model
        viewHolder.nameTextView.setText(location.GetName());

        String description = location.GetWeatherLocation().GetCurrentWeather().GetDescription();
        String descriptionUppercase = description.substring(0, 1).toUpperCase() + description.substring(1);

        viewHolder.detailsTextView.setText(descriptionUppercase);

        WeatherLocation weatherLocation = location.GetWeatherLocation();
        viewHolder.temperatureTextView.setText(weatherLocation.getWeatherMain().getTemp() + "°C");
        Uri imageSource = this.weatherDataset.get(position).GetImageSource();



        viewHolder.messageButton.setOnClickListener(this.onClickListener);
    }

    @Override
    public int getItemCount() {
        return weatherDataset.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public TextView detailsTextView;
        public TextView temperatureTextView;
        public Button messageButton;

        // each data item is just a string in this case
        public WeatherViewHolder(View v) {
            super(v);
            nameTextView = itemView.findViewById(R.id.contact_name);
            detailsTextView = itemView.findViewById(R.id.status);
            temperatureTextView = itemView.findViewById(R.id.temperature);
            messageButton = itemView.findViewById(R.id.message_button);
        }
    }
}
