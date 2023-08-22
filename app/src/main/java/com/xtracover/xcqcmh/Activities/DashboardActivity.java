package com.xtracover.xcqcmh.Activities;

import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.TestActivities.AccelerometerTestActivity;
import com.xtracover.xcqcmh.TestActivities.ButtonsTestActivity;
import com.xtracover.xcqcmh.TestActivities.FlashTestActivity;
import com.xtracover.xcqcmh.TestActivities.GPSTestActivity;
import com.xtracover.xcqcmh.TestActivities.LcdGlassTestActivity;
import com.xtracover.xcqcmh.TestActivities.LcdPixelTestActivity;
import com.xtracover.xcqcmh.TestActivities.MicrophoneTestActivity;
import com.xtracover.xcqcmh.TestActivities.NFCTestActivity;
import com.xtracover.xcqcmh.TestActivities.ProximitySensorActivity;
import com.xtracover.xcqcmh.TestActivities.RearCameraTestActivity;
import com.xtracover.xcqcmh.Utilities.UserSession;

import java.text.DecimalFormat;
import java.util.Set;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView refreshImg, battaryImg, checkImg;
    private AppCompatButton startTest_btn;
    private LinearLayout wifiTest, bluetoothTest, proximitySensorTest, buttonsTest, vibrationTest, frontCameraTest;
    private LinearLayout rearCameraTest, altraWideCameraTest, loudSpeakerTest, microphoneTest;
    private LinearLayout earpieceTest, lCDTest, lCDPixelTest, digitizerTest;
    private static BluetoothAdapter mBluetoothAdapter;
    private boolean bluetoothEnable;

    private WifiManager wifiManager;
    private boolean wifiEnabled = false, previousConnectivityStatus = false, mConnected = false;
    private ConnectivityManager connectionManager;
    private CountDownTimer countDownTimer;

    private Vibrator vibrator;
    private boolean vibrated;

    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private boolean speakerResults;

    private boolean earphoneResults, connectedEarphone;
    private MusicIntentReceiver myReceiver;

    // Currently not in use but if we need in case future then we will use it as per required
    private LinearLayout gpsTest, accelerometerTest, touchTest, forceTouchTest, touchIdTest, flashTest, backlightTest;
    private LinearLayout backlightSpotsTest, upperLowerSpeakerTest, headphoneJackTest, rightAndLeftChannelTest, nfcTest;
    private LinearLayout brightnessSensorTest, operatorBlockCheckTest, simCardReaderTest, voiceCallTest, batteryDrainTest;
    private LinearLayout trueDepthFaceIDTest, burntPixelsTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();
        getLayoutUiIdFinds();

        myReceiver = new MusicIntentReceiver();

        refreshImg.setOnClickListener(this);
        battaryImg.setOnClickListener(this);
        checkImg.setOnClickListener(this);
        startTest_btn.setOnClickListener(this);
        wifiTest.setOnClickListener(this);
        bluetoothTest.setOnClickListener(this);
        proximitySensorTest.setOnClickListener(this);
        buttonsTest.setOnClickListener(this);
        vibrationTest.setOnClickListener(this);
        microphoneTest.setOnClickListener(this);
        frontCameraTest.setOnClickListener(this);
        lCDTest.setOnClickListener(this);
        rearCameraTest.setOnClickListener(this);
        altraWideCameraTest.setOnClickListener(this);
        loudSpeakerTest.setOnClickListener(this);
        earpieceTest.setOnClickListener(this);
        lCDPixelTest.setOnClickListener(this);
        digitizerTest.setOnClickListener(this);

        if (userSession.getLCDPixel().equalsIgnoreCase("Pass")) {
            lCDPixelTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
        } else {
            lCDPixelTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
        }

        // Currently not in use but if we need in case future then we will use it as per required
        gpsTest.setOnClickListener(this);
        accelerometerTest.setOnClickListener(this);
        touchTest.setOnClickListener(this);
        forceTouchTest.setOnClickListener(this);
        touchIdTest.setOnClickListener(this);
        flashTest.setOnClickListener(this);
        backlightTest.setOnClickListener(this);
        backlightSpotsTest.setOnClickListener(this);
        upperLowerSpeakerTest.setOnClickListener(this);
        headphoneJackTest.setOnClickListener(this);
        rightAndLeftChannelTest.setOnClickListener(this);
        nfcTest.setOnClickListener(this);
        brightnessSensorTest.setOnClickListener(this);
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
            bluetoothTest = (LinearLayout) findViewById(R.id.bluetoothTest);
            proximitySensorTest = (LinearLayout) findViewById(R.id.proximitySensorTest);
            buttonsTest = (LinearLayout) findViewById(R.id.buttonsTest);
            vibrationTest = (LinearLayout) findViewById(R.id.vibrationTest);
            microphoneTest = (LinearLayout) findViewById(R.id.microphoneTest);
            frontCameraTest = (LinearLayout) findViewById(R.id.frontCameraTest);
            rearCameraTest = (LinearLayout) findViewById(R.id.rearCameraTest);
            lCDTest = (LinearLayout) findViewById(R.id.lCDTest);
            altraWideCameraTest = (LinearLayout) findViewById(R.id.altraWideCameraTest);
            loudSpeakerTest = (LinearLayout) findViewById(R.id.loudSpeakerTest);
            earpieceTest = (LinearLayout) findViewById(R.id.earpieceTest);
            lCDPixelTest = (LinearLayout) findViewById(R.id.lCDPixelTest);
            digitizerTest = (LinearLayout) findViewById(R.id.digitizerTest);

            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activityResultLauncher.launch(enableBtIntent);


            // Currently not in use but if we need in case future then we will use it as per required
            gpsTest = (LinearLayout) findViewById(R.id.gpsTest);
            accelerometerTest = (LinearLayout) findViewById(R.id.accelerometerTest);
            touchTest = (LinearLayout) findViewById(R.id.touchTest);
            forceTouchTest = (LinearLayout) findViewById(R.id.forceTouchTest);
            touchIdTest = (LinearLayout) findViewById(R.id.touchIdTest);
            flashTest = (LinearLayout) findViewById(R.id.flashTest);
            backlightTest = (LinearLayout) findViewById(R.id.backlightTest);
            backlightSpotsTest = (LinearLayout) findViewById(R.id.backlightSpotsTest);
            upperLowerSpeakerTest = (LinearLayout) findViewById(R.id.upperLowerSpeakerTest);
            headphoneJackTest = (LinearLayout) findViewById(R.id.headphoneJackTest);
            rightAndLeftChannelTest = (LinearLayout) findViewById(R.id.rightAndLeftChannelTest);
            nfcTest = (LinearLayout) findViewById(R.id.nfcTest);
            brightnessSensorTest = (LinearLayout) findViewById(R.id.brightnessSensorTest);
            operatorBlockCheckTest = (LinearLayout) findViewById(R.id.operatorBlockCheckTest);
            simCardReaderTest = (LinearLayout) findViewById(R.id.simCardReaderTest);
            voiceCallTest = (LinearLayout) findViewById(R.id.voiceCallTest);
            batteryDrainTest = (LinearLayout) findViewById(R.id.batteryDrainTest);
            trueDepthFaceIDTest = (LinearLayout) findViewById(R.id.trueDepthFaceIDTest);
            burntPixelsTest = (LinearLayout) findViewById(R.id.burntPixelsTest);

        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
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
                getCountTimeForWiFiTest();
                break;

            case R.id.bluetoothTest:
                getCountBluetoothTest();
                break;

            case R.id.proximitySensorTest:
                startActivity(new Intent(mContext, ProximitySensorActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.buttonsTest:
                startActivity(new Intent(mContext, ButtonsTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.vibrationTest:
                getCountVibrationTest();
                break;

            case R.id.frontCameraTest:
                Toast.makeText(mContext, "In Progress Front Camera test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.rearCameraTest:
                startActivity(new Intent(mContext, RearCameraTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.altraWideCameraTest:
                Toast.makeText(mContext, "In Progress Altra Wide Camera test!", Toast.LENGTH_SHORT).show();
                break;

            case R.id.loudSpeakerTest:
                getCountLoudSpeakerTestOfDevice();
                break;

            case R.id.microphoneTest:
                startActivity(new Intent(mContext, MicrophoneTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.earpieceTest:
                if (connectedEarphone) {
                    getCountEarphoneTestOfDevice();
                } else {
                    Toast.makeText(mContext, "Connect earphone please!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.lCDTest:
                startActivity(new Intent(mContext, LcdGlassTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.lCDPixelTest:
                startActivity(new Intent(mContext, LcdPixelTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.digitizerTest:
                Toast.makeText(mContext, "In Progress Digitizer test!", Toast.LENGTH_SHORT).show();
                break;


            // Currently not in use but if we need in case future then we will use it as per required
            case R.id.gpsTest:
                startActivity(new Intent(mContext, GPSTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                break;

            case R.id.accelerometerTest:
                startActivity(new Intent(mContext, AccelerometerTestActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
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

            case R.id.touchTest:
                Toast.makeText(mContext, "In Progress Touch test!", Toast.LENGTH_SHORT).show();
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

    private void getCountTimeForWiFiTest() {
        try {
            countDownTimer = new CountDownTimer(2000, 1000) {

                public void onTick(long millisUntilFinished) {
                    // Register Connectivity Receiver
                    IntentFilter intentFilter = new IntentFilter();
                    intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
                    mContext.registerReceiver(networkBroadcastReceiver, intentFilter);

                    // Register Wifi State Listener
                    IntentFilter wifiStateIntentFilter = new IntentFilter();
                    wifiStateIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
                    mContext.registerReceiver(wifiStateReceiver, wifiStateIntentFilter);

                    connectionManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
                    wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
                    getWiFiConnectivityStatus();

                }

                public void onFinish() {
                    if (wifiEnabled == true) {
                        wifiTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
                        userSession.setWiFi("1");
                    } else {
                        wifiTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
                        userSession.setWiFi("0");
                    }
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    // This code is WiFi test
    private void getWiFiConnectivityStatus() {
        try {
            wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
            if (wifiManager != null) {
                wifiManager.setWifiEnabled(true);
                wifiEnabled = wifiManager.isWifiEnabled();
                System.out.println("Wi-Fi Status :- " + wifiEnabled);
            } else {
                wifiManager.setWifiEnabled(false);
                wifiEnabled = wifiManager.isWifiEnabled();
                System.out.println("Wi-Fi Status :- " + wifiEnabled);
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private BroadcastReceiver networkBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("NetworkController.networkBroadcastReceiver.new BroadcastReceiver() {...}::onReceive");
            boolean connectivityStatus = isInternetConnectivityAvailable();
            if (previousConnectivityStatus != connectivityStatus) {
                if (connectivityStatus) {
                    previousConnectivityStatus = true;
                    System.out.println("Broadcast Internet Available");
                } else {
                    previousConnectivityStatus = false;
                    System.out.println("Broadcast Internet Disconnected");
                }
            }
        }
    };

    private BroadcastReceiver wifiStateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.out.println("NetworkController.wifiStateReceiver.new BroadcastReceiver() {...}::onReceive");
            int extraWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (extraWifiState) {
                case WifiManager.WIFI_STATE_DISABLED: {
                    System.out.println("Broadcast Wifi State Disabled");
                    if (wifiEnabled) {
                        System.out.println("Broadcast Wifi State Disabled");
                    }
                    break;
                }

                case WIFI_STATE_ENABLED: {
                    System.out.println("Broadcast Wifi State Enabled");
                    if (wifiEnabled) {
                        System.out.println("Broadcast Wifi State Enabled");
                    }
                    break;
                }
            }
        }
    };

    private boolean isInternetConnectivityAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    // Thhis code is Bluetooth test
    private void getCountBluetoothTest() {
        try {
            countDownTimer = new CountDownTimer(2000, 1000) {

                @SuppressLint("MissingPermission")
                public void onTick(long millisUntilFinished) {
                    if (mBluetoothAdapter.isEnabled()) {
                        bluetoothEnable = true;
                        System.out.println("Bluetooth enable A : -" + bluetoothEnable);
                        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                        registerReceiver(mReceiver, filter);
                    } else {
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        activityResultLauncher.launch(enableBtIntent);
                    }
                }

                public void onFinish() {
                    if (bluetoothEnable == true) {
                        bluetoothTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
                        userSession.setBluetooth("1");
                    } else {
                        bluetoothTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
                        userSession.setBluetooth("0");
                    }
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                System.out.println("Bluetooth result :- " + state);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        activityResultLauncherBl.launch(enableBtIntent);
                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Intent enableBtIntentA = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        activityResultLauncherBl.launch(enableBtIntentA);
                        break;

                    case BluetoothAdapter.STATE_ON:
                        enableBluetooth();  // Bluetooth is on
                        break;

                    case BluetoothAdapter.STATE_TURNING_ON:
                        enableBluetooth();  // Bluetooth is turning on
                        break;
                }
            }
        }
    };

    ActivityResultLauncher<Intent> activityResultLauncherBl = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
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

    @SuppressLint("MissingPermission")
    public void enableBluetooth() {
        if (!mBluetoothAdapter.isEnabled()) {
            mBluetoothAdapter.enable();
            bluetoothEnable = true;
            System.out.println("Bluetooth enable B: -" + bluetoothEnable);
        }
        if (mBluetoothAdapter.isEnabled()) {
            bluetoothEnable = true;
            System.out.println("Paired Devices");
            System.out.println("Bluetooth enable C: -" + bluetoothEnable);
            Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
            for (BluetoothDevice device : devices) {
                System.out.println("Paired device name list :- " + "\nDevice: " + device.getName() + ", " + device);
            }
        } else {
            //bluetooth is off so can't get paired devices
            bluetoothEnable = false;
            System.out.println("Bluetooth enable D: -" + bluetoothEnable);
            System.out.println("Turn ON Bluetooth to get paired devices");
        }
    }

    // This code is Vibration test
    private void getCountVibrationTest() {
        try {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            countDownTimer = new CountDownTimer(2000, 1000) {

                @SuppressLint("MissingPermission")
                public void onTick(long millisUntilFinished) {
                    vibrate();
                }

                public void onFinish() {
                    if (vibrator.hasVibrator()) {
                        vibrator.cancel();
                    }
                    if (vibrated) {
                        getAlertForVibrate(vibrated);
                    } else {
                        getAlertForVibrate(vibrated);
                    }
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getAlertForVibrate(boolean vibrated) {
        try {
            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.vibrate_item_layout);
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCanceledOnTouchOutside(true);
            dialog.getWindow().getAttributes().windowAnimations = R.style.animation;

            TextView yesVibrate = (TextView) dialog.findViewById(R.id.yesVibrate);
            TextView noVibrate = (TextView) dialog.findViewById(R.id.noVibrate);
            TextView titleTxt = (TextView) dialog.findViewById(R.id.titleTxt);
            TextView descriptionTxt = (TextView) dialog.findViewById(R.id.descriptionTxt);

            titleTxt.setText("Vibration Test");
            descriptionTxt.setText("Did you find any vibration in your device?");

            yesVibrate.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    vibrationTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
                    dialog.dismiss();
                    userSession.setVibration("1");
                }
            });

            noVibrate.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public void onClick(View v) {
                    vibrationTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
                    dialog.dismiss();
                    userSession.setVibration("0");
                }
            });

            dialog.show();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void vibrate() {
        try {
            if (vibrator.hasVibrator()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    vibrator.vibrate(VibrationEffect.createOneShot(300, VibrationEffect.DEFAULT_AMPLITUDE));
                    vibrated = true;
                } else {
                    vibrated = true;
                    vibrator.vibrate(300);
                }
            } else {
                vibrated = false;
                Toast.makeText(getApplicationContext(), "Vibrator Not Present", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    // This code is Loudspeaker test
    private void getCountLoudSpeakerTestOfDevice() {
        countDownTimer = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("Device loud speaker test!");
                audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null) {
                    audioManager.setMode(AudioManager.MODE_NORMAL);
                    if (!audioManager.isSpeakerphoneOn()) {
                        audioManager.setSpeakerphoneOn(true);
                    }
                    mediaPlayer = MediaPlayer.create(mContext, R.raw.notification_songs);
                    mediaPlayer.setLooping(false);
                    mediaPlayer.start();
                    if (mediaPlayer.isPlaying()) {
                        speakerResults = true;
                    } else {
                        speakerResults = false;
                    }
                } else {
                    speakerResults = false;
                }
            }

            @Override
            public void onFinish() {
                try {
                    mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        public void onCompletion(MediaPlayer mp) {
                            if (mediaPlayer != null) {
                                mediaPlayer.reset();
                                mediaPlayer.release();
                                mediaPlayer = null;
                            }
                        }
                    });
                } catch (Exception exp) {
                    exp.printStackTrace();
                    System.out.println("Device loud speaker test :- " + exp.getMessage());
                }
                if (speakerResults) {
                    loudSpeakerTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
                    userSession.setLoudSpeaker("1");
                } else {
                    loudSpeakerTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
                    userSession.setLoudSpeaker("0");
                }
            }
        }.start();
    }

    // This code is earpiece test
    private void getCountEarphoneTestOfDevice() {
        countDownTimer = new CountDownTimer(2000, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("Device ear phone test!");
                audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
                if (audioManager != null) {
                    if (audioManager.isWiredHeadsetOn()) {
                        audioManager.setMode(AudioManager.MODE_NORMAL);
                        if (!audioManager.isSpeakerphoneOn()) {
                            audioManager.setSpeakerphoneOn(false);
                            audioManager.setWiredHeadsetOn(true);
                        }
                        mediaPlayer = MediaPlayer.create(mContext, R.raw.notification_songs);
                        mediaPlayer.setLooping(false);
                        mediaPlayer.start();
                        if (mediaPlayer.isPlaying()) {
                            earphoneResults = true;
                        } else {
                            earphoneResults = false;
                        }
                    } else {
                        Toast.makeText(mContext, "Earphone is not connected!", Toast.LENGTH_SHORT).show();
                        earphoneResults = false;
                    }
                } else {
                    earphoneResults = false;
                }
            }

            @Override
            public void onFinish() {
                mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    public void onCompletion(MediaPlayer mp) {
                        if (mediaPlayer != null) {
                            mediaPlayer.reset();
                            mediaPlayer.release();
                            mediaPlayer = null;
                        }
                    }
                });
                if (earphoneResults) {
                    earpieceTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
                    userSession.setEarpiece("1");
                } else {
                    earpieceTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
                    userSession.setEarpiece("0");
                }
            }
        }.start();
    }

    private class MusicIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_HEADSET_PLUG)) {
                int state = intent.getIntExtra("state", -1);
                switch (state) {
                    case 0:
                        connectedEarphone = false;
                        System.out.println("Earphone is not connected!");
                        break;
                    case 1:
                        System.out.println("Earphone is connected!");
                        connectedEarphone = true;
                        break;
                    default:
                        connectedEarphone = false;
                        System.out.println("I have no idea what the headset state is!");
                        Toast.makeText(context, "I have no idea what the headset state is!", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (mBluetoothAdapter.isEnabled()) {
                bluetoothEnable = true;
                System.out.println("Bluetooth enable A : -" + bluetoothEnable);
                IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
                registerReceiver(mReceiver, filter);
            } else {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                activityResultLauncher.launch(enableBtIntent);
            }

            IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
            registerReceiver(myReceiver, filter);

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    getTestedDataForValidate();
                }
            });
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getTestedDataForValidate() {
        try {
            if (userSession.getWiFi().equalsIgnoreCase("1")) {
                wifiTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getWiFi().equalsIgnoreCase("0")) {
                wifiTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                wifiTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getBluetooth().equalsIgnoreCase("1")) {
                bluetoothTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getBluetooth().equalsIgnoreCase("0")) {
                bluetoothTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                bluetoothTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getProximity().equalsIgnoreCase("1")) {
                proximitySensorTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getProximity().equalsIgnoreCase("0")) {
                proximitySensorTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                proximitySensorTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getKeysButtons().equalsIgnoreCase("1")) {
                buttonsTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getKeysButtons().equalsIgnoreCase("0")) {
                buttonsTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                buttonsTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getVibration().equalsIgnoreCase("1")) {
                vibrationTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getVibration().equalsIgnoreCase("0")) {
                vibrationTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                vibrationTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getFrontCamera().equalsIgnoreCase("1")) {
                frontCameraTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getFrontCamera().equalsIgnoreCase("0")) {
                frontCameraTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                frontCameraTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getRearCamera().equalsIgnoreCase("1")) {
                rearCameraTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getRearCamera().equalsIgnoreCase("0")) {
                rearCameraTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                rearCameraTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getAltraWideCamera().equalsIgnoreCase("1")) {
                altraWideCameraTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getAltraWideCamera().equalsIgnoreCase("0")) {
                altraWideCameraTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                altraWideCameraTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getLoudSpeaker().equalsIgnoreCase("1")) {
                loudSpeakerTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getLoudSpeaker().equalsIgnoreCase("0")) {
                loudSpeakerTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                loudSpeakerTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getMicrophone().equalsIgnoreCase("1")) {
                microphoneTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getMicrophone().equalsIgnoreCase("0")) {
                microphoneTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                microphoneTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getEarpiece().equalsIgnoreCase("1")) {
                earpieceTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getEarpiece().equalsIgnoreCase("0")) {
                earpieceTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                earpieceTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getLCDGlass().equalsIgnoreCase("1")) {
                lCDTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getLCDGlass().equalsIgnoreCase("0")) {
                lCDTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                lCDTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getLCDPixel().equalsIgnoreCase("1")) {
                lCDPixelTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getLCDPixel().equalsIgnoreCase("0")) {
                lCDPixelTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                lCDPixelTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }

            if (userSession.getDigitizer().equalsIgnoreCase("1")) {
                digitizerTest.setBackground(getDrawable(R.drawable.bg_test_drwble_green));
            } else if (userSession.getDigitizer().equalsIgnoreCase("0")) {
                digitizerTest.setBackground(getDrawable(R.drawable.bg_test_drwble_red));
            } else {
                digitizerTest.setBackground(getDrawable(R.drawable.bg_test_drwble_blue));
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(mReceiver);
        unregisterReceiver(myReceiver);
    }
}