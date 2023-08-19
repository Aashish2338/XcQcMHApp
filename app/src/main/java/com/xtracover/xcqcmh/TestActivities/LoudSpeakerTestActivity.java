package com.xtracover.xcqcmh.TestActivities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class LoudSpeakerTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, loudSpeakerCheckImg;
    private AudioManager audioManager;
    private MediaPlayer mediaPlayer;
    private CountDownTimer countDownTimer;
    private boolean speakerResults;
    private int max;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loud_speaker_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();

        getLayoutUiIdFinds();

        backImg.setOnClickListener(this);
        checkImg.setOnClickListener(this);

        getCountLoudSpeakerTestOfDevice();

    }

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
                    loudSpeakerCheckImg.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, "Loud Speaker test pass!", Toast.LENGTH_SHORT).show();
                } else {
                    loudSpeakerCheckImg.setVisibility(View.GONE);
                    Toast.makeText(mContext, "Loud Speaker test fail!", Toast.LENGTH_SHORT).show();
                }
            }
        }.start();
    }

    private void getLayoutUiIdFinds() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            loudSpeakerCheckImg = (ImageView) findViewById(R.id.loudSpeakerCheckImg);


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
}