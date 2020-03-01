package com.example.weatherinator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.weatherinator.models.LocalLocation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AddLocationActivity extends AppCompatActivity {
    private EditText input;
    private Geocoder geocoder;
    private RecyclerView rvCities;
    private CityAdapter adapter;


    long delay = 500; // 1 seconds after user stops typing
    long lastTextEdit = 0;
    Handler handler = new Handler();

    private Runnable searchAfterInput = new Runnable() {
        public void run() {
            if (System.currentTimeMillis() > (lastTextEdit + delay - 500)) {
                try {
                    List<Address> addresses = geocoder.getFromLocationName(input.getText().toString(), 10);
                    List<LocalLocation> locations = new ArrayList<>();

                    for (Address a: addresses)
                        locations.add(new LocalLocation(a.getLocality()));

                    adapter.UpdateResults(locations);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        RecyclerView rvCities = findViewById(R.id.recyclerView);

        adapter = new CityAdapter(new ArrayList<LocalLocation>(), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(true);
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
