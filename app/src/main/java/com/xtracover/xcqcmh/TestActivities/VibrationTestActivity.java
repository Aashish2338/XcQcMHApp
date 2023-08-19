package com.xtracover.xcqcmh.TestActivities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class VibrationTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, vibrationCheckImg;
    private Vibrator vibrator;
    private VibrationEffect vibrationEffect;
    private CountDownTimer countDownTimer;
    private boolean vibrated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();

        getLayoutUiIdFinds();
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        getCountVibrationTest();

    }

    private void getCountVibrationTest() {
        try {
            countDownTimer = new CountDownTimer(2000, 1000) {

                @SuppressLint("MissingPermission")
                public void onTick(long millisUntilFinished) {
                    vibrate();
                }

                public void onFinish() {
                    if (vibrated == true) {
                        if (vibrator.hasVibrator()) {
                            vibrator.cancel();
                        }
                        vibrationCheckImg.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "Vibration test pass!", Toast.LENGTH_SHORT).show();
                    } else {
                        vibrationCheckImg.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, "Vibration test fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void vibrate() {
        try {
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(250, VibrationEffect.DEFAULT_AMPLITUDE));
                    vibrated = true;
                } else {
                    vibrated = true;
                    vibrator.vibrate(250);
                }
            } else {
                vibrated = false;
                Toast.makeText(getApplicationContext(), "Vibrator Not Present", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getLayoutUiIdFinds() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            vibrationCheckImg = (ImageView) findViewById(R.id.vibrationCheckImg);

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

            case R.id.checkImg:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}