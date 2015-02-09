package com.uwec.wellnessapp.data;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by butlernc on 12/9/2014.
 *
 * Used to save the current week's data.
 */
public class WeekData {

    private String week;

    /** name of the current week's physical activity */
    private String physical_activity;
    /** used to store the desc of the physical activity */
    private String physical_activity_description;
    private int pa_link_amount;
    private ArrayList<String> pa_links;
    /** int used to keep track of physical activity check offs there are
      * this will set the physicalGoalCheckOffAmount in the WeeklyData object in UserData */
    private int pa_days_per_week;
    private ArrayList<String> pa_strings;

    /** name of the current week's nutrition goal */
    private String nutrition_goal;
    /** detailed desc of the current week's nutrition goal */
    private String nutrition_goal_description;
    private int ng_link_amount;
    private ArrayList<String> ng_links;
    /** int used to keep track of nutrition goal check offs there are for the current week
      * this will set the nutritionalGoalCheckOffAmount in the WeeklyData object in UserData */
    private int ng_days_per_week;
    private ArrayList<String> ng_strings;

    /** Link that directs users to a youtube for the current suggested workout of the current week */
    private String suggestedWorkoutLink;
    /** one word desc. of the suggested workout for the current week. */
    private String suggestedWorkoutType;
    /** Short string that holds the dates for the current week, format:"month start_date - end_date" */
    private String weekDates;


    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week;
    }

    public String getPhysical_activity() {
        return physical_activity;
    }

    public void setPhysical_activity(String physical_activity) {
        this.physical_activity = physical_activity;
    }

    public String getPhysical_activity_description() {return physical_activity_description; }

    public void setPhysical_activity_description(String physical_activity_description) {
        this.physical_activity_description = physical_activity_description;
    }

    public int getPa_days_per_week() {
        return pa_days_per_week;
    }

    public void setPa_days_per_week(int pa_days_per_week) {
        this.pa_days_per_week = pa_days_per_week;
    }

    public String getNutrition_goal() {
        return nutrition_goal;
    }

    public void setNutrition_goal(String nutrition_goal) {
        this.nutrition_goal = nutrition_goal;
    }

    public int getNg_days_per_week() {
        return ng_days_per_week;
    }

    public void setNg_days_per_week(int ng_days_per_week) {
        this.ng_days_per_week = ng_days_per_week;
    }

    public String getNutrition_goal_description() {
        return nutrition_goal_description;
    }

    public void setNutrition_goal_description(String nutrition_goal_description) {
        this.nutrition_goal_description = nutrition_goal_description;
    }

    public ArrayList<String> getPa_strings() {
        return pa_strings;
    }

    public void setPa_strings(ArrayList<String> pa_strings) {
        this.pa_strings = pa_strings;
    }

    public ArrayList<String> getNg_strings() {
        return ng_strings;
    }

    public void setNg_strings(ArrayList<String> ng_strings) {
        this.ng_strings = ng_strings;
    }

    public ArrayList<String> getPa_links() {
        return pa_links;
    }

    public void setPa_links(ArrayList<String> pa_links) {
        this.pa_links = pa_links;
    }

    public ArrayList<String> getNg_links() {
        return ng_links;
    }

    public void setNg_links(ArrayList<String> ng_links) {
        this.ng_links = ng_links;
    }

    public int getPa_link_amount() {
        return pa_link_amount;
    }

    public void setPa_link_amount(int pa_link_amount) {
        this.pa_link_amount = pa_link_amount;
    }

    public int getNg_link_amount() {
        return ng_link_amount;
    }

    public void setNg_link_amount(int ng_link_amount) {
        this.ng_link_amount = ng_link_amount;
    }

    public String getSuggestedWorkoutLink() {
        return suggestedWorkoutLink;
    }

    public void setSuggestedWorkoutLink(String suggestedWorkoutLink) {
        this.suggestedWorkoutLink = suggestedWorkoutLink;
    }

    public String getWeekDates() {
        return weekDates;
    }

    public void setWeekDates(String weekDates) {
        this.weekDates = weekDates;
    }

    public String getSuggestedWorkoutType() {
        return suggestedWorkoutType;
    }

    public void setSuggestedWorkoutType(String suggestedWorkoutType) {
        this.suggestedWorkoutType = suggestedWorkoutType;
    }
}
