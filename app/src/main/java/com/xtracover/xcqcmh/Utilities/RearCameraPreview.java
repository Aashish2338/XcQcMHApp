package com.xtracover.xcqcmh.Utilities;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.hardware.Camera;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RearCameraPreview extends SurfaceView implements SurfaceHolder.Callback {

    private static String TAG = "CameraManager";

    private Context mContext = null;
    private SurfaceView mPreview = null;
    private SurfaceHolder mHolder = null;
    private Camera mCamera = null;
    private int mFrontFaceID = -1;
    private int mBackFaceID = -1;
    private int mActualFacingID = -1;
    private Bitmap bmp;
    private UserSession userSession;
    private Camera.PictureCallback mCall;

    public RearCameraPreview(Context context, SurfaceView preview) {
        super(context);
        mContext = context;
        mPreview = preview;
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);
        userSession = new UserSession(mContext);
    }

    //called in onCreate
    public void init() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, info);
            if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                mBackFaceID = i;
            }
        }
        if (mActualFacingID == -1) {
            if (mFrontFaceID != -1) {
                mActualFacingID = getFrontCameraId();
            } else {
                mActualFacingID = mBackFaceID;
            }
        }
    }

    public void start() {
        Log.i(TAG, "startCamera()");
        if (mCamera == null) {
            mActualFacingID = getFrontCameraId();
            mCamera = getCameraInstance(mActualFacingID);
        }
        if (mCamera == null) {
            Log.i(TAG, "can't get camera instance");
            return;
        }
        try {
            mCamera.setPreviewDisplay(mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        setCameraDisplayOrientation();
        setBestSupportedSizes();
        mCamera.startPreview();
    }

    public void stop() {
        Log.i(TAG, "stopCamera()");
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    public Camera getCameraInstance(int cameraID) {
        Camera c = null;
        if (cameraID != -1) {
            try {
                c = Camera.open(cameraID);
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "error opening camera: " + cameraID);
            }
        }
        return c;
    }

    int getFrontCameraId() {
        Camera.CameraInfo ci = new Camera.CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) return i;
        }
        return -1; // No front-facing camera found
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.i(TAG, "surfaceCreated()");
        start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.i(TAG, "surfaceChanged()");
        new CountDownTimer(2500, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                mCamera.takePicture(null, null, mCall);
            }
        }.start();

        mCall = new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Uri uriTarget = mContext.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
                OutputStream imageFileOS;
                try {
                    imageFileOS = mContext.getContentResolver().openOutputStream(uriTarget);
                    imageFileOS.write(data);
                    imageFileOS.flush();
                    imageFileOS.close();

                    Toast.makeText(mContext, "Image saved!", Toast.LENGTH_LONG).show();
                    if (uriTarget.toString() != null) {
                        userSession.setRearCamera("1");
                        userSession.setRearCameraImage(uriTarget.toString());
                        Toast.makeText(mContext, "Rear A", Toast.LENGTH_SHORT).show();
                    } else {
                        userSession.setRearCamera("0");
                        Toast.makeText(mContext, "Rear B", Toast.LENGTH_SHORT).show();
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                bmp = BitmapFactory.decodeByteArray(data, 0, data.length);
            }
        };
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.i(TAG, "surfaceDestroyed()");
        stop();
    }

    private void setBestSupportedSizes() {
        if (mCamera == null) {
            return;
        }
        Camera.Parameters parameters = mCamera.getParameters();
        List<Point> pictureSizes = getSortedSizes(parameters.getSupportedPictureSizes());

        List<Point> previewSizes = getSortedSizes(parameters.getSupportedPreviewSizes());

        Point previewResult = null;
        for (Point size : previewSizes) {
            float ratio = (float) size.y / size.x;
            if (Math.abs(ratio - 4 / (float) 3) < 0.05) { //Aspect ratio of 4/3 because otherwise the image scales to much.
                previewResult = size;
                break;
            }
        }
        Log.i(TAG, "preview: " + previewResult.x + "x" + previewResult.y);
        Point pictureResult = null;
        if (previewResult != null) {
            float previewRatio = (float) previewResult.y / previewResult.x;
            for (Point size : pictureSizes) {
                float ratio = (float) size.y / size.x;
                if (Math.abs(previewRatio - ratio) < 0.05) {
                    pictureResult = size;
                    break;
                }
            }
        }
        Log.i(TAG, "preview: " + pictureResult.x + "x" + pictureResult.y);

        if (previewResult != null && pictureResult != null) {
            Log.i(TAG, "best preview: " + previewResult.x + "x" + previewResult.y);
            Log.i(TAG, "best picture: " + pictureResult.x + "x" + pictureResult.y);
            parameters.setPreviewSize(previewResult.y, previewResult.x);
            parameters.setPictureSize(pictureResult.y, pictureResult.x);
            mCamera.setParameters(parameters);
            mPreview.setBackgroundColor(Color.TRANSPARENT); //in the case of errors needed
        } else {
            mCamera.stopPreview();
            mPreview.setBackgroundColor(Color.BLACK);
        }
    }

    private List<Point> getSortedSizes(List<Camera.Size> sizes) {
        ArrayList<Point> list = new ArrayList<>();

        for (Camera.Size size : sizes) {
            int height;
            int width;
            if (size.width > size.height) {
                height = size.width;
                width = size.height;
            } else {
                height = size.height;
                width = size.width;
            }
            list.add(new Point(width, height));

        }

        Collections.sort(list, new Comparator<Point>() {
            @Override
            public int compare(Point lhs, Point rhs) {
                long lhsCount = lhs.x * (long) lhs.y;
                long rhsCount = rhs.x * (long) rhs.y;
                if (lhsCount < rhsCount) {
                    return 1;
                }
                if (lhsCount > rhsCount) {
                    return -1;
                }
                return 0;
            }
        });
        return list;
    }

    //ROTATION
    private void setCameraDisplayOrientation() {
        if (mCamera != null) {
            mCamera.setDisplayOrientation((int) getRotation());
        }
    }

    @Override
    public float getRotation() {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(mActualFacingID, info);
        int rotation = ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }
}
