package com.xtracover.xcqcmh.TestActivities;

import static java.lang.Math.sqrt;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class AccelerometerTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, aclmtrCheckImg;
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private float[] mGravity;
    private float mAccel;
    private float mAccelCurrent;
    private float mAccelLast;
    private boolean aclmtrresults;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accelerometer_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();

        getLayoutUiIdFinds();

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        getCountAccelerometerTest();

    }

    private void getCountAccelerometerTest() {
        try {
            countDownTimer = new CountDownTimer(2000, 1000) {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                public void onTick(long millisUntilFinished) {
                    mSensorManager.registerListener(accelerometerSensor, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
                }

                public void onFinish() {
                    if (aclmtrresults == true) {
                        aclmtrCheckImg.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "Yes Accelerometer available!", Toast.LENGTH_SHORT).show();
                    } else {
                        aclmtrCheckImg.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, "Accelerometer is not supported!", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    SensorEventListener accelerometerSensor = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity = event.values.clone();
                // Shake detection
                float x = mGravity[0];
                float y = mGravity[1];
                float z = mGravity[2];
                mAccelLast = mAccelCurrent;
                mAccelCurrent = (float) sqrt(x * x + y * y + z * z);
                float delta = mAccelCurrent - mAccelLast;
                mAccel = mAccel * 0.9f + delta;
                if (mAccel > 3) {  // do something
                    aclmtrresults = true;
                } else {
                    aclmtrresults = false;
                }
            }
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };

    private void getLayoutUiIdFinds() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            aclmtrCheckImg = (ImageView) findViewById(R.id.aclmtrCheckImg);

            backImg.setOnClickListener(this);
            checkImg.setOnClickListener(this);

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
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(accelerometerSensor, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(accelerometerSensor);
    }
}