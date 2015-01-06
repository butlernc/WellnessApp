package com.uwec.wellnessapp.statics;

import com.uwec.wellnessapp.data.SessionData;
import com.uwec.wellnessapp.data.WeekData;
import com.uwec.wellnessapp.data.UserData;

/**
 * Created by Noah Butler on 12/23/2014.
 *
 * Used to hold the static objects in the app
 */
public class Statics {

    private static UserData globalUserData;
    //TODO: rename to WeekData
    private static WeekData globalWeekData;
    private static SessionData sessionData = new SessionData();

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

    public static void setSessionData(SessionData sessionData) {
        Statics.sessionData = sessionData;
    }
}
