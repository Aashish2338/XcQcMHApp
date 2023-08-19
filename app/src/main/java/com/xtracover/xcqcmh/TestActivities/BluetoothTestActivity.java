package com.xtracover.xcqcmh.TestActivities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

import java.util.Set;

public class BluetoothTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, bluetoothCheckImg;
    private static BluetoothAdapter mBluetoothAdapter;
    private CountDownTimer countDownTimer;
    private boolean bluetoothEnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();
        getLayoutUiIdFinds();

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        getCountBluetoothTest();

    }

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
                        bluetoothCheckImg.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "Bluetooth test pass!", Toast.LENGTH_SHORT).show();
                    } else {
                        bluetoothCheckImg.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, "Bluetooth test fail!", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getLayoutUiIdFinds() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            bluetoothCheckImg = (ImageView) findViewById(R.id.bluetoothCheckImg);

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
                        activityResultLauncher.launch(enableBtIntent);
                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:
                        Intent enableBtIntentA = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        activityResultLauncher.launch(enableBtIntentA);
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

    @Override
    protected void onResume() {
        super.onResume();
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
    }
}