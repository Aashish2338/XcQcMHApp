package com.xtracover.xcqcmh.TestActivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.cooltechworks.views.ScratchImageView;
import com.xtracover.xcqcmh.Activities.DashboardActivity;
import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class LcdPixelTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private CountDownTimer countDownTimer;
    private int counter = 0;
    private AppCompatButton failPixelBtn, passPixelBtn;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHideSystemUI();
        setContentView(R.layout.activity_lcd_pixel_test);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();
        failPixelBtn = (AppCompatButton) findViewById(R.id.failPixelBtn);
        passPixelBtn = (AppCompatButton) findViewById(R.id.passPixelBtn);

        failPixelBtn.setOnClickListener(this);
        passPixelBtn.setOnClickListener(this);

        getLCDPixelTestCount();
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

    private void getLCDPixelTestCount() {
        try {
            countDownTimer = new CountDownTimer(0, 0) {
                @Override
                public void onTick(long millisUntilFinished) {
                    int seconds = ((int) (millisUntilFinished / 1000)) % 60;
                }

                @Override
                public void onFinish() {
                    startScreenRedTest();
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void startScreenRedTest() {
        try {
            counter = 0;
            View localView = new View(mContext);
            localView.setBackgroundColor(getResources().getColor(R.color.Red));
            localView.setKeepScreenOn(true);
            setContentView(localView);

            localView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((counter == 0) || (counter == 3)) {
                        counter = counter + 1;
                        screenTestGreenColor();
                        Log.d("counter", String.valueOf(counter));
                    } else {
                        Log.d("counter", String.valueOf(counter));
                    }
                }
            });
//            new Handler().postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if ((counter == 0) || (counter == 3)) {
//                        counter = counter + 1;
//                        screenTestGreenColor();
//                        Log.d("counter", String.valueOf(counter));
//                    } else {
//                        Log.d("counter", String.valueOf(counter));
//                    }
//                }
//            }, 1500);
        } catch (Exception exp) {
            Log.d("Screen Red Color", " Test :- " + exp.toString());
            exp.getStackTrace();
        }
    }

    private void screenTestGreenColor() {
        try {
            View localView = new View(mContext);
            localView.setBackgroundColor(getResources().getColor(R.color.Green));
            localView.setKeepScreenOn(true);
            setContentView(localView);

            localView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (counter == 1) {
                        counter = counter + 1;
                        Log.d("counter", String.valueOf(counter));
                        screenTestBlueColor();
                    } else {
                        Log.d("counter", String.valueOf(counter));
                    }
                }
            });
        } catch (Exception exp) {
            Log.d("Screen Green Color", " Test :- " + exp.toString());
            exp.getStackTrace();
        }
    }

    private void screenTestBlueColor() {
        try {
            View localView = new View(mContext);
            localView.setBackgroundColor(getResources().getColor(R.color.Blue));
            localView.setKeepScreenOn(true);
            setContentView(localView);

            localView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (counter == 2) {
                        counter = counter + 1;
                        Log.d("counter", String.valueOf(counter));
                        getAlertForLcdPixel();
                    } else {
                        Log.d("counter", String.valueOf(counter));
                    }
                }
            });
        } catch (Exception exp) {
            Log.d("Screen Blue Color", " Test :- " + exp.toString());
            exp.getStackTrace();
        }
    }

    private void startWhiteScreenTest() {
        try {
            final View localView = new View(mContext);
            localView.setBackgroundColor(getResources().getColor(R.color.white));
            localView.setKeepScreenOn(true);
            setContentView(localView);

            new Handler().postDelayed(new Runnable() {
                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void run() {
                    if (counter == 3) {
                        counter = counter + 1;
                        getAlertForLcdPixel();
                    } else {
                        Log.d("counter", String.valueOf(counter));
                    }
                }
            }, 1500);
        } catch (Exception exp) {
            Log.d("Screen White Color", " Test :- " + exp.toString());
            exp.getStackTrace();
        }
    }

    private void getAlertForLcdPixel() {
        try {
            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.vibrate_item_layout);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;
            WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
            wmlp.gravity = Gravity.BOTTOM | Gravity.CENTER;

            TextView yesVibrate = (TextView) dialog.findViewById(R.id.yesVibrate);
            TextView noVibrate = (TextView) dialog.findViewById(R.id.noVibrate);
            TextView titleTxt = (TextView) dialog.findViewById(R.id.titleTxt);
            TextView descriptionTxt = (TextView) dialog.findViewById(R.id.descriptionTxt);


            titleTxt.setText("LCD Pixel Test");
            descriptionTxt.setText("Did you find any dot, spot or defect on the display?");

            yesVibrate.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    userSession.setLCDPixel("1");
                    dialog.dismiss();
                    onBackPressed();
                }
            });

            noVibrate.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    userSession.setLCDPixel("0");
                    dialog.dismiss();
                    onBackPressed();
                }
            });

            dialog.show();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.failPixelBtn:
                userSession.setLCDPixel("0");
                onBackPressed();
                break;

            case R.id.passPixelBtn:
                if ((counter == 0) || (counter == 2)) {
                    counter = counter + 1;
                    Log.d("counter First :- ", String.valueOf(counter));
                    screenTestGreenColor();
                } else if (counter == 1) {
                    counter = counter + 1;
                    screenTestBlueColor();
                    Log.d("counter Second :-", String.valueOf(counter));
                } else if (counter == 2) {
                    counter = counter + 1;
                    userSession.setLCDPixel("1");
                    onBackPressed();
                    Log.d("counter Third :- ", String.valueOf(counter));
                }
                break;
        }
    }
}