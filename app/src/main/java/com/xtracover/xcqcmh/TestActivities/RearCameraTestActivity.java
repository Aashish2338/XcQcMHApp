package com.xtracover.xcqcmh.TestActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.hardware.Camera;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class RearCameraTestActivity extends AppCompatActivity {

    private Context mContext;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rear_camera_test);
        mContext = this;
        userSession = new UserSession(mContext);

    }
}