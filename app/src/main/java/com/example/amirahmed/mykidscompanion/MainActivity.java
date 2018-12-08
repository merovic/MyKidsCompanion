package com.example.amirahmed.mykidscompanion;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.andexert.library.RippleView;

public class MainActivity extends AppCompatActivity {

    RippleView on,off;
    TinyDB tinydb;

    GpsTracker gps;

    double latitude;
    double longitude;

    TextView statue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tinydb = new TinyDB(getApplicationContext());

        statue = findViewById(R.id.statue);

        if(tinydb.getString("statue").equals("On"))
        {
            statue.setText("On");
            statue.setTextColor(Color.parseColor("#76FF03"));

        }else if(tinydb.getString("statue").equals("Off"))
        {
            statue.setText("Off");
            statue.setTextColor(Color.parseColor("#FF2C00"));
        }else
            {
                statue.setText("");
            }

        on = findViewById(R.id.on);
        off = findViewById(R.id.off);

        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isOnline()) {

                    if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    {
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                    }else
                        {

                            startService(new Intent(getApplicationContext(), LocationService.class));

                            /*gps = new GpsTracker(getApplicationContext(), MainActivity.this);

                            if (gps.canGetLocation()) {

                                latitude = gps.getLatitude();
                                longitude = gps.getLongitude();


                                showMessage("location is "+latitude+" and "+longitude);

                            }else
                            {
                                gps.showSettingsAlert();
                            }*/

                            tinydb.putString("statue","On");
                            statue.setText("On");
                            statue.setTextColor(Color.parseColor("#76FF03"));
                        }

                }else
                    {
                        showMessage("No Connection");
                    }
            }
        });

        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopService(new Intent(getApplicationContext(),LocationService.class));
                tinydb.putString("statue","Off");
                statue.setText("Off");
                statue.setTextColor(Color.parseColor("#FF2C00"));
            }
        });
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the

                    // contacts-related task you need to do.

                    startService(new Intent(getApplicationContext(), LocationService.class));

                    /*gps = new GpsTracker(getApplicationContext(), MainActivity.this);

                    if (gps.canGetLocation()) {

                        latitude = gps.getLatitude();
                        longitude = gps.getLongitude();


                        showMessage("location is "+latitude+" and "+longitude);

                    }else
                    {
                        gps.showSettingsAlert();
                    }*/

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.

                    //showMessage("You need to grant permission");
                }
                return;
            }
        }


    }










    public void onBackPressed(){
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }


    private void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        assert cm != null;
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

    private void showGPSDisabledAlertToUser(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("GPS is disabled in your device. Would you like to enable it?")
                .setCancelable(false)
                .setPositiveButton("Goto Settings Page To Enable GPS",
                        new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialog, int id){
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int id){
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


}
