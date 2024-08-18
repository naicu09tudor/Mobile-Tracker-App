package edu.utcluj.track;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class SendActivity extends AppCompatActivity implements View.OnClickListener {
    private Executor executor = Executors.newFixedThreadPool(1);
    private volatile Handler msgHandler;

    private LocationManager locationManager;
    private String androidId;

    private static final String STATIC_LOCATION = "{" +
            "\"terminalId\":\"%s\"," +
            "\"latitude\":\"%s\"," +
            "\"longitude\":\"%s\"" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        // Inițializează locationManager și obține ID-ul Android
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        //Toast.makeText(this, "Android ID: " + androidId, Toast.LENGTH_LONG).show();



        // Verifică permisiunile și le cere dacă nu sunt acordate
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        } else {

            startLocationUpdates();
        }

        Button sendButton = findViewById(R.id.button_send);
        sendButton.setOnClickListener(this);

        msgHandler = new MsgHandler(this);

        Button btnOpenSettings = findViewById(R.id.btnOpenSettings);
        btnOpenSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SendActivity.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
    }

    private void startLocationUpdates() {
        try {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 10, locationListener);
        } catch (SecurityException e) {
            e.printStackTrace(); // Log the exception
        }
    }

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                // Use the location in your sendCoordinates call
                executor.execute(new Runnable() {
                    public void run() {
                        Message msg = msgHandler.obtainMessage();
                        msg.arg1 = sendCoordinates(androidId, String.valueOf(location.getLatitude()), String.valueOf(location.getLongitude())) ? 1 : 0;
                        msgHandler.sendMessage(msg);
                    }
                });

                locationManager.removeUpdates(this);
            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 0) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            }
            else {
                Toast.makeText(this, "Eroare Location Updates", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onClick(View v) {

        // Get Android ID
        final String androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);



        // Get LocationManager instance
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // Check if permission is granted
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Request the permission
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            return;
        }

        // Get the last known location
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final double latitude;
        final double longitude;


        if (location != null) {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        } else {
            // Fallback to default values or show error
            latitude = 0; // Default latitude if not available
            longitude = 0; // Default longitude if not available
            Toast.makeText(this, "Location not available", Toast.LENGTH_SHORT).show();
            return;
        }

        executor.execute(new Runnable() {
            public void run() {
                Message msg = msgHandler.obtainMessage();
                // use MAC addr or IMEI as terminal id
                // read true position
                // replace static coordinates with the ones from the true position
                msg.arg1 = sendCoordinates(androidId, String.valueOf(latitude), String.valueOf(longitude)) ? 1 : 0;
                msgHandler.sendMessage(msg);
            }
        });
    }

    private boolean sendCoordinates(String terminalId, String lat, String lng) {
        HttpURLConnection con = null;
        try {

            // Preluăm setările serverului din SharedPreferences
            SharedPreferences prefs = getSharedPreferences("AppSettings", MODE_PRIVATE);
            String serverIP = prefs.getString("ServerIP", "10.0.2.2");
            int serverPort = prefs.getInt("ServerPort", 8082);

            // Construim URL-ul folosind adresa IP și portul serverului
            URL obj = new URL("http://" + serverIP + ":" + serverPort + "/positions");

            con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            con.setDoOutput(true);
            OutputStream os = con.getOutputStream();
            os.write(String.format(STATIC_LOCATION, terminalId, lat, lng).getBytes());
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) { //success
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) {
                con.disconnect();
            }
        }
    }


    private static class MsgHandler extends Handler {
        private final WeakReference<Activity> sendActivity;

        public MsgHandler(Activity activity) {
            sendActivity = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.arg1 == 1) {
                Toast.makeText(sendActivity.get().getApplicationContext(),
                        "Success!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(sendActivity.get().getApplicationContext(),
                        "Error!", Toast.LENGTH_LONG).show();
            }
        }
    }
}