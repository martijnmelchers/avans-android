package com.example.weatherinator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.example.weatherinator.models.LocalLocation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddImageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Button cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddImageActivity.this, MainActivity.class));
            }
        });

        // Lookup the recyclerview in activity layout
        RecyclerView rvContacts = findViewById(R.id.selectLocationRv);


        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        List<LocalLocation> savedLocations = LocalManager.GetLocations(sharedPref);

        CityAdapter adapter = new CityAdapter(savedLocations);
        // Create adapter passing in the sample user data
        // Attach the adapter to the recyclerview to populate items
        rvContacts.setAdapter(adapter);
        // Set layout manager to position the items
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action.equals(Intent.ACTION_SEND)) {
            if (Objects.requireNonNull(intent.getType()).startsWith("image/")) {
                //handleImageShare(intent);
            }
        }
    }


    private void handleImageShare(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        System.out.println(imageUri);

        ImageView imgView = (ImageView) findViewById(R.id.iv);
        imgView.setImageURI(imageUri);
    }
}


class CityAdapter  extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private List<LocalLocation> cities = new ArrayList<>();
    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
            .inflate(R.layout.city, parent, false);

        CityViewHolder vh = new CityViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CityAdapter.CityViewHolder holder, int position) {
        TextView text = holder.textView.findViewById(R.id.cityText);
        text.setText(this.cities.get(position).GetName());
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CityAdapter(List<LocalLocation> cities) {
        this.cities = cities;
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        public View textView;

        public CityViewHolder(@NonNull View v) {
            super(v);
            textView = v;
        }
    }

    public void UpdateResults(List<LocalLocation> cities) {
        this.cities = cities;
        this.notifyDataSetChanged();
    }
}
