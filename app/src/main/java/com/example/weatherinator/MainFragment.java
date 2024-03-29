package com.example.weatherinator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherinator.models.Coord;
import com.example.weatherinator.models.LocalLocation;
import com.example.weatherinator.models.WeatherLocation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.ExecutionException;

public class MainFragment extends Fragment {
    private WeatherAdapter adapter;
    private LocationManager locationManager;
    @Override
    public View onCreateView(
        LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment

        locationManager = (LocationManager)
            getActivity().getSystemService(Context.LOCATION_SERVICE);


        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        FloatingActionButton fab = view.findViewById(R.id.floatingButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddLocationActivity.class));
            }
        });




        // Lookup the recyclerview in activity layout
        final RecyclerView rvLocations = view.findViewById(R.id.rvLocations);


        // Create adapter passing in the sample user data
        adapter = new WeatherAdapter(((MainActivity) getActivity()).savedLocations, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle args = new Bundle();
                args.putInt("localId", rvLocations.findContainingViewHolder(v).getLayoutPosition());
                NavHostFragment.findNavController(MainFragment.this).navigate(R.id.openDetails, args);
            }
        });

        // Attach the adapter to the recyclerview to populate items
        rvLocations.setAdapter(adapter);
        // Set layout manager to position the items
        rvLocations.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        if (ContextCompat.checkSelfPermission(getActivity().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            StartGPSTracking();
        }

    }

    public static class GetWeatherTask extends AsyncTask<String, String, WeatherLocation> {
        public WeatherLocation doInBackground(String... params) {
            OpenWeatherApi api = new OpenWeatherApi();
            return api.GetWeatherByString(params[0]);
        }
    }

    public class GetWeatherCoordTask extends AsyncTask<Coord, String, WeatherLocation> {
        public WeatherLocation doInBackground(Coord... params) {
            OpenWeatherApi api = new OpenWeatherApi();
            return api.GetWeatherByCoords(params[0]);
        }
    }

    /*---------- Listener class to get coordinates ------------- */
    private class WeatherLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {
            /*------- To get city name from coordinates -------- */
            try {
                MainActivity activity = (MainActivity)getActivity();


                if(activity == null){
                    return;
                }
                if (!activity.networkAvailable){
                    Toast.makeText(getActivity().getApplicationContext(), "WIFI is uitgeschakeld", Toast.LENGTH_LONG).show();
                    return;
                }

                boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                if(!gps_enabled){
                    Toast.makeText(getActivity().getApplicationContext(), "GPS is uitgeschakeld", Toast.LENGTH_LONG).show();
                    return;
                }

                WeatherLocation weather = new GetWeatherCoordTask().execute(new Coord(loc.getLatitude(), loc.getLongitude())).get();

                if(weather == null){
                    Toast.makeText(getActivity().getApplicationContext(), "WIFI is uitgeschakeld", Toast.LENGTH_LONG).show();
                    return;
                }

                LocalLocation location = new LocalLocation(weather.getName());
                location.setWeather(weather);

                if(getView() != null){
                    ((TextView)getView().findViewById(R.id.currentGPSLocation)).setText(weather.getName());
                    ((TextView)getView().findViewById(R.id.currentTemp)).setText(weather.getWeatherMain().getTemp() + getString(R.string.celciusText));
                }

                //adapter.addLocation(location);
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    @SuppressLint("MissingPermission")
    private void StartGPSTracking() {
        boolean gps_enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if ( !gps_enabled )
            Toast.makeText(getActivity().getApplicationContext(), "GPS is uitgeschakeld!", Toast.LENGTH_LONG).show();
        else {
            getActivity().findViewById(R.id.gpsConstraint).setVisibility(View.VISIBLE);
            LocationListener locationListener = new WeatherLocationListener();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode != 1)
            return;

        if(grantResults[0] == PackageManager.PERMISSION_GRANTED)
            StartGPSTracking();
    }

}
