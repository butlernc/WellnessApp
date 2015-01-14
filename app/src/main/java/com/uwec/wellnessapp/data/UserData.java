package com.uwec.wellnessapp.data;

import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayList;

/**
 * Created by Noah Butler on 12/2/2014.
 * the global UserData object that will be used to store
 * the user's app data is stored in Statics
 */


public class UserData {

    private String first_name;
    private String last_name;

    private String email;
    private String password;

    private int total_score;

    private int weekly_score;

    private int weekNum;

    public int getWeekNum() {
        return weekNum;
    }

    public void setWeekNum(int weekNum) {
        this.weekNum = weekNum;
    }

    /* this will hold all of the data for each week */
    private ArrayList<WeeklyUserData> weeklyData = new ArrayList<>();

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getWeekly_score() {return weekly_score;}

    public void setWeekly_score(int weekly_score) {this.weekly_score = weekly_score;}

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public ArrayList<WeeklyUserData> getWeeklyData() {
        return weeklyData;
    }

    public void setWeeklyData(ArrayList<WeeklyUserData> weeklyData) {
        this.weeklyData = weeklyData;
    }

}
