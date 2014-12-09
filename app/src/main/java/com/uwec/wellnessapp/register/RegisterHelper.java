package com.uwec.wellnessapp.register;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.utils.FileSourceConnector;

/**
 * Created by Noah Butler on 12/5/2014.
 *
 * Small wrapper class that will call the will
 * start an async task that will call the server
 * and write our new user to it.
 *
 * also includes a method that will start our register activity
 * easily.
 */
public class RegisterHelper {


    /**
     * Used to start the Register Activity
     * @param current activity
     */
    public static void startRegisterActivity(Activity current) {
        Intent intent = new Intent(current, RegisterActivity.class);
        current.startActivity(intent);
    }

    public static boolean register(String...input) {
        UserData userData = new UserData();
        userData.setLast_name(input[0]);
        userData.setFirst_name(input[1]);
        userData.setEmail(input[2]);
        userData.setPassword(input[3]);
        userData.setTotal_score(0);

        FileSourceConnector.userData = userData;

        //create a FileSourceConnector, used to read and write to the server.
        FileSourceConnector fileSourceConnector = new FileSourceConnector();
        fileSourceConnector.execute(userData.getEmail(), userData.getPassword(), "write", "new");

        //wait until async task is over with because I can't do network operations on the
        //UI thread, so I have to use an async task.
        /* TODO: Show progress */
        while(!fileSourceConnector.isDone()) {}
        Log.e("DONE", "Made it here");
        return true;
    }


}
