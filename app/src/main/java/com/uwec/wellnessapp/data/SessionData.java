package com.uwec.wellnessapp.data;

import android.content.Context;
import android.util.Log;

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
    private GregorianCalendar lastSessionDate;
    private int weekNumber;

    private boolean rememberMe;
    private String Username;
    private String Password;

    /** used to check if we need to create a new weekly data object for the user */
    private boolean newWeek;

    //TODO: finish session date saving/saves the date the user logged in on
    //TODO: System pulls WeekData off of the given date/write that method in here

    /** used to save login date */
    public void saveLoginSession(Context context, boolean rememberMe) {
        Log.d("Save", "saving login session data");
        Date current = new Date();
        currentSessionDate = new GregorianCalendar();
        currentSessionDate.setTime(current);
        Log.d("DATE", "Year: " + currentSessionDate.get(Calendar.YEAR));
        Log.d("DATE", "Month: " + currentSessionDate.get(Calendar.MONTH));
        Log.d("DATE", "Day: " + currentSessionDate.get(Calendar.DAY_OF_WEEK));
        Log.d("DATE", "Day: " + currentSessionDate.get(Calendar.DAY_OF_MONTH));
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
    }

    /** used to load last login date */
    public void loadLastSession(Context context) {
        String rawData = "";
        StringBuilder sb = new StringBuilder();

        JSONObject data;
        InputStream inputStream;

        try{
            inputStream = context.openFileInput(SESSION_FILE_NAME);
            BufferedReader dataReader = new BufferedReader(new InputStreamReader(inputStream));

            while((rawData = dataReader.readLine()) != null) {
                sb.append(rawData);
            }

            data = new JSONObject(sb.toString());
            lastSessionDate = new GregorianCalendar();
            lastSessionDate.set(Integer.parseInt("" + data.get("year")), Integer.parseInt("" + data.get("month")), Integer.parseInt("" + data.get("day")));

            if(Integer.parseInt(""+data.get("rememberMe")) == 1) {
                shouldRememberMe(true);
                setUsername(""+data.get("username"));
                setPassword(""+data.get("password"));
            }

            Log.d("Session", "Data: Year: " + data.get("year"));
            Log.d("Session", "Data: Month: " + data.get("month"));
            Log.d("Session", "Data: Day Of Month: " + data.get("day"));
            Log.d("Session", "Data: Username: " + data.get("username"));
            Log.d("Session", "Data: rememberMe: " + data.get("rememberMe"));
        }catch (Exception e) {
            e.printStackTrace();
        }

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
}
