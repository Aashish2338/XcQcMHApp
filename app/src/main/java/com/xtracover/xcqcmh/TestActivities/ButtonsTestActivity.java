package com.xtracover.xcqcmh.TestActivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class ButtonsTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, powerButtonCheckImg, volumeUpCheckImg, volumeDownCheckImg;
    private ImageView backButtonCheckImg, autoSwitchButtonCheckImg;
    private AppCompatButton retryTest_btn;
    private RelativeLayout powerButtonTest, volumeUpTest, volumeDownTest, backButtonTest, autoSwitchButtonTest;
    private boolean recentPressed = false;
    private ScreenStateReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();
        getLayoutUiIdFind();

        backImg.setOnClickListener(this);
        checkImg.setOnClickListener(this);
        retryTest_btn.setOnClickListener(this);

        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        mReceiver = new ScreenStateReceiver();
        registerReceiver(mReceiver, intentFilter);

    }

    private void getLayoutUiIdFind() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            powerButtonCheckImg = (ImageView) findViewById(R.id.powerButtonCheckImg);
            volumeUpCheckImg = (ImageView) findViewById(R.id.volumeUpCheckImg);
            volumeDownCheckImg = (ImageView) findViewById(R.id.volumeDownCheckImg);
            backButtonCheckImg = (ImageView) findViewById(R.id.backButtonCheckImg);
            autoSwitchButtonCheckImg = (ImageView) findViewById(R.id.autoSwitchButtonCheckImg);
            retryTest_btn = (AppCompatButton) findViewById(R.id.retryTest_btn);
            powerButtonTest = (RelativeLayout) findViewById(R.id.powerButtonTest);
            volumeUpTest = (RelativeLayout) findViewById(R.id.volumeUpTest);
            volumeDownTest = (RelativeLayout) findViewById(R.id.volumeDownTest);
            backButtonTest = (RelativeLayout) findViewById(R.id.backButtonTest);
            autoSwitchButtonTest = (RelativeLayout) findViewById(R.id.autoSwitchButtonTest);

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
                Toast.makeText(mContext, "In Progress works!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.retryTest_btn:
                Toast.makeText(mContext, "In Progress works for retry!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    class InnerRecevier extends BroadcastReceiver {

        final String SYSTEM_DIALOG_REASON_KEY = "reason";
        final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (Intent.ACTION_CLOSE_SYSTEM_DIALOGS.equals(action)) {
                    String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                    if (reason != null) {
                        if (reason.equals(SYSTEM_DIALOG_REASON_HOME_KEY)) {
                            recentPressed = true;
                        }
                    }
                }
            } catch (Exception exp) {
                exp.getStackTrace();
                System.out.println("Buttons test Exception 1 :- " + exp.getMessage());
            }
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        try {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                recentPressed = true;
                Log.d("Test1", "Back button pressed!");
                System.out.println("Back Key Result :- " + "Back button pressed!");
                backButtonCheckImg.setVisibility(View.VISIBLE);
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
                recentPressed = true;
                Log.d("Test2", "VolumeUp");
                System.out.println("Volume Up Button Result :- " + "VolumeUp");
                volumeUpCheckImg.setVisibility(View.VISIBLE);
            } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
                recentPressed = true;
                Log.d("Test3", "VolumeDown");
                System.out.println("Volume Down Button Result :- " + "VolumeDown");
                volumeDownCheckImg.setVisibility(View.VISIBLE);
            } else if (keyCode == KeyEvent.KEYCODE_POWER) {
                recentPressed = true;
                Log.d("Tes4t", "Power_Key");
                System.out.println("Power Key Button Result :- " + "Power_Key");
                powerButtonCheckImg.setVisibility(View.VISIBLE);
            }
        } catch (Exception exp) {
            exp.getStackTrace();
            System.out.println("Buttons test Exception 2 :- " + exp.getMessage());
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onUserLeaveHint() {
        super.onUserLeaveHint();
        try {
            if (recentPressed) {
                Log.d("Home_Key", "Home_Key pressed!");
                System.out.println("Home_Key Result :- " + "Home_Key pressed!");
                autoSwitchButtonCheckImg.setVisibility(View.VISIBLE);
            }
        } catch (Exception exp) {
            exp.getStackTrace();
            System.out.println("Buttons test Exception 3 :- " + exp.getMessage());
        }
    }

    public class ScreenStateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String action = intent.getAction();
                if (Intent.ACTION_SCREEN_ON.equals(action) || Intent.ACTION_SCREEN_OFF.equals(action)) {
                    Log.d("Power Button Test:", "Power Button Pressed");
                    System.out.println("Power_Key Result :- " + "Power Button Pressed");
                    powerButtonCheckImg.setVisibility(View.VISIBLE);
                }
            } catch (Exception exp) {
                exp.getStackTrace();
                System.out.println("Buttons test Exception 4 :- " + exp.getMessage());
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (recentPressed == true) {
            recentPressed = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }
}