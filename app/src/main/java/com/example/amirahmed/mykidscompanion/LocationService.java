package com.example.amirahmed.mykidscompanion;


import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class LocationService extends Service {

    double latitude;
    double longitude;

    GpsTracker gps;

    TimerTask hourTask;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

         Timer timer = new Timer();
                hourTask = new TimerTask() {
                    @Override
                    public void run() {

                        Handler handler = new Handler(Looper.getMainLooper());
                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                gps = new GpsTracker(getApplicationContext());

                                if (gps.canGetLocation()) {

                                    latitude = gps.getLatitude();
                                    longitude = gps.getLongitude();


                                    showMessage("location is "+latitude+"and "+longitude);

                                }


                            }
                        });


                    }
                };
                timer.schedule(hourTask, 0L, 1000 * 10);



        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        hourTask.cancel();
        super.onDestroy();
    }

    private void showMessage(String _s) {
        Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
    }

}
