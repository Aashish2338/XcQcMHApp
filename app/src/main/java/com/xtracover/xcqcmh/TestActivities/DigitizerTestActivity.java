package com.xtracover.xcqcmh.TestActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.cooltechworks.views.ScratchImageView;
import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class DigitizerTestActivity extends AppCompatActivity {

    private Context mContext;
    private UserSession userSession;
    private ScratchImageView scratchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHideSystemUI();
        setContentView(R.layout.activity_digitizer_test);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();
        scratchView = (ScratchImageView) findViewById(R.id.scratchViews);
        getLcdGlassTest();

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

    private void getLcdGlassTest() {
        try {
            scratchView.setRevealListener(new ScratchImageView.IRevealListener() {
                @Override
                public void onRevealed(ScratchImageView iv) {
                    if (iv.isRevealed()) {
                        System.out.println("LCD pixel pass!");
                        Toast.makeText(mContext, "Lcd pixel pass!", Toast.LENGTH_LONG).show();
                    } else {
                        System.out.println("LCD pixel fail!");
                        Toast.makeText(mContext, "Lcd pixel fail!", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onRevealPercentChangedListener(ScratchImageView siv, float percent) {
                    System.out.println("Scratched percentage :- " + percent);
                    if (percent >= 0.9999043 || percent >= 0.99952066) { //0.99952066
                        System.out.println("Lcd pixel pass!");
                        Toast.makeText(mContext, "LCD pixel pass!", Toast.LENGTH_LONG).show();
                        userSession.setLCDGlass("1");
                        getBackCountTime();
                    } else {
                        System.out.println("Lcd pixel fail!");
                        userSession.setLCDGlass("0");
                    }
                }
            });
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getBackCountTime() {
        try {
            new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    onBackPressed();
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void setHideSystemUI() {
        try {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            } else {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_IMMERSIVE | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}