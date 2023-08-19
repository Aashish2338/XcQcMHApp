package com.xtracover.xcqcmh.TestActivities;

import static android.net.wifi.WifiManager.WIFI_STATE_ENABLED;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class WiFiInternetTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, wifiCheckImg;
    private WifiManager wifiManager;
    private boolean wifiEnabled = false, previousConnectivityStatus = false, mConnected = false;
    private ConnectivityManager connectionManager;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wi_fi_internet_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();
        getLayoutUiIdFinds();

        backImg.setOnClickListener(this);
        checkImg.setOnClickListener(this);

        getCountTimeForWiFiTest();

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
                        wifiCheckImg.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "Wi-Fi test pass!", Toast.LENGTH_SHORT).show();
                    } else {
                        wifiCheckImg.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, "Wi-Fi test fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

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

    private void getLayoutUiIdFinds() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            wifiCheckImg = (ImageView) findViewById(R.id.wifiCheckImg);

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
                Toast.makeText(mContext, "In Progress working!", Toast.LENGTH_SHORT).show();
                break;
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

    private boolean isInternetConnectivityAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}