package com.uwec.wellnessapp.statics;

import com.uwec.wellnessapp.data.SessionData;
import com.uwec.wellnessapp.data.WeekData;
import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.utils.SingleExecutor;

import java.util.concurrent.Executor;

/**
 * Created by Noah Butler on 12/23/2014.
 *
 * Used to hold the static objects in the app
 */
public class Statics {

    private static UserData globalUserData;
    private static WeekData globalWeekData;
    private static SessionData sessionData = new SessionData();

    private static SingleExecutor singleExecutor = new SingleExecutor();

    public static String[] weeks = {"WEEK_ONE", "WEEK_TWO", "WEEK_THREE", "WEEK_FOUR", "WEEK_FIVE"};

    public static UserData getGlobalUserData() {
        return globalUserData;
    }

    public static void setGlobalUserData(UserData globalUserData) {
        Statics.globalUserData = globalUserData;
    }

    public static WeekData getGlobalWeekData() {
        return globalWeekData;
    }

    public static void setGlobalWeekData(WeekData globalWeekData) {
        Statics.globalWeekData = globalWeekData;
    }

    public static SessionData getSessionData() { return sessionData; }

    public static SingleExecutor getSingleExecutor() {
        return singleExecutor;
    }
}
