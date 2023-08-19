package com.xtracover.xcqcmh.TestActivities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class ScreenTouchActivity extends AppCompatActivity {

    private Context mContext;
    private UserSession userSession;
    private LinearLayout layoutMain, layoutButtons;
    private Button b;
    private WindowManager.LayoutParams param, layoutParams;
    private int btnCounter, flag;
    private String[] buttons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_touch);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();
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

    private void fillScreen() {
        try {
            for (int i = 1; i < 14; i++) {
                layoutMain = new LinearLayout(getApplicationContext());
                for (int j = 1; j < 14; j++) {
                    b = new Button(getApplicationContext());
                    b.setTag("btn" + i + j);
                    b.setBackgroundColor(getResources().getColor(R.color.white));
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        b.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_test_drwble_green));
                    } else {
                        b.setBackground(getResources().getDrawable(R.drawable.bg_test_drwble_green));
                    }
//                    b.setOnTouchListener(getOnClickDoSomething(b));

                    b.setLayoutParams(param);
                    layoutMain.addView(b);

                    if (i == j || j == (15 - i - 1) || j == 13 || j == 1 || i == 1 || i == 13) {
                        b.setVisibility(View.VISIBLE);
                    } else {
                        b.setVisibility(View.INVISIBLE);
                    }
                }
                layoutMain.setLayoutParams(layoutParams);
                layoutButtons.addView(layoutMain);
            }
        } catch (Exception e) {
            System.out.println("promptMessage,RotationActivity Exception" + e.toString());
        }

    }

    /*View.OnTouchListener getOnClickDoSomething(final Button btn) {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (!buttons.contains((CharSequence) btn.getTag())) {
                    buttons.add(btn.getTag());
                    btnCounter++;
                }
                btn.setBackgroundColor(getResources().getColor(R.color.bg_color_green));
                if ((btnCounter) == 67) {
                    flag++;
                    if (flag == 2) {
                        Toast.makeText(mContext, "Touch Screen test pass?", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        };
    }*/
}