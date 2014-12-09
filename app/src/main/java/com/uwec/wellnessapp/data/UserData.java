package com.uwec.wellnessapp.data;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Noah Butler on 12/2/2014.
 * test
 */


public class UserData {

    private String first_name;
    private String last_name;

    private String email;
    private String password;

    private int total_score;

    private HashMap<Integer, List<Integer>> point_breakdown;

    public UserData() {}

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

    public int getTotal_score() {
        return total_score;
    }

    public void setTotal_score(int total_score) {
        this.total_score = total_score;
    }

    public HashMap<Integer, List<Integer>> getPoint_breakdown() {
        return point_breakdown;
    }

    public void setPoint_breakdown(HashMap<Integer, List<Integer>> point_breakdown) {
        this.point_breakdown = point_breakdown;
    }

}
