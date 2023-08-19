package com.xtracover.xcqcmh.Activities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.TestActivities.AccelerometerTestActivity;
import com.xtracover.xcqcmh.TestActivities.BluetoothTestActivity;
import com.xtracover.xcqcmh.TestActivities.ButtonsTestActivity;
import com.xtracover.xcqcmh.TestActivities.FlashTestActivity;
import com.xtracover.xcqcmh.TestActivities.GPSTestActivity;
import com.xtracover.xcqcmh.TestActivities.MicrophoneTestActivity;
import com.xtracover.xcqcmh.TestActivities.NFCTestActivity;
import com.xtracover.xcqcmh.TestActivities.ProximitySensorActivity;
import com.xtracover.xcqcmh.TestActivities.VibrationTestActivity;
import com.xtracover.xcqcmh.TestActivities.WiFiInternetTestActivity;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView refreshImg, battaryImg, checkImg;
    private AppCompatButton startTest_btn;
    private LinearLayout wifiTest, gpsTest, bluetoothTest, proximitySensorTest, buttonsTest, vibrationTest, accelerometerTest;
    private LinearLayout microphoneTest, touchTest, cameraTest, flashTest, upperLowerSpeakerTest, headphoneJackTest;
    private LinearLayout nfcTest, brightnessSensorTest, forceTouchTest, touchIdTest, lCDTest, backlightTest, backlightSpotsTest;
    private LinearLayout rightAndLeftChannelTest, operatorBlockCheckTest, simCardReaderTest, voiceCallTest, batteryDrainTest;
    private LinearLayout trueDepthFaceIDTest, burntPixelsTest;
    private static BluetoothAdapter mBluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();
        getLayoutUiIdFinds();

        refreshImg.setOnClickListener(this);
        battaryImg.setOnClickListener(this);
        checkImg.setOnClickListener(this);
        startTest_btn.setOnClickListener(this);
        wifiTest.setOnClickListener(this);
        gpsTest.setOnClickListener(this);
        bluetoothTest.setOnClickListener(this);
        proximitySensorTest.setOnClickListener(this);
        buttonsTest.setOnClickListener(this);
        vibrationTest.setOnClickListener(this);
        accelerometerTest.setOnClickListener(this);
        microphoneTest.setOnClickListener(this);
        touchTest.setOnClickListener(this);
        cameraTest.setOnClickListener(this);
        flashTest.setOnClickListener(this);
        upperLowerSpeakerTest.setOnClickListener(this);
        headphoneJackTest.setOnClickListener(this);
        nfcTest.setOnClickListener(this);
        brightnessSensorTest.setOnClickListener(this);
        forceTouchTest.setOnClickListener(this);
        touchIdTest.setOnClickListener(this);
        lCDTest.setOnClickListener(this);
        backlightTest.setOnClickListener(this);
        backlightSpotsTest.setOnClickListener(this);
        rightAndLeftChannelTest.setOnClickListener(this);
        operatorBlockCheckTest.setOnClickListener(this);
        simCardReaderTest.setOnClickListener(this);
        voiceCallTest.setOnClickListener(this);
        batteryDrainTest.setOnClickListener(this);
        trueDepthFaceIDTest.setOnClickListener(this);
        burntPixelsTest.setOnClickListener(this);

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

    private void getLayoutUiIdFinds() {
        try {
            refreshImg = (ImageView) findViewById(R.id.refreshImg);
            battaryImg = (ImageView) findViewById(R.id.battaryImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            startTest_btn = (AppCompatButton) findViewById(R.id.startTest_btn);
            wifiTest = (LinearLayout) findViewById(R.id.wifiTest);
            gpsTest = (LinearLayout) findViewById(R.id.gpsTest);
            bluetoothTest = (LinearLayout) findViewById(R.id.bluetoothTest);
            proximitySensorTest = (LinearLayout) findViewById(R.id.proximitySensorTest);
            buttonsTest = (LinearLayout) findViewById(R.id.buttonsTest);
            vibrationTest = (LinearLayout) findViewById(R.id.vibrationTest);
            accelerometerTest = (LinearLayout) findViewById(R.id.accelerometerTest);
            microphoneTest = (LinearLayout) findViewById(R.id.microphoneTest);
            touchTest = (LinearLayout) findViewById(R.id.touchTest);
            cameraTest = (LinearLayout) findViewById(R.id.cameraTest);
            flashTest = (LinearLayout) findViewById(R.id.flashTest);
            upperLowerSpeakerTest = (LinearLayout) findViewById(R.id.upperLowerSpeakerTest);
            headphoneJackTest = (LinearLayout) findViewById(R.id.headphoneJackTest);
            nfcTest = (LinearLayout) findViewById(R.id.nfcTest);
            brightnessSensorTest = (LinearLayout) findViewById(R.id.brightnessSensorTest);
            forceTouchTest = (LinearLayout) findViewById(R.id.forceTouchTest);
            touchIdTest = (LinearLayout) findViewById(R.id.touchIdTest);
            lCDTest = (LinearLayout) findViewById(R.id.lCDTest);
            backlightTest = (LinearLayout) findViewById(R.id.backlightTest);
            backlightSpotsTest = (LinearLayout) findViewById(R.id.backlightSpotsTest);
            rightAndLeftChannelTest = (LinearLayout) findViewById(R.id.rightAndLeftChannelTest);
            operatorBlockCheckTest = (LinearLayout) findViewById(R.id.operatorBlockCheckTest);
            simCardReaderTest = (LinearLayout) findViewById(R.id.simCardReaderTest);
            voiceCallTest = (LinearLayout) findViewById(R.id.voiceCallTest);
            batteryDrainTest = (LinearLayout) findViewById(R.id.batteryDrainTest);
            trueDepthFaceIDTest = (LinearLayout) findViewById(R.id.trueDepthFaceIDTest);
            burntPixelsTest = (LinearLayout) findViewById(R.id.burntPixelsTest);

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activityResultLauncher.launch(enableBtIntent);

        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @SuppressLint("MissingPermission")
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        mBluetoothAdapter.enable();
                        System.out.println("Request granted - bluetooth is turning on...");
                    } else {
                        System.out.println("Request not granted - bluetooth is turning on...");
                    }
                }
            });

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.refreshImg:
                Toast.makeText(mContext, "In progress refresh action!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.battaryImg:
                Toast.makeText(mContext, "In progress battery action!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.checkImg:
                userSession.logoutUser();
                DashboardActivity.this.finish();
                Toast.makeText(mContext, "Log out successfully!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.startTest_btn:
                Toast.makeText(mContext, "In progress start test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.wifiTest:
                startActivity(new Intent(mContext, WiFiInternetTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.gpsTest:
                startActivity(new Intent(mContext, GPSTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.bluetoothTest:
                startActivity(new Intent(mContext, BluetoothTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.proximitySensorTest:
                startActivity(new Intent(mContext, ProximitySensorActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.buttonsTest:
                startActivity(new Intent(mContext, ButtonsTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.vibrationTest:
                startActivity(new Intent(mContext, VibrationTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.accelerometerTest:
                startActivity(new Intent(mContext, AccelerometerTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.microphoneTest:
                startActivity(new Intent(mContext, MicrophoneTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.touchTest:
                Toast.makeText(mContext, "In Progress Touch test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.cameraTest:
                Toast.makeText(mContext, "In Progress Camera test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.flashTest:
                startActivity(new Intent(mContext, FlashTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.upperLowerSpeakerTest:
                Toast.makeText(mContext, "In Progress Upper and lower speaker test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.headphoneJackTest:
                Toast.makeText(mContext, "In Progress Headphone jack test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.nfcTest:
                startActivity(new Intent(mContext, NFCTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.brightnessSensorTest:
                Toast.makeText(mContext, "In Progress Brightness test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.forceTouchTest:
                Toast.makeText(mContext, "In progress force touch test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.touchIdTest:
                Toast.makeText(mContext, "In progress touch id test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.lCDTest:
                Toast.makeText(mContext, "In progress LCD test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.backlightTest:
                Toast.makeText(mContext, "In progress back light test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.backlightSpotsTest:
                Toast.makeText(mContext, "In progress back light spots test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rightAndLeftChannelTest:
                Toast.makeText(mContext, "In progress right and left channel test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.operatorBlockCheckTest:
                Toast.makeText(mContext, "In progress operator block check test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.simCardReaderTest:
                Toast.makeText(mContext, "In progress sim card reader test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.voiceCallTest:
                Toast.makeText(mContext, "In progress voice call test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.batteryDrainTest:
                Toast.makeText(mContext, "In progress battery drain test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.trueDepthFaceIDTest:
                Toast.makeText(mContext, "In progress tru depth face id test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.burntPixelsTest:
                Toast.makeText(mContext, "In progress burnt pixel test!", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}