package com.example.mobile.config;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import com.example.mobile.activity.LoginActivity;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class SessionManager {
    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "auth0";

    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";

    // User login (make variable public to access from outside)
    public static final String KEY_LOGIN = "login";

    // User token
    public static String KEY_TOKEN = "token";

    //User image path in server
    public static String KEY_IMAGE_PATH_SERVER = "imageServer";

    public static String KEY_EXPIRY_DATE="expiry_date";

    // Constructor
    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLoginSession(String login, String token, String expiryDate, String imageServerPath){
        // Storing login value as TRUE
        editor.putBoolean(IS_LOGIN, true);

        // Storing login in pref
        editor.putString(KEY_LOGIN, login);

        editor.putString(KEY_TOKEN, token);

        editor.putString(KEY_EXPIRY_DATE, expiryDate);

        editor.putString(KEY_IMAGE_PATH_SERVER, imageServerPath);

        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getUserDetails(){
        HashMap<String, String> user = new HashMap<>();
        user.put(KEY_LOGIN, pref.getString(KEY_LOGIN, null));
        user.put(KEY_TOKEN, pref.getString(KEY_TOKEN, null));
        user.put(KEY_IMAGE_PATH_SERVER, pref.getString(KEY_IMAGE_PATH_SERVER, null));
        return user;
    }

    public void checkLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        } else if(!checkdate()){
            logoutUser();
        }
    }

    public void logoutUser(){
        // Clearing all data from Shared Preferences
        editor.clear();
        editor.commit();

        // After logout redirect user to Loing Activity
        Intent intent = new Intent(_context, LoginActivity.class);
        // Closing all the Activities
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        // Add new Flag to start new Activity
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        // Staring Login Activity
        _context.startActivity(intent);
    }

    // Get Login State
    public boolean isLoggedIn(){
        return pref.getBoolean(IS_LOGIN, false);
    }

    public void setKeyImagePathServer(String path){
        editor.putString(KEY_IMAGE_PATH_SERVER, path);
        // commit changes
        editor.commit();
    }

    private boolean checkdate(){
        LocalDateTime expiry;
        LocalDateTime now;
        try {
            expiry = LocalDateTime.parse(pref.getString(KEY_EXPIRY_DATE, "").substring(0, pref.getString(KEY_EXPIRY_DATE, "").length() - 9));
            now = LocalDateTime.now();
        } catch (DateTimeException dateTimeException) {
            dateTimeException.printStackTrace();
            return false;
        }
        Duration duration = Duration.between(now, expiry);
        return duration.getSeconds() > 43200;
    }
}
