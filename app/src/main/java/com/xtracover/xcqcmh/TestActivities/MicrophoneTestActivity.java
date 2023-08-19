package com.xtracover.xcqcmh.TestActivities;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

public class MicrophoneTestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int RECORDER_SAMPLERATE = 1;
    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, loudSpeakerImg, bottomMicImg, microphoneControlImg, rearMicImg;
    private MediaRecorder mRecorder;
    private MediaPlayer mPlayer;
    private static String mFileName = null;
    public static final int REQUEST_AUDIO_PERMISSION_CODE = 1;
    private double gain = 2.0;
    private AudioRecord audioRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_microphone_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();

        getLayoutUiIdFinds();

    }

    private void getLayoutUiIdFinds() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            loudSpeakerImg = (ImageView) findViewById(R.id.loudSpeakerImg);
            bottomMicImg = (ImageView) findViewById(R.id.bottomMicImg);
            microphoneControlImg = (ImageView) findViewById(R.id.microphoneControlImg);
            rearMicImg = (ImageView) findViewById(R.id.rearMicImg);

            backImg.setOnClickListener(this);
            checkImg.setOnClickListener(this);
            loudSpeakerImg.setOnClickListener(this);
            bottomMicImg.setOnClickListener(this);
            microphoneControlImg.setOnClickListener(this);
            rearMicImg.setOnClickListener(this);

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

            case R.id.loudSpeakerImg:
//                ByteBuffer data = ByteBuffer.allocateDirect(SAMPLES_PER_FRAME).order(ByteOrder.nativeOrder());
//                audioRecord = new AudioRecord()
//                int audioInputLengthBytes = audioRecord.read(data, SAMPLES_PER_FRAME);
//                ShortBuffer shortBuffer = data.asShortBuffer();
//                for (int i = 0; i < audioInputLengthBytes / 2; i++) { // /2 because we need the length in shorts
//                    short s = shortBuffer.get(i);
//                    int increased = (int) (s * gain);
//                    s = (short) Math.min(Math.max(increased, Short.MIN_VALUE), Short.MAX_VALUE);
//                    shortBuffer.put(i, s);
//                }
//                startRecording();
                break;

            case R.id.bottomMicImg:
                pauseRecording();
                break;

            case R.id.microphoneControlImg:
                pausePlaying();
                break;

            case R.id.rearMicImg:
                playAudio();
                break;
        }
    }

    private void startRecording() {
        if (CheckPermissions()) {
            mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
            mFileName += "/AudioRecording.3gp";
            mRecorder = new MediaRecorder();
            mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
            mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mRecorder.setOutputFile(mFileName);
            try {
                mRecorder.prepare();
            } catch (IOException e) {
                Log.e("TAG", "prepare() failed");
            }

            mRecorder.start();
            Toast.makeText(mContext, "Recording Started!", Toast.LENGTH_SHORT).show();
        } else {
            RequestPermissions();
        }
    }

    public boolean CheckPermissions() {
        int result = ContextCompat.checkSelfPermission(mContext, WRITE_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(mContext, RECORD_AUDIO);
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void RequestPermissions() {
        ActivityCompat.requestPermissions((Activity) mContext, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, REQUEST_AUDIO_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_AUDIO_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (permissionToRecord && permissionToStore) {
                        Toast.makeText(mContext, "Permission Granted", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(mContext, "Permission Denied", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }

    public void playAudio() {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(mFileName);
            mPlayer.prepare();
            mPlayer.start();
            Toast.makeText(mContext, "Recording Started Playing!", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("TAG", "prepare() failed");
        }
    }

    public void pauseRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        Toast.makeText(mContext, "Recording Stopped!", Toast.LENGTH_SHORT).show();
    }

    public void pausePlaying() {
        mPlayer.release();
        mPlayer = null;
        Toast.makeText(mContext, "Recording Play Stopped!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}