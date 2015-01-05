package com.uwec.wellnessapp.data;

import android.content.Context;

import java.io.File;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by butlernc on 12/9/2014.
 */
public class Current {

    private static String SESSION_FILE_NAME = "session_file.txt";

    private GregorianCalendar currentDate;
    private int weekNumber;

    /* used to check if we need to create a new weekly data object for the user */
    private boolean newWeek;

    //TODO: finish session date saving/saves the date the user logged in on
    //TODO: System pulls Goal Data off of the given date/write that method in here

    /* used to save login date */
    public void saveLoginSession(Context context) {
        File session_file = new File(context.getFilesDir(), SESSION_FILE_NAME);
        Date current = new Date();
        currentDate = new GregorianCalendar();
        currentDate.setTime(current);
    }

    /* used to load last login date */
    public void loadLastSession() {

    }

}
