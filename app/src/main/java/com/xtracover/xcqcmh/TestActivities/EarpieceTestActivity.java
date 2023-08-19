package com.xtracover.xcqcmh.TestActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class EarpieceTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, earpieceCheckImg;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;
    private boolean earphoneResults, connectedEarphone;
    private int max;
    private MusicIntentReceiver myReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earpiece_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();

        getLayoutUiIdfinds();

        backImg.setOnClickListener(this);
        checkImg.setOnClickListener(this);

        myReceiver = new MusicIntentReceiver();

        if (connectedEarphone) {
            getCountEarphoneTestOfDevice();
        } else {
            Toast.makeText(mContext, "Connect earphone please!", Toast.LENGTH_SHORT).show();
        }

    }

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
                    earpieceCheckImg.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, "Earphone test pass!", Toast.LENGTH_SHORT).show();
                } else {
                    earpieceCheckImg.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Earphone test fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    private void getLayoutUiIdfinds() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            earpieceCheckImg = (ImageView) findViewById(R.id.earpieceCheckImg);

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
                Toast.makeText(mContext, "In progress work!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onStart() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(myReceiver, filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(myReceiver);
        super.onStop();
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
}