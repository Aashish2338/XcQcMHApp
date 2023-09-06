package com.xtracover.xcqcmh.TestActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.FrontCameraPreview;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class FrontCameraTestActivity extends AppCompatActivity {

    private Context mContext;
    private UserSession userSession;
    private SurfaceView surfaceView;
    private ImageView ivCapture;
    private FrontCameraPreview cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_front_camera_test);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();

        getLayoutUiIdFinds();
        getCountFrontCameraTest();

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

    private void getCountFrontCameraTest() {
        try {
            new CountDownTimer(5000, 1000) {

                @Override
                public void onTick(long millisUntilFinished) {
                    cameraPreview = new FrontCameraPreview(mContext, surfaceView);
                    cameraPreview.init();
                }

                @Override
                public void onFinish() {
                    setFrontCameraImage();
                    if (userSession.getFrontCamera().equalsIgnoreCase("1")) {
                        userSession.setFrontCamera("1");
                        onBackPressed();
                    } else if (userSession.getFrontCamera().equalsIgnoreCase("0")) {
                        userSession.setFrontCamera("0");
                        onBackPressed();
                    } else {
                        userSession.setFrontCamera("-1");
                        onBackPressed();
                    }
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void setFrontCameraImage() {
        try {
            if (!userSession.getFrontCameraImage().equalsIgnoreCase("")) {
                surfaceView.setVisibility(View.GONE);
                ivCapture.setVisibility(View.VISIBLE);
                ivCapture.setImageURI(Uri.parse(userSession.getFrontCameraImage().toString()));
            } else {
                surfaceView.setVisibility(View.VISIBLE);
                ivCapture.setVisibility(View.GONE);
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getLayoutUiIdFinds() {
        try {
            surfaceView = (SurfaceView) findViewById(R.id.surfaceView);
            ivCapture = (ImageView) findViewById(R.id.ivCapture);

        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}