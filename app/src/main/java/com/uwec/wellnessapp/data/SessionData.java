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

    private boolean firstTime;

    /* used to pause the user's progress through the app if these data sets are not loaded */
    public boolean bonusDataLoaded;
    public boolean startWeekDataLoaded;

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
                currentMonth = currentSessionDate.get(Calendar.MONTH);
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

                    data.put("username", Statics.globalUserData.getEmail());
                    data.put("password", Statics.globalUserData.getPassword());

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

                        //if (data.getInt("rememberMe") == 1) {
                        //    setUsername(data.getString("username"));
                        //    setPassword(data.getString("password"));
                        //}

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
                JSONObject parentWeekDataJSON;
                /* split up the the start dates by the months they are in, each month that has start
                dates will have a respective JSONObject in this ArrayList
                 */
                ArrayList<JSONObject> months = new ArrayList<>();

                try {
                    /* create the json object from the loaded string */
                    parentWeekDataJSON = (JSONObject)fileSourceConnector.queue("readWeekStartData");

                    /* populate our months array with JSONObjects that hold the start days of that month */
                    for(int i = 0; i < parentWeekDataJSON.getInt("month_amount"); i++) {
                        months.add(parentWeekDataJSON.getJSONObject("month_" + i));
                    }
                    /* get the correct month */
                    JSONObject currentMonthJSON = null;
                    /* grab the correct json object out of our array so we can get the correct list
                     * of start dates.
                     */
                    for(int i = 0; i < months.size(); i++) {
                        if(currentMonth == months.get(i).getInt("month")) {
                            currentMonthJSON = months.get(i);
                            break;
                        }
                    }
                    if(currentMonthJSON != null) {
                        /* get the correct week number by comparing the currentDay with the currentMonthJSON start dates. */
                        for(int i = currentMonthJSON.getInt("week_amount"); i >= 1; i++) {
                            if(currentDay >= currentMonthJSON.getJSONObject(""+i).getInt("week_date")){
                                weekNumber = currentMonthJSON.getJSONObject(""+i).getInt("week_num");
                                break;
                            }
                        }
                        monthNumber = currentMonth;
                    }else{
                        weekNumber = 1;
                        monthNumber = currentMonth;
                    }


                    Log.d("DATE", "weekNumber: " + weekNumber);
                    Log.d("DATE", "monthNumber: " + monthNumber);

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
            /* six steps of progress to finish */
            progressIncrease = ((1/6.0) * 100);
            /* when true, the data for all of the weeks is finished loading */
            allFinished = false;
            bonusDataLoaded = false;
            startWeekDataLoaded = true;
            /* each signifies when a week's data is finished loading */
            finished = new ArrayList<>();
            for(int i = 0; i < 6; i++) {
                finished.add(false);
            }
        }

        @Override
        public void run() {

            Statics.messenger.sendMessage("Loaded all weekly data...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    boolean completed = (Boolean)loadWeekDataConnector.queue("readWeekData", "" + 1);
                    Statics.messenger.sendProgress(progressIncrease);
                    finished.add(0, completed);
                }
            }).start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread load2 = new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    boolean completed = (Boolean)loadWeekDataConnector.queue("readWeekData", "" + 2);
                    Statics.messenger.sendProgress(progressIncrease);
                    finished.add(1, completed);
                }
            });
            while(!finished.get(0)){}
            load2.start();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Thread load3 = new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    boolean completed = (Boolean)loadWeekDataConnector.queue("readWeekData", "" + 3);
                    Statics.messenger.sendProgress(progressIncrease);
                    finished.add(2, completed);
                }
            });
            while(!finished.get(1)){}
            load3.start();

            Thread load4 = new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    boolean completed = (Boolean)loadWeekDataConnector.queue("readWeekData", "" + 4);
                    Statics.messenger.sendProgress(progressIncrease);
                    finished.add(3, completed);
                }
            });
            while(!finished.get(2)){}
            load4.start();

            Thread load5 = new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    boolean completed = (Boolean)loadWeekDataConnector.queue("readWeekData", "" + 5);
                    Statics.messenger.sendProgress(progressIncrease);
                    finished.add(4, completed);
                }
            });
            while(!finished.get(3)){}
            load5.start();

            Thread load6 = new Thread(new Runnable() {
                @Override
                public void run() {
                    FileSourceConnector loadWeekDataConnector = new FileSourceConnector(context);
                    boolean completed = (Boolean)loadWeekDataConnector.queue("readWeekData", "" + 6);
                    Statics.messenger.sendProgress(progressIncrease);
                    finished.add(5, completed);
                   }
            });
            while(!finished.get(4)){}
            load6.start();

            while(!allFinished) {
                checkIfFinished(finished);
            }

            /* load the start dates info for the program */
            LoadStartWeekData loadStartWeekDataThread = createLoadStartWeekDataThread(context);
            loadStartWeekDataThread.start();

            /* load the bonus points data for the app */
            LoadBonusDataThread loadBonusDataThread = createLoadBonusDataThread(context);
            loadBonusDataThread.start();
            while(!bonusDataLoaded && !startWeekDataLoaded){}
            Statics.appLoaded = true;
        }

        private void checkIfFinished(ArrayList<Boolean> finished) {
            int tally = 0;
            for(int i = 0; i < finished.size(); i++) {
                if(finished.get(i)) {
                    tally++;
                }
            }

            if(tally == 6) {
                allFinished = true;
            }
        }

    }
    public LoadWeekDataList createLoadWeekDataListThread(Context context) {
        return new LoadWeekDataList(context);
    }

    /**
     * Loads all of the Bonus Point static data that the app will display
     * in the Bonus Activity Fragment. Called in LoadWeekDataList at the end.
     */
    public class LoadBonusDataThread extends Thread {

        Context context;

        public LoadBonusDataThread(Context context) {
            this.context = context;
        }

        @Override
        public void run() {
            FileSourceConnector fileSourceConnector = new FileSourceConnector(context);
            fileSourceConnector.queue("readBonusData");
            Statics.sessionData.bonusDataLoaded = true;
        }
    }
    public LoadBonusDataThread createLoadBonusDataThread(Context context) {
        return new LoadBonusDataThread(context);
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
