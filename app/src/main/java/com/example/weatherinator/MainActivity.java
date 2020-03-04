package com.example.weatherinator;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.weatherinator.models.Coord;
import com.example.weatherinator.models.LocalLocation;
import com.example.weatherinator.models.WeatherLocation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public List<LocalLocation> savedLocations = new ArrayList<>();
    public boolean networkAvailable = false;
    private boolean popup = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startNetworkListener();


        ConnectivityManager connMgr =
            (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWifiConn = false;
        boolean isMobileConn = false;
        for (Network network : connMgr.getAllNetworks()) {
            NetworkInfo networkInfo = connMgr.getNetworkInfo(network);
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                isWifiConn |= networkInfo.isConnected();
            }
            if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                isMobileConn |= networkInfo.isConnected();
            }
        }

        if(!isMobileConn && !isWifiConn){
            ShowDialog();
        }
        else{
            Init();
        }
    }

    private void Init(){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        savedLocations = LocalManager.GetLocations(sharedPref);

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

    private boolean startNetworkListener() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkRequest.Builder builder = new NetworkRequest.Builder();

        connectivityManager.registerNetworkCallback(
                builder.build(),
                new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onLost(Network network) {
                        // When network connection is lost, close the app
                        networkAvailable = false;
//                        ShowDialog();
                    }

                    @Override
                    public void onAvailable(Network network){
                        networkAvailable = true;
//                        ShowDialog();

                    }
                }

        );

        return true;
    }


    private void ShowDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        // Add the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                System.exit(0);
            }
        });
        builder.setMessage("Geen internetverbinding gevonden");
        // Create the AlertDialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void CommitLocations (){
        SharedPreferences sharedPref = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        LocalManager.UpdateLocations(sharedPref,savedLocations);
        return;
    }

}
