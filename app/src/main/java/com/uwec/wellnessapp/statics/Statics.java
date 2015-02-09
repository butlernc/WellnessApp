package com.uwec.wellnessapp.statics;

import android.os.Handler;
import android.util.Log;

import com.uwec.wellnessapp.data.SessionData;
import com.uwec.wellnessapp.data.WeekData;
import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.data.WeeklyUserData;
import com.uwec.wellnessapp.login.LoginHelper;
import com.uwec.wellnessapp.register.RegisterHelper;
import com.uwec.wellnessapp.utils.Messenger;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * Created by Noah Butler on 12/23/2014.
 *
 * Used to hold the static objects in the app
 */
public class Statics {

    /* instantiated in Loading Activity */
    public static UserData globalUserData;
    public static ArrayList<WeekData> globalWeekDataList;
    public static SessionData sessionData;

    public static boolean appLoaded;

    public static Handler handler;
    public static Messenger messenger = new Messenger();

    public static String[] weeks = {"WEEK_ONE", "WEEK_TWO", "WEEK_THREE", "WEEK_FOUR", "WEEK_FIVE", "WEEK_SIX"};

    /**
     * Returns the correct weekData object for the current week.
     * @return
     */
    public static WeekData getCurrentWeekData() {
        return globalWeekDataList.get(sessionData.getWeekNumber() - 1);
    }

    /**
     * Returns the correct user's weekly data for the current week
     * @return
     */
    public static WeeklyUserData getUsersCurrentWeekData() {
        return globalUserData.getWeeklyData().get(sessionData.getWeekNumber() - 1);
    }
}
