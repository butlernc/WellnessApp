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

        public SaveLoginSession(Context context, boolean rememberMe) {
            this.context = context;
            this.rememberMe = rememberMe;
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
     * Starts a Thread (FileSourceConnector) that loads the correct WeekData
     * and then populates the WeekData object in the app
     *
     * Will be called when the app is in start up (loginActivity)
     * Session data should already be loaded
     */
    public class LoadWeekData extends Thread {

        public boolean isDone;

        public LoadWeekData() {
            isDone = false;
        }

        @Override
        public void run() {
            synchronized (this) {
                Log.d("WEEKDATA", "loading weekly data");
                FileSourceConnector fileSourceConnector = new FileSourceConnector();
                JSONObject startWeekDataJSON = null;
                String startWeekData = "";
                //TODO: fix
                //Statics.singleExecutor.runTask(fileSourceConnector.queue("readWeekStartData"));
                startWeekData = fileSourceConnector.getRETURN_STR();

                try {
                    startWeekDataJSON = new JSONObject(startWeekData);

                    int startOfWeek = startWeekDataJSON.getInt(Statics.weeks[0]);
                    int nextStartOfWeek = startWeekDataJSON.getInt(Statics.weeks[1]);

                    if (startWeekDataJSON != null) {

                        for (int i = 0; i < Statics.weeks.length; i++) {
                            Log.d("Week", "Week start dates: Week " + i + ": " + startWeekDataJSON.getInt(Statics.weeks[i]));
                        }

                        //if(currentSessionDate.get(Calendar.MONTH) == startWeekDataJSON.getInt("MONTH_ONE")) {
                    /* for testing purposes */
                        if (currentSessionDate.get(Calendar.MONTH) == 0) {
                            int index = 2;
                            weekNumber = 1;
                            while (!(currentDay >= startOfWeek && currentDay < nextStartOfWeek)) {
                                startOfWeek = nextStartOfWeek;
                                nextStartOfWeek = startWeekDataJSON.getInt(Statics.weeks[index]);
                                index++;
                                weekNumber++;
                            }
                            monthNumber = currentSessionDate.get(Calendar.MONTH);
                        } else {

                            int index = 2;
                            weekNumber = 1;
                            while (nextStartOfWeek > startOfWeek) {
                                startOfWeek = nextStartOfWeek;
                                nextStartOfWeek = startWeekDataJSON.getInt(Statics.weeks[index]);
                                index++;
                                weekNumber++;
                            }
                            while (!(currentDay >= startOfWeek && currentDay < nextStartOfWeek)) {
                                startOfWeek = nextStartOfWeek;
                                nextStartOfWeek = startWeekDataJSON.getInt(Statics.weeks[index]);
                                index++;
                                weekNumber++;
                            }

                            monthNumber = currentSessionDate.get(Calendar.MONTH);
                        }
                    }

                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector();
                /* TODO: fix week selection and this entire method lol */
                    //Statics.singleExecutor.runTask(loadWeekDataConnector.queue("readWeekData", "" + 1));
                    Log.d("DATE", "weekNumber: " + weekNumber);
                    Log.d("DATE", "monthNumber: " + monthNumber);
                    loadedWeekData = true;

                    Statics.messenger.sendMessage("loaded weekly data...");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            notify();
            isDone = true;
        }
    }

    public class LoadWeekDataList extends Thread {

        public boolean isDone;

        public LoadWeekDataList() {
            isDone = false;
        }

        @Override
        public void run() {
            synchronized (this) {
                FileSourceConnector loadWeekDataConnector = new FileSourceConnector();

                for (int i = 0; i < Statics.weeks.length; i++) {
                    Log.d("thread", "update: " + i);
                    loadWeekDataConnector.queue("readWeekData", "" + (i + 1));
                }

                Statics.messenger.sendMessage("Loaded all weekly data...");
                notify();
            }
            isDone = true;
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
