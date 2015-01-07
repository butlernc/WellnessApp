package com.uwec.wellnessapp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;

/**
 * Created by butlernc on 12/2/2014.
 *
 * Used as static reference for basic user log information
 */
public class LoginHelper {

    private static boolean isLogged;

    public static boolean isLogged() {return isLogged;}

    public static void setLogged(boolean set) {isLogged = set;}

    /**
     * Used to log a user into the system
     *
     * @param email
     * @param password
     * @return boolean
     */
    public static boolean login(Context context, String email, String password, boolean rememberMe) {
        Log.d("LOGIN", "Start server setup");
        //create a FileSourceConnector, used to read and write to the server.
        FileSourceConnector fileSourceConnector = new FileSourceConnector();
        Statics.getSingleExecutor().push(fileSourceConnector.queue("readUser", email, password));

        /* TODO: Show progress */
        if(fileSourceConnector.getRETURN_STR().contains("GOOD")) {
            if(rememberMe) {
                Statics.getSessionData().setUsername(email);
                Statics.getSessionData().setPassword(password);
            }
            Statics.getSessionData().saveLoginSession(context, rememberMe);
            Statics.getSingleExecutor().runTask();
            return true;
        }
        return false;
    }

    /**
     * Used to start the Login Activity
     * @param current activity
     */
    public static void startLoginActivity(Activity current, String[] extras) {
        Intent intent = new Intent(current, LoginActivity.class);
        intent.putExtra("extras", extras);
        current.startActivity(intent);
    }
}
