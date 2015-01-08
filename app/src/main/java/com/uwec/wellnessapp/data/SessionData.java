package com.uwec.wellnessapp.data;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.uwec.wellnessapp.statics.Statics;
import com.uwec.wellnessapp.utils.FileSourceConnector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by butlernc on 12/9/2014.
 */
public class SessionData {

    private static String SESSION_FILE_NAME = "session_file.txt";

    private GregorianCalendar currentSessionDate;
    private int currentDay;
    private int lastSessionDay;
    private int lastSessionMonth;

    private int weekNumber;
    private int monthNumber;

    private boolean rememberMe;
    private String Username;
    private String Password;

    /** used to check if we need to create a new weekly data object for the user */
    private boolean newWeek;
    private boolean loadedWeekData = false;

    //TODO: finish session date saving/saves the date the user logged in on
    //TODO: System pulls WeekData off of the given date/write that method in here

    /** used to setup this class when app starts */
    public void setupSession() {
        Date current = new Date();
        currentSessionDate = new GregorianCalendar();
        currentSessionDate.setTime(current);
        currentDay = currentSessionDate.get(Calendar.DAY_OF_MONTH);
    }

    /** used to save login date */
    public void saveLoginSession(final Context context, final boolean rememberMe) {

        Runnable runnable = new Runnable() {

            @Override
            public void run() {
                JSONObject data = new JSONObject();
                try {
                    data.put("year", currentSessionDate.get(Calendar.YEAR));
                    data.put("month", currentSessionDate.get(Calendar.MONTH));
                    data.put("day", currentSessionDate.get(Calendar.DAY_OF_MONTH));

                    if(rememberMe) {
                        data.put("username", getUsername());
                        data.put("password", getPassword());
                        data.put("rememberMe", 1);
                        Log.d("Save", "Login data will be saved for next login");

                    }else{
                        data.put("rememberMe", 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String rawData = data.toString();
                FileOutputStream outputStream;

                try{
                    outputStream = context.openFileOutput(SESSION_FILE_NAME, Context.MODE_PRIVATE);
                    outputStream.write(rawData.getBytes());
                    outputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Statics.messenger.sendMessage("New Session Created.");
            }
        };

        Statics.singleExecutor.runTask(runnable);
    }

    /** used to load last login date */
    public void loadLastSession(final Context context) {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                String rawData = "";
                StringBuilder sb = new StringBuilder();

                JSONObject data;
                InputStream inputStream;

                try {
                    inputStream = context.openFileInput(SESSION_FILE_NAME);
                    BufferedReader dataReader = new BufferedReader(new InputStreamReader(inputStream));

                    while ((rawData = dataReader.readLine()) != null) {
                        sb.append(rawData);
                    }

                    data = new JSONObject(sb.toString());

                    lastSessionMonth = data.getInt("month");
                    lastSessionDay = data.getInt("day");

                    if (Integer.parseInt("" + data.get("rememberMe")) == 1) {
                        shouldRememberMe(true);
                        setUsername("" + data.get("username"));
                        setPassword("" + data.get("password"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();

                }

                Statics.messenger.sendMessage("Loaded Cached Data");
            }
        };

        Statics.singleExecutor.runTask(runnable);
    }

    /**
     * Starts an asyncTask (FileSourceConnector) that loads the correct WeekData
     * and then populates the WeekData object in the app
     *
     * Will be called when the app is in start up (loginActivity)
     * Session data should already be loaded
     */
    public void loadWeekData() {

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Log.d("WEEKDATA", "loading weekly data");
                FileSourceConnector fileSourceConnector = new FileSourceConnector();
                JSONObject startWeekDataJSON = null;
                String startWeekData = "";

                Statics.singleExecutor.runTask(fileSourceConnector.queue("readWeekStartData"));
                /* wait */
                while(!fileSourceConnector.isDone()) {}
                startWeekData = fileSourceConnector.getRETURN_STR();

                try {
                    startWeekDataJSON = new JSONObject(startWeekData);

                    if(startWeekDataJSON != null) {

                        int startOfWeek = startWeekDataJSON.getInt(Statics.weeks[0]);
                        int nextStartOfWeek = startWeekDataJSON.getInt(Statics.weeks[1]);

                        //if(currentSessionDate.get(Calendar.MONTH) == startWeekDataJSON.getInt("MONTH_ONE")) {
                        /* for testing purposes */
                        if(currentSessionDate.get(Calendar.MONTH) == 0) {
                            int index = 2;

                            while(!(currentDay >= startOfWeek && currentDay < nextStartOfWeek)) {
                                startOfWeek = nextStartOfWeek;
                                nextStartOfWeek = startWeekDataJSON.getInt(Statics.weeks[index]);
                                index++;
                            }

                            weekNumber  = startOfWeek;
                            monthNumber = currentSessionDate.get(Calendar.MONTH);
                        }else{

                            int index = 2;
                            while(nextStartOfWeek > startOfWeek) {
                                startOfWeek = nextStartOfWeek;
                                nextStartOfWeek = startWeekDataJSON.getInt(Statics.weeks[index]);
                                index++;
                            }
                            while(!(currentDay >= startOfWeek && currentDay < nextStartOfWeek)) {
                                startOfWeek = nextStartOfWeek;
                                nextStartOfWeek = startWeekDataJSON.getInt(Statics.weeks[index]);
                                index++;
                            }

                            weekNumber  = startOfWeek;
                            monthNumber = currentSessionDate.get(Calendar.MONTH);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                FileSourceConnector loadWeekDataConnector = new FileSourceConnector();
                /* TODO: fix week selection */
                Statics.singleExecutor.runTask(loadWeekDataConnector.queue("readWeekData", "" + 1));
                while(!loadWeekDataConnector.isDone()){}
                Log.d("DATE", "weekNumber: " + weekNumber);
                Log.d("DATE", "monthNumber: " + monthNumber);
                loadedWeekData = true;

                Statics.messenger.sendMessage("Loaded Weekly Data...");
            }
        };

        Statics.singleExecutor.runTask(runnable);
    }

    public boolean rememberedMe() {
        return rememberMe;
    }

    public void shouldRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public int getWeekNumber() {
        return weekNumber;
    }

    public int getMonthNumber() {
        return monthNumber;
    }

    public boolean loadedWeekData() {
        return loadedWeekData;
    }
}
