package com.xtracover.xcqcmh.TestActivities;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.UserSession;

import java.util.List;

public class FlashTestActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private ImageView backImg, checkImg, flashCheckImg, frontFlashCheckImg, lightSpotCheckImg;
    private LinearLayout flashLightTest, frontFlashTest, lightSpotTest;
    private boolean hasCameraFlash = false, hasFrontCameraFlash = false, flashOn = false;
    private int flashCount = 0, vibrationCount = 0, backCameraId, frontCameraId;
    private String[] cameraIdList;
    private Camera.Parameters params;
    private CameraManager camManager;
    private Camera mCamera;

    private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    private Camera.Parameters param;

    private CameraManager cameraManager;
    private String getCameraID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flash_test);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        getChangedNotificationColor();

        getLayoutUiIdFinds();

        camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    try {
                        getCameraID = cameraManager.getCameraIdList()[1];
                        cameraManager.setTorchMode(getCameraID, true);
                        flashOn = true;
                    } catch (CameraAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });

//        getCountBackFlashLight();

    }

    private void getCountBackFlashLight() {
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            try {
                                if (flashOn == true) {
                                    getCameraID = cameraManager.getCameraIdList()[0];
                                    cameraManager.setTorchMode(getCameraID, false);
                                    flashCheckImg.setVisibility(View.VISIBLE);
                                } else {
                                    getCameraID = cameraManager.getCameraIdList()[0];
                                    cameraManager.setTorchMode(getCameraID, false);
                                    flashCheckImg.setVisibility(View.INVISIBLE);
                                }
                            } catch (CameraAccessException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });
            }
        };
    }

    private void getLayoutUiIdFinds() {
        try {
            backImg = (ImageView) findViewById(R.id.backImg);
            checkImg = (ImageView) findViewById(R.id.checkImg);
            flashCheckImg = (ImageView) findViewById(R.id.flashCheckImg);
            frontFlashCheckImg = (ImageView) findViewById(R.id.frontFlashCheckImg);
            lightSpotCheckImg = (ImageView) findViewById(R.id.lightSpotCheckImg);
            flashLightTest = (LinearLayout) findViewById(R.id.flashLightTest);
            frontFlashTest = (LinearLayout) findViewById(R.id.frontFlashTest);
            lightSpotTest = (LinearLayout) findViewById(R.id.lightSpotTest);

            backImg.setOnClickListener(this);
            checkImg.setOnClickListener(this);
            flashLightTest.setOnClickListener(this);
            frontFlashTest.setOnClickListener(this);
            lightSpotTest.setOnClickListener(this);

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

            case R.id.flashLightTest:
//                backCameraHasFlash();
                break;

            case R.id.frontFlashTest:
                /*if (isFlashOn) {
                    turnOffFlash();
                    Toast.makeText(mContext, "ON", Toast.LENGTH_SHORT).show();
                } else {
                    turnOnFlash();
                    Toast.makeText(mContext, "OFF", Toast.LENGTH_SHORT).show();
                }*/
                break;

            case R.id.lightSpotTest:
                break;

        }
    }

    private void getCamera() {
        if (camera == null) {
            try {
                camera = Camera.open();
                params = camera.getParameters();
            } catch (Exception e) {
                e.getStackTrace();
            }
        }
    }

    private void turnOnFlash() {
        if (!isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(params);
            camera.startPreview();
            isFlashOn = true;
        }
    }

    private void turnOffFlash() {
        if (isFlashOn) {
            if (camera == null || params == null) {
                return;
            }

            params = camera.getParameters();
            params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(params);
            camera.stopPreview();
            isFlashOn = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        turnOffFlash();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasFlash) {
            turnOnFlash();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        getCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (camera != null) {
            camera.release();
            camera = null;
        }
    }

    private void backCameraHasFlash() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String cameraId = camManager.getCameraIdList()[0];
                backCameraId = getBackCameraId();
                CameraCharacteristics chars = null;
                chars = camManager.getCameraCharacteristics(String.valueOf(backCameraId));
                Integer facing = null;
                facing = chars.get(CameraCharacteristics.LENS_FACING);
                boolean hasFlash = false;
                hasFlash = chars.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

                if (backCameraId == 0 && facing != null && facing == CameraCharacteristics.LENS_FACING_BACK && hasFlash) {
                    hasCameraFlash = true;
                    System.out.println("Back flash A :- " + hasCameraFlash);
                    countBackFlashLightTest(hasCameraFlash);
                } else {
                    hasCameraFlash = false;
                    System.out.println("Back flash B :- " + hasCameraFlash);
                }
            } else {
                getBackCamera();
                params = mCamera.getParameters();
                params = mCamera.getParameters();
                List SupportedFlashModes = params.getSupportedFlashModes();
                backCameraId = getBackCameraId();
                if (backCameraId == 0 && SupportedFlashModes != null && SupportedFlashModes.contains(Camera.Parameters.FLASH_MODE_TORCH)) {
                    hasCameraFlash = true;
                    Toast.makeText(mContext, "Camera Flash Present", Toast.LENGTH_SHORT).show();
                    countBackFlashLightTest(hasCameraFlash);
                } else {
                    hasCameraFlash = false;
                    Toast.makeText(mContext, "Camera Flash Not Present", Toast.LENGTH_SHORT).show();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void countBackFlashLightTest(boolean hasBackFlash) {
        try {
          /*  new CountDownTimer(2000, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    if (hasBackFlash == true) {
                        backFlashOn();
                        System.out.println("Back flash C :- " + hasCameraFlash);
                    } else {
                        System.out.println("Back Camera Flash Not Present");
                    }
                }

                @Override
                public void onFinish() {
                    backFlashOff();
                    flashCheckImg.setVisibility(View.VISIBLE);
                    Toast.makeText(mContext, "Back flash test pass!", Toast.LENGTH_SHORT).show();

                }
            };*/

            if (hasBackFlash == true) {
                backFlashOn();
                System.out.println("Back flash C :- " + hasCameraFlash);
            } else {
                System.out.println("Back Camera Flash Not Present");
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void frontCameraHasFlash() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                cameraIdList = camManager.getCameraIdList();
                String cameraId = camManager.getCameraIdList()[1];
                frontCameraId = getFrontCameraId();
                CameraCharacteristics chars = null;
                chars = camManager.getCameraCharacteristics(String.valueOf(frontCameraId));
                Integer facing = null;
                facing = chars.get(CameraCharacteristics.LENS_FACING);
                boolean hasFlash = false;
                hasFlash = chars.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

                if (frontCameraId == 1 && facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT && hasFlash) {
                    hasFrontCameraFlash = true;
                } else {
                    hasFrontCameraFlash = false;
                }
            } else {
                getFrontCamera();
                params = mCamera.getParameters();
                params = mCamera.getParameters();
                List SupportedFlashModes = params.getSupportedFlashModes();
                frontCameraId = getFrontCameraId();
                if (frontCameraId == 1 && SupportedFlashModes != null && SupportedFlashModes.size() > 1 && SupportedFlashModes.get(0).equals(Camera.Parameters.FLASH_MODE_ON)) {
                    hasFrontCameraFlash = true;
                    Toast.makeText(mContext, "Camera Flash Present", Toast.LENGTH_SHORT).show();
                } else {
                    hasFrontCameraFlash = false;
                    Toast.makeText(mContext, "Camera Flash Not Present", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getBackCameraId() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String cameraId = camManager.getCameraIdList()[0];
                int i = -1;
                i = Integer.parseInt(cameraId);
                return i;
            } else {
                Camera.CameraInfo ci = new Camera.CameraInfo();
                for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                    Camera.getCameraInfo(i, ci);
                    if (ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK) return i;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return -1; // No front-facing camera found
    }

    private int getFrontCameraId() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager cameraManager = null;
                cameraManager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
                assert cameraManager != null;
                String cameraId = cameraManager.getCameraIdList()[1];
                int i = -1;
                i = Integer.parseInt(cameraId);
                return i;
            } else {
                Camera.CameraInfo ci = new Camera.CameraInfo();
                for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
                    Camera.getCameraInfo(i, ci);
                    if (ci.facing == Camera.CameraInfo.CAMERA_FACING_BACK) return i;
                }
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        return -1; // No front-facing camera found
    }

    private void backFlashOn() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    String camBId = camManager.getCameraIdList()[0];
                    backCameraId = getBackCameraId();
                    camManager.setTorchMode(String.valueOf(backCameraId), true);   //Turn ON
                    System.out.println("Back Camera D :- " + backCameraId);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    getBackCamera();
                    params = mCamera.getParameters();
                    params = mCamera.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(params);
                    mCamera.startPreview();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void frontFlashOn() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                try {
                    String camFId = cameraManager.getCameraIdList()[1];
                    frontCameraId = getFrontCameraId();
                    cameraManager.setTorchMode(String.valueOf(frontCameraId), true);   //Turn ON
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    getFrontCamera();
                    params = mCamera.getParameters();
                    params = mCamera.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                    mCamera.setParameters(params);
                    mCamera.startPreview();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getBackCamera() {
        try {
            if (mCamera == null) {
                try {
                    mCamera = Camera.open(0);
                    params = mCamera.getParameters();
                } catch (RuntimeException e) {
                    System.out.println("Back Camera Error. " + e.getMessage());
                }
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getFrontCamera() {
        try {
            if (mCamera == null) {
                try {
                    mCamera = Camera.open(1);
                    params = mCamera.getParameters();
                } catch (RuntimeException e) {
                    System.out.println("Front Camera Error. " + e.getMessage());
                }
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void backFlashOff() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    String camBId = camManager.getCameraIdList()[0];
                    backCameraId = getBackCameraId();
                    camManager.setTorchMode(String.valueOf(backCameraId), false);   //Turn ON
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    getBackCamera();
                    params = mCamera.getParameters();
                    params = mCamera.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(params);
                    mCamera.startPreview();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void frontFlashOff() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    String camFId = camManager.getCameraIdList()[1];
                    frontCameraId = getFrontCameraId();
                    camManager.setTorchMode(String.valueOf(frontCameraId), false);   //Turn ON
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    getFrontCamera();
                    params = mCamera.getParameters();
                    params = mCamera.getParameters();
                    params.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
                    mCamera.setParameters(params);
                    mCamera.startPreview();
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }
}