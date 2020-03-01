package com.example.weatherinator;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import androidx.appcompat.app.AlertDialog;
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

import java.util.ArrayList;
import java.util.List;

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


        setContentView(R.layout.fragment_first);

        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvLocations);

        // Initialize contacts
        List<LocalLocation> locations = new ArrayList<LocalLocation>(){ {
            add(new LocalLocation("Wijk bij Duurstede"));
            add(new LocalLocation("Doorn"));
            add(new LocalLocation("Zeist"));
            add(new LocalLocation("De Meern"));
        }};

        // Create adapter passing in the sample user data
        WeatherAdapter adapter = new WeatherAdapter(locations);
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        GetWeatherCoordTask GetWeather = new GetWeatherCoordTask();
        try{
            for (LocalLocation loc:
                locations) {

                loc.setWeather(new GetWeatherTask().execute(loc.GetName()).get());
            }
        }
        catch (Exception e) { }

        try{
            WeatherLocation loc =  GetWeather.execute(new Coord( (float)35.02,(float)139.01)).get();
            System.out.println("test");
        }
        catch (Exception e){
            System.out.println();
        }

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions( this, new String[] {  android.Manifest.permission.ACCESS_COARSE_LOCATION  },  1);
        }


        Intent intent = getIntent();
        String action = intent.getAction();

        if(action == Intent.ACTION_SEND){
            if(intent.getType().startsWith("image/")){
                handleImageShare(intent);
            }
        }
    }



    private void handleImageShare(Intent intent){
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        System.out.println(imageUri);

        ImageView imgView = (ImageView)findViewById(R.id.iv);
        imgView.setImageURI(imageUri);
    }

    public class GetWeatherTask extends AsyncTask<String, String, WeatherLocation> {
        public WeatherLocation doInBackground(String... params){
            OpenWeatherApi api = new OpenWeatherApi();
            return api.GetWeather(params[0]);
        }
    }

    public class GetWeatherCoordTask extends AsyncTask<Coord, String, WeatherLocation> {
        public WeatherLocation doInBackground(Coord... params) {
            OpenWeatherApi api = new OpenWeatherApi();
            return api.GetWeather(params[0]);
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
