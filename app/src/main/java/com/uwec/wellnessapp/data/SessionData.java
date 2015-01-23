package com.uwec.wellnessapp.data;

import android.content.Context;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Noah Butler on 12/9/2014.
 *
 * Controls the data for the user's "session".
 * Hold sub classes that all extend the Thread Class
 *
 * Basic functionality of the ParentClass (SessionData)
 * is to control whether the user wants to auto login or not
 * the next time they start the app.
 */
public class SessionData {

    private static String SESSION_FILE_NAME = "session_file.txt";

    /* helper object that will get the currentDay for us */
    private GregorianCalendar currentSessionDate;
    /* used as the current day of the month */
    private int currentDay;
    private int currentMonth;
    /* TODO: figure out if we need this our not */
    private int lastSessionDay;
    private int lastSessionMonth;

    /* known as the correct weekNumber and is used to select the correct data when
    the week number is needed
     */
    private int weekNumber;
    /* TODO: make sure this used in the app */
    private int monthNumber;

    private boolean rememberMe;
    /* TODO: probably could just pull this from the User object */
    private String Username;
    private String Password;

    /* TODO: not sure if we need to check for new week, most likely not */
    private boolean newWeek;
    /* TODO: check if this variable is being used at all */
    private boolean loadedWeekData = false;

    //TODO: finish session date saving/saves the date the user logged in on
    //TODO: System pulls WeekData off of the given date/write that method in here

    /** used to setup this class when app starts */
    public class SetupSession extends Thread {

        public boolean isDone;

        public SetupSession() {
            isDone = false;
        }

        @Override
        public void run() {
            synchronized (this) {
                Date current = new Date();
                currentSessionDate = new GregorianCalendar();
                currentSessionDate.setTime(current);
                currentDay = currentSessionDate.get(Calendar.DAY_OF_MONTH);
                notify();
                isDone = true;
            }
        }
    }
    public SetupSession createSetupSession() {
        return new SetupSession();
    }

    /** used to save login date */
    public class SaveLoginSession extends Thread {

        Context context;
        boolean rememberMe;

        public boolean isDone;

        public SaveLoginSession(Context context, boolean rememberMe) {
            this.context = context;
            this.rememberMe = rememberMe;
            isDone = false;
        }

