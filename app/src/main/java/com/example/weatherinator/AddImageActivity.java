package com.example.weatherinator;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AddImageActivity extends AppCompatActivity {
    private CityAdapter adapter;
    private int selectedItem = -1;
    private Uri imageUri;
    private List<LocalLocation> savedLocations;

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


        final SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        Button confirmButton = findViewById(R.id.confirmButton);
        confirmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                LocalLocation location = savedLocations.get(selectedItem);
                location.setImageSource(imageUri);
                LocalManager.UpdateLocations(sharedPref, savedLocations);
            }
        });

        // Lookup the recyclerview in activity layout
        final RecyclerView rvCities = findViewById(R.id.selectLocationRv);

        savedLocations = LocalManager.GetLocations(sharedPref);

        adapter = new CityAdapter(savedLocations, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tView = v.findViewById(R.id.cityText);
                CharSequence text = tView.getText();
                CityAdapter.CityViewHolder holder = (CityAdapter.CityViewHolder)rvCities.findContainingViewHolder(v);
                int pos = holder.getAdapterPosition();

                if(v.isSelected())
                    v.setSelected(false);
                else
                    v.setSelected(true);

                adapter.notifyItemChanged(pos);
                selectedItem = pos;
            }
        });

        // Create adapter passing in the sample user data
        // Attach the adapter to the recyclerview to populate items
        rvCities.setAdapter(adapter);
        // Set layout manager to position the items
        rvCities.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        String action = intent.getAction();

        if (action.equals(Intent.ACTION_SEND)) {
            if (Objects.requireNonNull(intent.getType()).startsWith("image/")) {
               imageUri =  handleImageShare(intent);
            }
        }
    }


    private Uri handleImageShare(Intent intent) {
        Uri imageUri = (Uri) intent.getParcelableExtra(Intent.EXTRA_STREAM);
        return imageUri;
    }
}


class CityAdapter  extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private List<LocalLocation> cities;
    private View.OnClickListener listener;

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
        holder.bind(listener);
        text.setText(this.cities.get(position).GetName());

        if(holder.itemView.isSelected()){
            holder.itemView.setBackgroundColor(Color.parseColor("#60d680"));
        }
        else{
            holder.itemView.setBackgroundColor(Color.parseColor("#ffffff"));
        }
    }

    @Override
    public int getItemCount() {
        return cities.size();
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public CityAdapter(List<LocalLocation> cities, View.OnClickListener listener) {
        this.cities = cities;
        this.listener = listener;
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        public View textView;

        public CityViewHolder(@NonNull View v) {
            super(v);
            textView = v;
        }

        public void bind(View.OnClickListener listener) {
            itemView.setOnClickListener(listener);
        }
    }


    public void UpdateResults(List<LocalLocation> cities) {
        this.cities = cities;
        this.notifyDataSetChanged();
    }
}
