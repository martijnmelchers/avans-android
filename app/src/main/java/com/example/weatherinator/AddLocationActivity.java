package com.example.weatherinator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.weatherinator.models.LocalLocation;
import com.example.weatherinator.models.WeatherLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class AddLocationActivity extends AppCompatActivity {
    private EditText input;
    private Geocoder geocoder;
    private RecyclerView rvCities;
    private CityAdapter adapter;


    long delay = 1000; // 1 seconds after user stops typing
    long lastTextEdit = 0;
    Handler handler = new Handler();

    private Runnable searchAfterInput = new Runnable() {
        public void run() {
            new GetGeocodeResults().execute(input.getText().toString());
        }
    };


    public class GetGeocodeResults extends AsyncTask<String, String, List<Address>> {
        public List<Address> doInBackground(String... params) {
            try {
                return geocoder.getFromLocationName(params[0], 10);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return new ArrayList<>();
        }

        @Override
        protected void onPostExecute(List<Address> addresses) {
            List<LocalLocation> locations = new ArrayList<>();

            for (Address a: addresses)
                locations.add(new LocalLocation(a.getLocality()));

            adapter.UpdateResults(locations);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        RecyclerView rvCities = findViewById(R.id.recyclerView);
        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);


        adapter = new CityAdapter(new ArrayList<LocalLocation>(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocalManager.AddLocation(sharedPref, new LocalLocation(((TextView) v.findViewById(R.id.cityText)).getText().toString()));
                startActivity(new Intent(AddLocationActivity.this, MainActivity.class));
            }
        });
        geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

        rvCities.setAdapter(adapter);
        rvCities.setLayoutManager(new LinearLayoutManager(this));



        input = findViewById(R.id.locationSearch);

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    lastTextEdit = System.currentTimeMillis();
                    handler.postDelayed(searchAfterInput, delay);
                }
            }
        });
    }
}
