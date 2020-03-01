package com.example.weatherinator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherinator.models.LocalLocation;

import java.util.ArrayList;
import java.util.List;


public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {
    private List<LocalLocation> weatherDataset = new ArrayList<>();

    public WeatherAdapter(List<LocalLocation> weatherDataset){
        this.weatherDataset = weatherDataset;
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
        TextView textView = viewHolder.nameTextView;
        textView.setText(location.GetName());

        Button button = viewHolder.messageButton;
        button.setText(location.GetWeatherLocation().GetCurrentWeather().getMain());
        button.setEnabled(true);
    }

    @Override
    public int getItemCount() {
        return weatherDataset.size();
    }

    public static class WeatherViewHolder extends RecyclerView.ViewHolder {

        public TextView nameTextView;
        public Button messageButton;
        // each data item is just a string in this case
        public WeatherViewHolder(View v) {
            super(v);
            nameTextView = (TextView) itemView.findViewById(R.id.contact_name);
            messageButton = (Button) itemView.findViewById(R.id.message_button);
        }
    }
}
