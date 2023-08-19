package com.xtracover.xcqcmh.TestActivities;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.LocationFinder;
import com.xtracover.xcqcmh.Utilities.UserSession;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GPSTestActivity extends AppCompatActivity implements View.OnClickListener, LocationListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, gpsCheckImg;
    private LocationFinder finder;
    private static final int MY_LOCATION_PERMISSION_CODE = 101;
    private double latitude = 0.0, longitude = 0.0;
    private String address, city, state, postalCode, currentLocation = "";
    private Runnable runnable;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpstest);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();
        finder = new LocationFinder(mContext);

        getLayoutUiIdFinds();

        getCountGpsTest();

    }

    private void getChangedNotificationColor() {
        try {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(mContext, R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getCountGpsTest() {
        try {
            countDownTimer = new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                    onRestart();
                }

                public void onFinish() {
                    if (!currentLocation.equalsIgnoreCase("")) {
                        gpsCheckImg.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "GPS test pass!", Toast.LENGTH_SHORT).show();
                    } else {
                        gpsCheckImg.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, "GPS test fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getLayoutUiIdFinds() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            gpsCheckImg = (ImageView) findViewById(R.id.gpsCheckImg);

            backImg.setOnClickListener(this);
            checkImg.setOnClickListener(this);

        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backImg:
                onBackPressed();
                break;

            case R.id.checkImg:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finder.stopUsingGPS();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getCurrentAddressOfUser();
                finder.getLocation();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        finder.stopUsingGPS();
    }

    @Override
    protected void onStop() {
        super.onStop();
        finder.stopUsingGPS();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finder.stopUsingGPS();
    }

    private void getCurrentAddressOfUser() {
        try {
            finder = new LocationFinder(mContext);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mContext.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && mContext.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_LOCATION_PERMISSION_CODE);
                } else {    // Check if GPS enabled
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (finder.canGetLocation()) {
                                Location location = finder.getLocation();
                                latitude = finder.getLatitude();
                                longitude = finder.getLongitude();
//                                System.out.println("Address by Network or GPS GPSTracker :- " + location);
                                if (location != null) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        getAddress(mContext, latitude, longitude);
                                    }
                                }
                            } else {
                                finder.showSettingsAlert();
                            }
                        }
                    });
                }
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {
                address = addresses.get(0).getAddressLine(0);
                city = addresses.get(0).getLocality();
                state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.d("Address list :- ", String.valueOf(addresses.size()));

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!address.equalsIgnoreCase("") || !(address == null)) {
                            System.out.println("Address by Network or GPS :- " + address);
                            currentLocation = address;
                        } else {
                            System.out.println("Not found your location!");
                        }
                        refreshCurrentAddressOfUser(5000);
                    }
                });
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }

    private void refreshCurrentAddressOfUser(int milliseconds) {
        Handler handler = new Handler(mContext.getMainLooper());
        runnable = new Runnable() {
            @Override
            public void run() {
                getCurrentAddressOfUser();
            }
        };
        handler.postDelayed(runnable, milliseconds);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLocationChanged(@NonNull Location location) {
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        double speed = location.getSpeed();
        speed = (speed * 3600) / 1000;
        if (location != null) {
            getAddress(mContext, latitude, longitude);
        }
    }
}