        @Override
        public void run() {
            synchronized (this) {
                JSONObject data = new JSONObject();
                try {
                    data.put("year", currentSessionDate.get(Calendar.YEAR));
                    data.put("month", currentSessionDate.get(Calendar.MONTH));
                    data.put("day", currentSessionDate.get(Calendar.DAY_OF_MONTH));

                    if (rememberMe) {
                        data.put("username", getUsername());
                        data.put("password", getPassword());
                        data.put("rememberMe", 1);
                        Log.d("Save", "Login data will be saved for next login");

                    } else {
                        data.put("rememberMe", 0);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String rawData = data.toString();
                FileOutputStream outputStream;

                try {
                    outputStream = context.openFileOutput(SESSION_FILE_NAME, Context.MODE_PRIVATE);
                    outputStream.write(rawData.getBytes());
                    outputStream.close();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Statics.messenger.sendMessage("new session created...");
                notify();
                isDone = true;
            }
        }
    }
    public SaveLoginSession createSaveLoginSessionThread(Context context, boolean rememberMe) {
        return new SaveLoginSession(context, rememberMe);
    }

    /** used to load last login date */
    public class LoadLastSession extends Thread {

        Context context;
        public boolean isDone;

        public LoadLastSession(Context context) {
            this.context = context;
            isDone = false;
        }

        @Override
        public void run() {
            synchronized (this) {
                String rawData = "";
                StringBuilder sb = new StringBuilder();

                JSONObject data;
                InputStream inputStream;

                File file = new File(context.getFilesDir(), SESSION_FILE_NAME);
                if (file.exists()) {

                    try {
                        inputStream = context.openFileInput(SESSION_FILE_NAME);
                        BufferedReader dataReader = new BufferedReader(new InputStreamReader(inputStream));

                        while ((rawData = dataReader.readLine()) != null) {
                            sb.append(rawData);
                        }

                        data = new JSONObject(sb.toString());

                        Statics.sessionData.lastSessionMonth = data.getInt("month");
                        Statics.sessionData.lastSessionDay = data.getInt("day");

                        if (data.getInt("rememberMe") == 1) {
                            shouldRememberMe(true);
                            setUsername(data.getString("username"));
                            setPassword(data.getString("password"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }

                Statics.messenger.sendMessage("loaded old session data...");

                notify();
                isDone = true;
            }

        }
    }
    public LoadLastSession createLoadLastSession(Context context) {
        return new LoadLastSession(context);
    }

    /**
     * Starts a thread that will load the dates that the weeks will start on.
     * This allows the committee to change them whenever they want with the admin program.
     *
     * TODO: make sure the json for the week start data is correct
     */
    public class LoadStartWeekData extends Thread {

        public boolean isDone;

        public LoadStartWeekData() {
            isDone = false;
        }

        @Override
        public void run() {
            synchronized (this) {
                Log.d("WEEKDATA", "loading weekly data");
                FileSourceConnector fileSourceConnector = new FileSourceConnector();
                JSONObject startWeekDataJSON = null;
                /* split up the the start dates by the months they are in, each month that has start
                dates will have a respective JSONObject in this ArrayList
                 */
                ArrayList<JSONObject> months = new ArrayList<>();
                /* load in the Parent JSON object that holds all of the months and the start dates
                that are in those months.
                 */
                fileSourceConnector.queue("readWeekStartData");
                String startWeekData = fileSourceConnector.getRETURN_STR();
                try {
                    /* create the json object from the loaded string */
                    startWeekDataJSON = new JSONObject(startWeekData);

                    /* populate our months array with JSONObjects that hold the start days of that month */
                    for(int i = 0; i < startWeekDataJSON.getInt("month_amount"); i++) {
                        months.add(startWeekDataJSON.getJSONObject("month_" + i));
                    }
                    /* get the correct month */
                    JSONObject currentMonthJSON;
                    int monthIndex = 0;
                    while(currentMonth > months.get(monthIndex).getInt("month")) {
                        monthIndex++;
                    }
                    /* grap the correct json object out of our array so we can get the correct list
                     * of start dates.
                     */
                    currentMonthJSON = months.get(monthIndex);

                    int startOfWeek = currentMonthJSON.getInt("0");
                    int nextStartOfWeek = startWeekDataJSON.getInt("1");

                    for (int i = 0; i < Statics.weeks.length; i++) {
                        Log.d("Week", "Length: " + Statics.weeks.length);
                        Log.d("Week", "Week start dates: Week " + i + ": " + startWeekDataJSON.getInt(Statics.weeks[i]));
                    }

                    int index = 2;
                    /* TODO: fix so the weekNumber is counted correctly, if we're in the second
                    month, load add the amount of weeks that were in the first month.
                     */
                    weekNumber = 1;
                    while (!(currentDay >= startOfWeek && currentDay < nextStartOfWeek)) {
                        startOfWeek = nextStartOfWeek;
                        nextStartOfWeek = startWeekDataJSON.getInt("" + index);
                        index++;
                        weekNumber++;
                    }

                    monthNumber = currentSessionDate.get(Calendar.MONTH);


                    Log.d("DATE", "weekNumber: " + weekNumber);
                    Log.d("DATE", "monthNumber: " + monthNumber);
                    loadedWeekData = true;

                    Statics.messenger.sendMessage("loaded weekly data...");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                notify();
                isDone = true;
            }

        }
    }
    public LoadStartWeekData createLoadStartWeekDataThread() {
        return new LoadStartWeekData();
    }

    /**
     * Starts a Thread that loads the correct WeekData (basically all of the challenge info.
     * for that week) and adds it to an ArrayList<WeekData> which is located in the Statics Class.
     *
     * Will be called when the app is in start up (LoadingActivity)
     * Session data should already be loaded since it relies on this.
     */
    public class LoadWeekDataList extends Thread {

        public boolean isDone;

        public LoadWeekDataList() {
            isDone = false;
        }

        @Override
        public void run() {
            synchronized (this) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileSourceConnector loadWeekDataConnector = new FileSourceConnector();
                        loadWeekDataConnector.queue("readWeekData", "" + 1);
                    }
                }).start();
                try {
                    wait(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileSourceConnector loadWeekDataConnector = new FileSourceConnector();
                        loadWeekDataConnector.queue("readWeekData", "" + 2);
                    }
                }).start();
                try {
                    wait(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileSourceConnector loadWeekDataConnector = new FileSourceConnector();
                        loadWeekDataConnector.queue("readWeekData", "" + 3);
                    }
                }).start();
                try {
                    wait(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileSourceConnector loadWeekDataConnector = new FileSourceConnector();
                        loadWeekDataConnector.queue("readWeekData", "" + 4);
                    }
                }).start();
                try {
                    wait(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileSourceConnector loadWeekDataConnector = new FileSourceConnector();
                        loadWeekDataConnector.queue("readWeekData", "" + 5);
                    }
                }).start();
                try {
                    wait(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        FileSourceConnector loadWeekDataConnector = new FileSourceConnector();
                        loadWeekDataConnector.queue("readWeekData", "" + 6);
                    }
                }).start();

                Statics.messenger.sendMessage("Loaded all weekly data...");
                notify();
                isDone = true;
            }

        }
    }
    public LoadWeekDataList createLoadWeekDataListThread() {
        return new LoadWeekDataList();
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
