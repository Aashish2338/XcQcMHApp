package com.xtracover.xcqcmh.TestActivities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

public class AltraWideCameraTestActivity extends AppCompatActivity {

    private Context mContext;
    private UserSession userSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_altra_wide_camera_test);
        mContext = this;
        userSession = new UserSession(mContext);

    }
}