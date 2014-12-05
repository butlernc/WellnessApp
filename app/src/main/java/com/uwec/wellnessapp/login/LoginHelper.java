package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.uwec.wellnessapp.utils.FileSourceConnector;

/**
 * Created by butlernc on 12/2/2014.
 *
 * Used as static reference for basic user log information
 */
public class LoginHelper {

    private static boolean isLogged;

    /**
     * FileIO Helper Object
     */
    private static FileSourceConnector fileSourceConnector;

    static {
        fileSourceConnector = new FileSourceConnector();
    }

    public LoginHelper(boolean isLogged) {
        this.isLogged = isLogged;
    }

    public static boolean isLogged() {
        return isLogged;
    }

    public static void setLogged(boolean set) {
        isLogged = set;
    }

    /**
     *TEST PUSH FROM ANDROID STUDIO
     * Used to log a user into the system
     *
     * @param email
     * @param password
     * @return boolean
     */
    public static boolean login(String email, String password) {
        Log.d("LOGIN", "Start server setup");
        new FileSourceConnector().execute(email, password, "read", "no");

        return isLogged;
    }

    /**
     * Used to start the Login Activity
     * @param current activity
     */
    public static void startLoginActivity(Activity current) {
        Intent intent = new Intent(current, LoginActivity.class);
        current.startActivity(intent);
    }
}
