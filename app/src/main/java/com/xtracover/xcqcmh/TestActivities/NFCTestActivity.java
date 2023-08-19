package com.xtracover.xcqcmh.TestActivities;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class NFCTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, nfcCheckImg;
    private CountDownTimer countDownTimer;
    private PendingIntent pendingIntent;
    private NfcManager nfcManager;
    private NfcAdapter nfcAdapter = null;
    private boolean nfcFinder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nfctest);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();

        getLayoutUiIdFinds();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(mContext,
                    0, new Intent(mContext, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_IMMUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(mContext,
                    0, new Intent(mContext, this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                    PendingIntent.FLAG_UPDATE_CURRENT);
        }

        nfcManager = (NfcManager) mContext.getSystemService(Context.NFC_SERVICE);
        nfcAdapter = nfcManager.getDefaultAdapter();

        getCountNFCTest();

    }

    private void getCountNFCTest() {
        try {
            countDownTimer = new CountDownTimer(2000, 1000) {

                @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                public void onTick(long millisUntilFinished) {
                    nfcManager = (NfcManager) mContext.getSystemService(Context.NFC_SERVICE);
                    nfcAdapter = nfcManager.getDefaultAdapter();
                    if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                        System.out.println("Yes NFC available");
                        nfcTest();
                    } else if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
                        System.out.println("NFC is not enabled.Need to enable by the user.");
                        nfcFinder = false;
                    } else if (nfcAdapter == null) {
                        nfcFinder = false;
                        System.out.println("NFC is not supported");
                    }
                }

                public void onFinish() {
                    if (nfcFinder == true) {
                        nfcCheckImg.setVisibility(View.VISIBLE);
                        Toast.makeText(mContext, "Yes NFC available!", Toast.LENGTH_SHORT).show();
                    } else {
                        nfcCheckImg.setVisibility(View.INVISIBLE);
                        Toast.makeText(mContext, "NFC is not supported!", Toast.LENGTH_SHORT).show();
                    }
                }
            }.start();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void nfcTest() {
        try {
            if (nfcAdapter != null) {
                if (nfcAdapter != null && nfcAdapter.isEnabled()) {
                    System.out.println("Yes NFC available");
                    nfcFinder = true;
                } else if (nfcAdapter != null && !nfcAdapter.isEnabled()) {
                    System.out.println("NFC is not enabled.Need to enable by the user.");
                    showWirelessSettings();
                    nfcAdapter.enableForegroundDispatch((Activity) mContext, pendingIntent, null, null);
                } else if (nfcAdapter == null) {
                    System.out.println("NFC is not supported");
                    nfcFinder = false;
                }
            } else {
                System.out.println("NFC Not Supported");
                nfcFinder = false;
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void showWirelessSettings() {
        try {
            Toast.makeText(mContext, "You need to enable NFC", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
            startActivity(intent);
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getLayoutUiIdFinds() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            nfcCheckImg = (ImageView) findViewById(R.id.nfcCheckImg);

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
}