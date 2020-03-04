package com.example.weatherinator;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.weatherinator.models.LocalLocation;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Random;

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
        LocalLocation item = ((MainActivity) getActivity()).savedLocations.get(currentItem);


        if (getActivity() == null) {
            return;
        }

        ((TextView) getActivity().findViewById(R.id.detailName)).setText(item.GetName());
        ((TextView) getActivity().findViewById(R.id.detailTemperature)).setText(item.GetWeatherLocation().getWeatherMain().getTemp() + getString(R.string.celciusText));

        String description = item.GetWeatherLocation().GetCurrentWeather().GetDescription();
        String descriptionUppercase = description.substring(0, 1).toUpperCase() + description.substring(1);

        ((TextView) getActivity().findViewById(R.id.detailSummary)).setText(descriptionUppercase);

        ((TextView) getActivity().findViewById(R.id.detailFeelsLike)).setText(item.GetWeatherLocation().getWeatherMain().getFeels_like() + getString(R.string.celciusText));
        ((TextView) getActivity().findViewById(R.id.detailMinimum)).setText(item.GetWeatherLocation().getWeatherMain().getTemp_min() + getString(R.string.celciusText));
        ((TextView) getActivity().findViewById(R.id.detailMaximum)).setText(item.GetWeatherLocation().getWeatherMain().getTemp_max() + getString(R.string.celciusText));
//        ((ImageView)getActivity().findViewById(R.id.detailsImage)).setImageURI(item.GetImageSource());


        if (item.GetImageSource() != null) {
            loadImageFromStorage(item.GetImageSource().toString());
        }

        this.currentIndex = currentItem;
        view.findViewById(R.id.setBackgroundButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivityForResult(Intent.createChooser(intent, "Selecteer een achtergrond"), 100);
            }
        });


        view.findViewById(R.id.detailDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity activity = ((MainActivity) getActivity());
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
        if (requestCode == 100) {
            Uri uri = data.getData();
            int index = this.currentIndex;
            MainActivity activity = ((MainActivity) getActivity());

            try {
                Bitmap b = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
                String savedPath = saveToInternalStorage(b);
                activity.savedLocations.get(index).setImageSource(Uri.parse(savedPath));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            activity.CommitLocations();
            this.Refresh();
        }
    }

    private void loadImageFromStorage(String path) {
        try {

            File f = new File(path);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(f));
            ImageView view = ((ImageView) getActivity().findViewById(R.id.detailsImage));
            view.setImageBitmap(b);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getActivity().getApplicationContext());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir

        String randomFilename = UUID.randomUUID().toString() + ".jpg";

        File mypath = new File(directory, randomFilename);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getAbsolutePath();
    }


    private void Refresh() {
        getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
}
