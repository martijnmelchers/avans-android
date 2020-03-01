package com.example.weatherinator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.example.weatherinator.models.Coord;
import com.example.weatherinator.models.LocalLocation;
import com.example.weatherinator.models.WeatherLocation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });


        setContentView(R.layout.main_fragment);

        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = findViewById(R.id.rvLocations);

        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        List<LocalLocation> savedLocations = new ArrayList<>();


        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            LocationManager lm = (LocationManager)getSystemService(LOCATION_SERVICE);

            if(!lm.isProviderEnabled(LocationManager.GPS_PROVIDER))
                Toast.makeText(this, "GPS is uitgeschakeld!", Toast.LENGTH_LONG).show();

            List<String> x = lm.getAllProviders();

            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location == null)
                return;

            double longitude = location.getLongitude();
            double latitude = location.getLatitude();

            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            try {
                Address address = gcd.getFromLocation(latitude, longitude, 1).get(0);
                savedLocations.add(new LocalLocation(address.getLocality()));
            } catch (IOException e) {
                e.printStackTrace();
            }

//            try {
//                WeatherLocation bruh = new GetWeatherCoordTask().execute(new Coord(longitude, latitude)).get();
//                savedLocations.add(new LocalLocation(name));
//            } catch (ExecutionException e) {
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

        }
        savedLocations = LocalManager.GetLocations(sharedPref);

        // Create adapter passing in the sample user data
        WeatherAdapter adapter = new WeatherAdapter(savedLocations);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        GetWeatherCoordTask GetWeather = new GetWeatherCoordTask();
        try {
            for (LocalLocation loc : savedLocations)
                loc.setWeather(new GetWeatherTask().execute(loc.GetName()).get());

        } catch (Exception e) {
        }
    }




    public class GetWeatherTask extends AsyncTask<String, String, WeatherLocation> {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
