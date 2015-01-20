package com.uwec.wellnessapp.register;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.data.WeeklyUserData;
import com.uwec.wellnessapp.login.LoginActivity;
import com.uwec.wellnessapp.login.LoginHelper;
import com.uwec.wellnessapp.statics.Statics;
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

    public void register(Activity activity, String...input) {
        UserData userData = new UserData();
        userData.setLast_name(input[0]);
        userData.setFirst_name(input[1]);
        userData.setEmail(input[2]);
        userData.setPassword(input[3]);
        userData.setTotal_score(0);

        for(int i = 0; i < Statics.weeks.length; i++) {
            userData.getWeeklyData().add(new WeeklyUserData(i));
        }

        Statics.globalUserData = userData;
        //create a FileSourceConnector, used to read and write to the server.
        FileSourceConnector fileSourceConnector = new FileSourceConnector();
        //TODO: fix, needs to run off of the UI thread
        fileSourceConnector.queue("writeUser", userData.getEmail(), "new");
        /* TODO: Show progress */
        Log.e("DONE", "Made it here");

        if(fileSourceConnector.getRETURN_STR().contentEquals("GOOD")) {
            Toast.makeText(activity, "Account creation successful!", Toast.LENGTH_LONG).show();
            String[] extras = {"!load"};
            LoginHelper.startLoginActivity(activity, extras);

        }else if(fileSourceConnector.getRETURN_STR().contentEquals("NOGOOD")) {
            //TODO: registering didn't work, write this code
        }
    }


}
