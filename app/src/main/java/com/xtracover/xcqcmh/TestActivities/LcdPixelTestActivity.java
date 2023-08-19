package com.xtracover.xcqcmh.TestActivities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.cooltechworks.views.ScratchImageView;
import com.xtracover.xcqcmh.Activities.DashboardActivity;
import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class LcdPixelTestActivity extends AppCompatActivity {

    private Context mContext;
    private UserSession userSession;
    private ScratchImageView scratchView;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHideSystemUI();
        setContentView(R.layout.activity_lcd_pixel_test);
        mContext = this;
        userSession = new UserSession(mContext);

        scratchView = (ScratchImageView) findViewById(R.id.scratchView);

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
                    userSession.setLCDPixel("Pass");
                    getBackCountTime();
                } else {
                    System.out.println("Lcd pixel fail!");
                    userSession.setLCDPixel("Fail");
                }
            }
        });
    }

    private void getBackCountTime() {
        try {
            countDownTimer = new CountDownTimer(1000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    startActivity(new Intent(mContext, DashboardActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
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
}