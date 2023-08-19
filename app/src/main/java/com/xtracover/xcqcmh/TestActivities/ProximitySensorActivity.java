package com.xtracover.xcqcmh.TestActivities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class ProximitySensorActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, proximityCheckImg;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    private CountDownTimer countDownTimer;
    private boolean proximityNearAway;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_proximity_sensor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();
        getLayoutUiIdFinds();

        backImg.setOnClickListener(this);
        checkImg.setOnClickListener(this);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);

        getCountProximityTest();
    }

    private void getCountProximityTest() {
        try {
            countDownTimer = new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                    if (proximitySensor == null) {
                        Toast.makeText(mContext, "No proximity sensor found in device.", Toast.LENGTH_SHORT).show();
                    } else {
                        sensorManager.registerListener(proximitySensorEventListener, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
                    }
                }

                public void onFinish() {
                    if (proximityNearAway == true) {
                        proximityCheckImg.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "Proximity sensor test pass!", Toast.LENGTH_SHORT).show();
                    } else {
                        proximityCheckImg.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, "Proximity sensor test fail!", Toast.LENGTH_SHORT).show();
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
            proximityCheckImg = (ImageView) findViewById(R.id.proximityCheckImg);

        } catch (Exception exp) {
            exp.getStackTrace();
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backImg:
                onBackPressed();
                break;

            case R.id.checkImg:
                Toast.makeText(mContext, "In progress working for that", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    // calling the sensor event class to detect
    // the change in data when sensor starts working.
    SensorEventListener proximitySensorEventListener = new SensorEventListener() {
        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
            // method to check accuracy changed in sensor.
        }

        @Override
        public void onSensorChanged(SensorEvent event) {
            // check if the sensor type is proximity sensor.
            if (event.sensor.getType() == Sensor.TYPE_PROXIMITY) {
                if (event.values[0] == 0) {
                    proximityNearAway = true;
                    Toast.makeText(mContext, "Near", Toast.LENGTH_SHORT).show();
                } else {
                    System.out.println("Away");
                    proximityNearAway = false;
                }
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}