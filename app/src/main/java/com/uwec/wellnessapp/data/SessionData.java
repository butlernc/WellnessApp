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

    /* TODO: probably could just pull this from the User object */
    private String Username;
    private String Password;
    private boolean firstTime;

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

                    data.put("username", getUsername());
                    data.put("password", getPassword());

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
                    firstTime = false;
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
                            setUsername(data.getString("username"));
                            setPassword(data.getString("password"));
                        }

                    } catch (Exception e) {
                        e.printStackTrace();

                    }
                }else{
                    firstTime = true;
                }
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
        private Context context;

        public LoadStartWeekData(Context context) {
            isDone = false;
            this.context = context;
        }

        @Override
        public void run() {
            synchronized (this) {
                Log.d("WEEKDATA", "loading weekly data");
                FileSourceConnector fileSourceConnector = new FileSourceConnector(context);
                JSONObject startWeekDataJSON = null;
                /* split up the the start dates by the months they are in, each month that has start
                dates will have a respective JSONObject in this ArrayList
                 */
                ArrayList<JSONObject> months = new ArrayList<>();
                /* load in the Parent JSON object that holds all of the months and the start dates
                that are in those months. File name on FTP server: /wellnessappftp.eu.pn/weekData/new_week_info.txt
                 */

                try {
                    /* create the json object from the loaded string */
                    startWeekDataJSON = (JSONObject)fileSourceConnector.queue("readWeekStartData");

                    /* populate our months array with JSONObjects that hold the start days of that month */
                    for(int i = 0; i < startWeekDataJSON.getInt("month_amount"); i++) {
                        months.add(startWeekDataJSON.getJSONObject("month_" + i));
                    }
                    /* get the correct month */
                    JSONObject currentMonthJSON;
                    int monthIndex = 0;
                    /* TODO: fix this */
                    while(currentMonth > months.get(monthIndex).getInt("month")) {
                        monthIndex++;
                    }
                    /* grab the correct json object out of our array so we can get the correct list
                     * of start dates.
                     */
                    currentMonthJSON = months.get(monthIndex);
                    int weekIndex = 1;
                    int startOfWeek = currentMonthJSON.getJSONObject(String.valueOf(weekIndex)).getInt("week_date");
                    int nextStartOfWeek = currentMonthJSON.getJSONObject(String.valueOf(weekIndex)).getInt("week_date");

                    while (!(currentDay >= startOfWeek && currentDay < nextStartOfWeek) && weekIndex <= currentMonthJSON.getInt("week_amount")) {
                        startOfWeek = nextStartOfWeek;
                        nextStartOfWeek = currentMonthJSON.getJSONObject(String.valueOf(weekIndex)).getInt("week_date");
                        weekIndex++;

                    }

                    weekNumber = currentMonthJSON.getJSONObject(String.valueOf(weekIndex - 1)).getInt("week_num");
                    monthNumber = currentSessionDate.get(Calendar.MONTH);


                    Log.d("DATE", "weekNumber: " + weekNumber);
                    Log.d("DATE", "monthNumber: " + monthNumber);

                    Statics.messenger.sendMessage("loaded weekly data...");

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                notify();
                isDone = true;
            }

        }
    }
    public LoadStartWeekData createLoadStartWeekDataThread(Context context) {
        return new LoadStartWeekData(context);
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
        private Context context;
        private double progressIncrease;
        private ArrayList<Boolean> finished;
        private boolean allFinished;

        public LoadWeekDataList(Context context) {
            isDone = false;
            this.context = context;
            progressIncrease = ((1/6.0) * 100);
            allFinished = false;
            finished = new ArrayList<>();
        }

        @Override
        public void run() {

            Statics.messenger.sendMessage("Loaded all weekly data...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    finished.add((Boolean)loadWeekDataConnector.queue("readWeekData", "" + 1));
                    Statics.messenger.sendProgress(progressIncrease);
                }
            }).start();

            try{
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    finished.add((Boolean)loadWeekDataConnector.queue("readWeekData", "" + 2));
                    Statics.messenger.sendProgress(progressIncrease);
                }
            }).start();

            try{
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    finished.add((Boolean)loadWeekDataConnector.queue("readWeekData", "" + 3));
                    Statics.messenger.sendProgress(progressIncrease);
                }
            }).start();

            try{
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    finished.add((Boolean)loadWeekDataConnector.queue("readWeekData", "" + 4));
                    Statics.messenger.sendProgress(progressIncrease);
                }
            }).start();

            try{
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    finished.add((Boolean)loadWeekDataConnector.queue("readWeekData", "" + 5));
                    Statics.messenger.sendProgress(progressIncrease);
                }
            }).start();

            try{
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }

            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    finished.add((Boolean)loadWeekDataConnector.queue("readWeekData", "" + 6));
                    Statics.messenger.sendProgress(progressIncrease);
                }
            }).start();

            try{
                Thread.sleep(1000);
            }catch(InterruptedException e) {
                e.printStackTrace();
            }

            while(!allFinished) {
                checkIfFinished(finished);
            }
            Statics.appLoaded = true;
            LoadStartWeekData loadStartWeekDataThread = Statics.sessionData.createLoadStartWeekDataThread(context);
            loadStartWeekDataThread.start();
        }

        private void checkIfFinished(ArrayList<Boolean> finished) {
            int tally = 0;
            for(int i = 0; i < finished.size(); i++) {
                if(finished.get(i)) {
                    tally++;
                }
            }

            if(tally == finished.size()) {
                allFinished = true;
            }
        }
    }
    public LoadWeekDataList createLoadWeekDataListThread(Context context) {
        return new LoadWeekDataList(context);
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

    public boolean isFirstTime() {
        return firstTime;
    }
}
