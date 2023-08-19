package com.xtracover.xcqcmh.Activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.xtracover.xcqcmh.Interface.ApiClient;
import com.xtracover.xcqcmh.Models.LoginResponse;
import com.xtracover.xcqcmh.R;
import com.xtracover.xcqcmh.Utilities.ApiNetworkClient;
import com.xtracover.xcqcmh.Utilities.NetworkStatus;
import com.xtracover.xcqcmh.Utilities.UserSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext;
    private UserSession userSession;
    private TextView appVersion_txt, txt_resetP;
    private EditText email_etP, password_etP;
    private AppCompatButton emailSignInButtonP, pinSignInButtonN;
    private String mailId = "", password = "";
    private CompositeDisposable disposable;
    private String rearCameraMp, frontCameraMp, screenSize, screensize, brandName;
    private String modelName, storage1, ram, processorCore, batteryCapacity, deviceId;
    private String deviceName, androidOs, osName, serialNumber, stringMac, strMacAddress;
    private String address, addressAFS;
    private int maxResolution2 = 0, maxResolution1 = 0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Object mPowerProfile_;
    private double batteryCapacities = 0.0;
    private static final String POWER_PROFILE_CLASS = "com.android.internal.os.PowerProfile";
    private Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        mContext = this;
        userSession = new UserSession(mContext);
        disposable = new CompositeDisposable();
        getChangedNotificationColor();
        getLayoutUiIdfind();
        getSoftwareVersion();

        emailSignInButtonP.setOnClickListener(this);
        pinSignInButtonN.setOnClickListener(this);
        txt_resetP.setOnClickListener(this);

    }

    private void getLayoutUiIdfind() {
        try {
            appVersion_txt = (TextView) findViewById(R.id.appVersion_txt);
            txt_resetP = (TextView) findViewById(R.id.txt_resetP);
            email_etP = (EditText) findViewById(R.id.email_etP);
            password_etP = (EditText) findViewById(R.id.password_etP);
            emailSignInButtonP = (AppCompatButton) findViewById(R.id.emailSignInButtonP);
            pinSignInButtonN = (AppCompatButton) findViewById(R.id.pinSignInButtonN);

            sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            System.out.println("1. Aashish Vishwakarma");
            getDeviceConfigurationDetails();

        } catch (Exception exp) {
            exp.getStackTrace();
        }
    }

    private void getDeviceConfigurationDetails() {
        try {
            System.out.println("2. Aashish Vishwakarma");
            frontCameraMp = getFrontCamera();
            rearCameraMp = getRearCamera();
            screenSize = screenInch();
            brandName = Build.BRAND;
            modelName = Build.MODEL;
            storage1 = storage();
            ram = getTotalRAM();
            processorCore = "" + getNumOfCores();
            batteryCapacity = battery();
            deviceId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
            deviceName = getDeviceName();
            androidOs = "(" + AboutOS() + ") " + Build.VERSION.RELEASE;
            osName = "Android";
            serialNumber = getSerialNumber();
            getMacAddress();

            System.out.println("Configuration of Device :- ");
            System.out.println("Front Camera - " + frontCameraMp);
            System.out.println("Rear Camera - " + rearCameraMp);
            System.out.println("Screen Size - " + screenSize);
            System.out.println("Brand Name - " + brandName);
            System.out.println("Model Name - " + modelName);
            System.out.println("Device Storage - " + storage1);
            System.out.println("Device Ram - " + ram);
            System.out.println("Device Processor - " + processorCore);
            System.out.println("Battery Capacity - " + batteryCapacity);
            System.out.println("Device Id - " + deviceId);
            System.out.println("Device Name - " + deviceName);
            System.out.println("Android Os - " + androidOs);
            System.out.println("OS Name - " + osName);
            System.out.println("Device Serial Number - " + serialNumber);
            System.out.println("Device Mac Address - " + strMacAddress);

            String jsonData = ApiJsonUpdateResult(frontCameraMp, rearCameraMp, screenSize,
                    brandName, modelName, storage1, ram, processorCore, batteryCapacity,
                    deviceId, deviceName, androidOs, osName, serialNumber, strMacAddress).toString();
            System.out.println("Configuration of Device Data :- " + jsonData);

        } catch (Exception exp) {
            exp.getStackTrace();
            System.out.println("Device Configuration Exception :- " + exp.getMessage() + ", " + exp.getCause());
        }
    }

    private JsonObject ApiJsonUpdateResult(String frontCameraMp, String rearCameraMp, String screenSize,
                                           String brandName, String modelName, String storage1,
                                           String ram, String processorCore, String batteryCapacity,
                                           String deviceId, String deviceName, String androidOs,
                                           String osName, String serialNumber, String strMacAddress) {

        JsonObject gsonObjectUpdateResult = new JsonObject();
        try {
            JSONObject paramAbResult = new JSONObject();
            paramAbResult.put("Front_Camera", frontCameraMp);
            paramAbResult.put("Rear_Camera", rearCameraMp);
            paramAbResult.put("Screen_Size", screenSize);
            paramAbResult.put("Brand_Name", brandName);
            paramAbResult.put("Model_Name", modelName);
            paramAbResult.put("Device_Storage", storage1);
            paramAbResult.put("Device_Ram", ram);
            paramAbResult.put("Device_Processor", processorCore);
            paramAbResult.put("Battery_Capacity", batteryCapacity);
            paramAbResult.put("Device_Id", deviceId);
            paramAbResult.put("Device_Name", deviceName);
            paramAbResult.put("Os", androidOs);
            paramAbResult.put("OS_Name", osName);
            paramAbResult.put("Device_Serial_Number", serialNumber);
            paramAbResult.put("Device_Mac_Address", strMacAddress);

            JsonParser jsonParser = new JsonParser();
            gsonObjectUpdateResult = (JsonObject) jsonParser.parse(paramAbResult.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return gsonObjectUpdateResult;
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
            case R.id.emailSignInButtonP:
                if (NetworkStatus.isNetworkAvailable(mContext)) {
                    if (isLoginDataValidate()) {
                        getDataForLogin(mailId, password);
                    }
                } else {
                    Toast.makeText(mContext, "Please check your internet connection!", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.pinSignInButtonN:
                break;

            case R.id.txt_resetP:
                Toast.makeText(mContext, "Reset option is working progress!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void getDataForLogin(String mailId, String password) {
        try {
            dialog = new Dialog(mContext);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.google_progress_bar_item);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            ApiClient apiClient = ApiNetworkClient.getStoreApiRetrofit().create(ApiClient.class);
            disposable.add(apiClient.getUsersAccountLogin(mailId, password)
                    .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableSingleObserver<LoginResponse>() {

                        @Override
                        public void onSuccess(LoginResponse loginResponse) {
                            if (loginResponse.getRespMsg().equalsIgnoreCase("SUCCESS")) {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                System.out.println("Login Response data :- " + loginResponse.toString());
                                Toast.makeText(mContext, "Login successfully!", Toast.LENGTH_SHORT).show();
                                userSession.createLoginSession(mailId, password);
                                String user = loginResponse.getLoginData().get(0).getEmpName();
                                String userid = loginResponse.getLoginData().get(0).getEmpCode();
                                Intent dashboard = new Intent(mContext, DashboardActivity.class);
                                userSession.setEmpCode(userid);
                                userSession.setUserName(userid);
                                startActivity(dashboard);
                                finish();
                            } else {
                                if (dialog.isShowing()) {
                                    dialog.dismiss();
                                }
                                Toast.makeText(mContext, "Invalid Credentials!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                            Toast.makeText(mContext, "Server Error!", Toast.LENGTH_SHORT).show();
                        }
                    }));
        } catch (Exception exp) {
            exp.getStackTrace();
            System.out.println("Login exception msg :- " + exp.getMessage() + ", " + exp.getCause());
        }
    }

    private boolean isLoginDataValidate() {
        try {
            mailId = email_etP.getText().toString().trim();
            password = password_etP.getText().toString().trim();
            if (mailId.isEmpty()) {
                email_etP.setError("Enter user id");
                email_etP.requestFocus();
                return false;
            } else if (password.isEmpty()) {
                password_etP.setError("Enter password");
                password_etP.requestFocus();
                return false;
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
        return true;
    }

    private void getSoftwareVersion() {
        try {
            PackageInfo pInfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            String version = pInfo.versionName;
            System.out.println("Software Version :- " + version);
            appVersion_txt.setText("Version: " + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private String getFrontCamera() {
        int noOfCameras = Camera.getNumberOfCameras();
        float maxResolution = -1;
        long pixelCount = -1;
        try {
            for (int i = 0; i < noOfCameras; i++) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);

                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                    Camera camera = Camera.open(i);
                    Camera.Parameters cameraParams = camera.getParameters();
                    for (int j = 0; j < cameraParams.getSupportedPictureSizes().size(); j++) {
                        long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                        if (pixelCountTemp > pixelCount) {
                            pixelCount = pixelCountTemp;
                            maxResolution = ((float) pixelCountTemp) / (1024000.0f);
                            maxResolution1 = (int) Math.ceil(maxResolution);
                        }
                    }
                    camera.release();
                }
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }
        editor.putString("FrontCameraMp", "" + maxResolution1 + " MP");
        editor.apply();
        editor.commit();

        return "" + maxResolution1 + " MP";
    }

    private String getRearCamera() {
        int noOfCameras = Camera.getNumberOfCameras();
        float maxResolution = -1;
        long pixelCount = -1;
        try {
            for (int i = 0; i < noOfCameras; i++) {
                Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
                Camera.getCameraInfo(i, cameraInfo);

                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    Camera camera = Camera.open(i);
                    Camera.Parameters cameraParams = camera.getParameters();
                    for (int j = 0; j < cameraParams.getSupportedPictureSizes().size(); j++) {
                        long pixelCountTemp = cameraParams.getSupportedPictureSizes().get(j).width * cameraParams.getSupportedPictureSizes().get(j).height; // Just changed i to j in this loop
                        if (pixelCountTemp > pixelCount) {
                            pixelCount = pixelCountTemp;
                            maxResolution = ((float) pixelCountTemp) / (1024000.0f);
                            maxResolution2 = (int) Math.ceil(maxResolution);
                        }
                    }
                    camera.release();
                }
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }

        editor.putString("RearCameraMp", "" + maxResolution2 + " MP");
        editor.apply();
        editor.commit();

        return "" + maxResolution2 + " MP";
    }

    public String screenInch() {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= 17) {
            windowmanager.getDefaultDisplay().getRealMetrics(dm);
        } else {
            windowmanager.getDefaultDisplay().getMetrics(dm);
        }
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int dens = dm.densityDpi;
        double wi = (double) width / dm.xdpi;
        double hi = (double) height / dm.ydpi;
        double x = Math.pow(wi, 2);
        double y = Math.pow(hi, 2);
        double screenInches = Math.sqrt(x + y);
        DecimalFormat twoDecimalForm = new DecimalFormat("0.0");
        screensize = twoDecimalForm.format(screenInches);
        String screenInche = twoDecimalForm.format(screenInches).concat(" inch");

        editor.putString("ScreenSize", "" + screenInche);
        editor.apply();
        editor.commit();
        return "" + screenInche;
    }

    private String storage() {
        float totalSize = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                totalSize = megabytesAvailable(Environment.getDataDirectory());
            }
            editor.putString("Storege", formatSize((long) totalSize));
            editor.apply();
            editor.commit();
        } catch (Exception exp) {
            exp.getStackTrace();
        }
        return formatSize((long) totalSize);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static float megabytesAvailable(File f) {
        StatFs stat = new StatFs(f.getAbsolutePath());
        long bytesAvailable = 0, freeSize = 0, totalSize = 0;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                freeSize = stat.getFreeBlocksLong() * (long) stat.getBlockSizeLong();
                totalSize = stat.getTotalBytes();
            } else {
                freeSize = stat.getFreeBlocks() * (long) stat.getBlockSize();
                totalSize = stat.getTotalBytes();
            }
        } catch (Exception exp) {
            exp.getStackTrace();
        }

        return totalSize;
    }

    public static String formatSize(long size) {
        String suffix = null;

        if (size >= 1024) {
            suffix = " KB";
            size /= 1024;
            if (size >= 1024) {
                suffix = " MB";
                size /= 1024;
                if (size >= 1024) {
                    suffix = " GB";
                    size /= 1024;
                    if (size >= 1024) {
                        suffix = " TB";
                        size /= 1024;
                    }
                }
            }
        }
        if (size <= 1) {
            size = 1;
        } else if (size <= 2) {
            size = 2;
        } else if (size <= 4) {
            size = 4;
        } else if (size <= 8) {
            size = 8;
        } else if (size <= 16) {
            size = 16;
        } else if (size <= 32) {
            size = 32;
        } else if (size <= 64) {
            size = 64;
        } else if (size <= 128) {
            size = 128;
        } else if (size <= 256) {
            size = 256;
        } else if (size <= 512) {
            size = 512;
        } else if (size <= 1024) {
            size = 1024;
        }
        StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

        int commaOffset = resultBuffer.length() - 3;
        while (commaOffset > 0) {
            resultBuffer.insert(commaOffset, ',');
            commaOffset -= 3;
        }
        if (suffix != null) resultBuffer.append(suffix);
        return resultBuffer.toString();
    }

    public String getTotalRAM() {
        RandomAccessFile reader = null;
        String load = null;
        DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
        double totRam = 0;
        String lastValue = "";
        try {
            reader = new RandomAccessFile("/proc/meminfo", "r");
            load = reader.readLine();
            // Get the Number value from the string
            Pattern p = Pattern.compile("(\\d+)");
            Matcher m = p.matcher(load);
            String value = "";
            while (m.find()) {
                value = m.group(1);
            }
            reader.close();

            totRam = Double.parseDouble(value);
            double mb = totRam / 1024.0;
            double gb = totRam / 1048576.0;
            double tb = totRam / 1073741824.0;

            if (tb >= 1) {
                lastValue = twoDecimalForm.format((int) Math.ceil(tb)).concat(" TB");
            } else if (gb >= 1) {
                lastValue = twoDecimalForm.format((int) Math.ceil(gb)).concat(" GB");
            } else if (mb >= 1) {
                lastValue = twoDecimalForm.format((int) Math.ceil(mb)).concat(" MB");
            } else {
                lastValue = twoDecimalForm.format((int) Math.ceil(totRam)).concat(" KB");
            }

            editor.putString("ram", lastValue);
            editor.apply();
            editor.commit();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return lastValue;
    }

    private int getNumOfCores() {
        try {
            int i = new File("/sys/devices/system/cpu/").listFiles(new FileFilter() {
                public boolean accept(File params) {
                    return Pattern.matches("cpu[0-9]", params.getName());
                }
            }).length;
            return i;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    private String battery() {
        try {
            mPowerProfile_ = Class.forName(POWER_PROFILE_CLASS).getConstructor(Context.class).newInstance(this);
            batteryCapacities = (double) Class.forName(POWER_PROFILE_CLASS).getMethod("getBatteryCapacity").invoke(mPowerProfile_);

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        editor.putString("BatteryCapacity", "" + (String.format(Locale.US, "%.1f", batteryCapacity)) + "  mAh");
        editor.apply();
        editor.commit();

        return "" + (String.format(Locale.US, "%.1f", batteryCapacity)) + "  mAh";
    }

    public String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.toLowerCase().startsWith(manufacturer.toLowerCase())) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String AboutOS() {
        Field[] fields = Build.VERSION_CODES.class.getFields();
        String osName = "UNKNOWN";
        for (Field field : fields) {
            try {
                if (field.getInt(Build.VERSION_CODES.class) == Build.VERSION.SDK_INT) {
                    osName = field.getName();
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        editor.putString("OSName", osName);
        editor.apply();
        editor.commit();
        return osName;
    }

    @SuppressLint("HardwareIds")
    public static String getSerialNumber() {
        String serialNumber;

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get", String.class);

            serialNumber = (String) get.invoke(c, "gsm.sn1");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ril.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "ro.serialno");
            if (serialNumber.equals(""))
                serialNumber = (String) get.invoke(c, "sys.serialnumber");
            if (serialNumber.equals(""))
                serialNumber = Build.SERIAL;

            if (serialNumber.equals("unknown")) {
                try {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        serialNumber = Build.getSerial();
                    }
                } catch (SecurityException e) {
                    serialNumber = "Not fetched";
                    e.printStackTrace();
                    Log.d("responseData", String.valueOf(e.getMessage()));
                    e.printStackTrace();
                    System.out.println("GetInTouchActivity Exception Serial Number :- " + e.getMessage() + ", " + e.getCause());

                }
            }

            // If none of the methods above worked
            if (serialNumber.equals(""))
                serialNumber = "Not fetched";
        } catch (Exception e) {
            e.printStackTrace();
            serialNumber = "Not fetched";
        }

        return serialNumber;
    }

    public void getMacAddress() {
        try {
            List<NetworkInterface> networkInterfaceList = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface networkInterface : networkInterfaceList) {
                if (networkInterface.getName().equalsIgnoreCase("wlan0")) {
                    for (int i = 0; i < networkInterface.getHardwareAddress().length; i++) {
                        String stringMacByte = Integer.toHexString(networkInterface.getHardwareAddress()[i] & 0xFF);
                        if (stringMacByte.length() == 1) {
                            stringMacByte = "0" + stringMacByte;
                        }
                        stringMac = stringMac + stringMacByte.toUpperCase() + ":";
                    }
                    break;
                }
            }
            Log.d("getMac2:", "" + stringMac.substring(0, stringMac.length()));
            String macAddressString = stringMac.substring(0, stringMac.length() - 1);
            if (macAddressString.contains("null")) {
                strMacAddress = macAddressString.substring(4);
                editor.putString("macAddress", strMacAddress);
                editor.apply();
                editor.commit();
            } else {
                strMacAddress = stringMac.substring(0, stringMac.length() - 1);
                editor.putString("macAddress", strMacAddress);
                editor.apply();
                editor.commit();
            }
        } catch (Exception ex) {
            address = getServerIPv4();
            addressAFS = address.replace("%wlan0", "");
            Log.d("Mac Address Number :- ", addressAFS);
            strMacAddress = addressAFS.toUpperCase();
            editor.putString("macAddress", strMacAddress);
            editor.apply();
            editor.commit();
            ex.getStackTrace();
        }
    }

    public static String getServerIPv4() {
        String candidateAddress = null;
        try {
            Enumeration<NetworkInterface> nics = NetworkInterface.getNetworkInterfaces();
            while (nics.hasMoreElements()) {
                NetworkInterface nic = nics.nextElement();
                Enumeration<InetAddress> inetAddresses = nic.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    String address = inetAddresses.nextElement().getHostAddress();
                    String nicName = nic.getName();
                    if (nicName.startsWith("wlan0") || nicName.startsWith("en0")) {
                        return address;
                    }

                    if (nicName.endsWith("0") || candidateAddress == null) {
                        candidateAddress = address;
                    }
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException("Cannot resolve local network address", e);
        }
        return candidateAddress == null ? "127.0.0.1" : candidateAddress;
    }
}