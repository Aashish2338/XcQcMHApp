package com.xtracover.xcqcmh.Utilities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.xtracover.xcqcmh.Activities.LoginActivity;

import java.util.HashMap;

public class UserSession {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "Xtracover";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String KEY_NAME = "name";
    private static final String KEY_EMAIL = "email";

    public UserSession(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String name, String password) {
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(KEY_NAME, name);
        editor.putString(KEY_EMAIL, password);
        editor.commit();
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        user.put(KEY_NAME, pref.getString(KEY_NAME, null));
        user.put(KEY_EMAIL, pref.getString(KEY_EMAIL, null));
        return user;
    }

    public void logoutUser() {
        editor.clear();
        editor.commit();
        Intent i = new Intent(_context, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(i);
    }

    // Get Login State
    public boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setSoftwareVersion(String softwareVersion) {
        editor.putString("softwareVersion", softwareVersion);
        editor.commit();
    }

    public String getSoftwareVersion() {
        return pref.getString("softwareVersion", "");
    }

    public void setEmpCode(String EmpCode) {
        editor.putString("EmpCode", EmpCode);
        editor.commit();
    }

    public String getEmpCode() {
        return pref.getString("EmpCode", "");
    }

    public void setUserName(String UserId) {
        editor.putString("UserId", UserId);
        editor.commit();
    }

    public String getUserName() {
        return pref.getString("UserId", "");
    }

    public void setWiFi(String wifi) {
        editor.putString("wifi", wifi);
        editor.commit();
    }

    public String getWiFi() {
        return pref.getString("wifi", "");
    }

    public void setBluetooth(String bluetooth) {
        editor.putString("bluetooth", bluetooth);
        editor.commit();
    }

    public String getBluetooth() {
        return pref.getString("bluetooth", "");
    }

    public void setProximity(String proximity) {
        editor.putString("proximity", proximity);
        editor.commit();
    }

    public String getProximity() {
        return pref.getString("proximity", "");
    }

    public void setKeysButtons(String keysButtons) {
        editor.putString("keysButtons", keysButtons);
        editor.commit();
    }

    public String getKeysButtons() {
        return pref.getString("keysButtons", "");
    }

    public void setVibration(String vibration) {
        editor.putString("vibration", vibration);
        editor.commit();
    }

    public String getVibration() {
        return pref.getString("vibration", "");
    }

    public void setFrontCamera(String frontCamera) {
        editor.putString("frontCamera", frontCamera);
        editor.commit();
    }

    public String getFrontCamera() {
        return pref.getString("frontCamera", "");
    }

    public void setFrontCameraImage(String frontCameraImage) {
        editor.putString("frontCameraImage", frontCameraImage);
        editor.commit();
    }

    public String getFrontCameraImage() {
        return pref.getString("frontCameraImage", "");
    }

    public void setRearCamera(String rearCamera) {
        editor.putString("rearCamera", rearCamera);
        editor.commit();
    }

    public String getRearCamera() {
        return pref.getString("rearCamera", "");
    }

    public void setRearCameraImage(String rearCameraImage) {
        editor.putString("rearCameraImage", rearCameraImage);
        editor.commit();
    }

    public String getRearCameraImage() {
        return pref.getString("rearCameraImage", "");
    }

    public void setAltraWideCamera(String altraWideCamera) {
        editor.putString("altraWideCamera", altraWideCamera);
        editor.commit();
    }

    public String getAltraWideCamera() {
        return pref.getString("altraWideCamera", "");
    }

    public void setAltraWideCameraImage(String altraWideCameraImage) {
        editor.putString("altraWideCameraImage", altraWideCameraImage);
        editor.commit();
    }

    public String getAltraWideCameraImage() {
        return pref.getString("altraWideCameraImage", "");
    }

    public void setLoudSpeaker(String loudSpeaker) {
        editor.putString("loudSpeaker", loudSpeaker);
        editor.commit();
    }

    public String getLoudSpeaker() {
        return pref.getString("loudSpeaker", "");
    }

    public void setMicrophone(String microphone) {
        editor.putString("microphone", microphone);
        editor.commit();
    }

    public String getMicrophone() {
        return pref.getString("microphone", "");
    }

    public void setEarpiece(String earpiece) {
        editor.putString("earpiece", earpiece);
        editor.commit();
    }

    public String getEarpiece() {
        return pref.getString("earpiece", "");
    }

    public void setLCDGlass(String lCDGlass) {
        editor.putString("lCDGlass", lCDGlass);
        editor.commit();
    }

    public String getLCDGlass() {
        return pref.getString("lCDGlass", "");
    }

    public void setLCDPixel(String lCDPixel) {
        editor.putString("LCDPixel", lCDPixel);
        editor.commit();
    }

    public String getLCDPixel() {
        return pref.getString("LCDPixel", "");
    }

    public void setDigitizer(String digitizer) {
        editor.putString("digitizer", digitizer);
        editor.commit();
    }

    public String getDigitizer() {
        return pref.getString("digitizer", "");
    }
}