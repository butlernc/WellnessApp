package com.uwec.wellnessapp.data;

import java.util.ArrayList;

/**
 * Created by Noah Butler on 2/18/2015.
 *
 * Used to house all of the Bonus Activities
 * Data
 */
public class BonusData {

    ArrayList<String> titles;
    ArrayList<String> links;
    ArrayList<Integer> totalPerProgram;
    ArrayList<Integer> completePerWeek;
    ArrayList<Integer> perCompletion;
    ArrayList<Integer> canCompleteOnce;

    public BonusData() {}

    public ArrayList<String> getTitles() {
        return titles;
    }

    public void setTitles(ArrayList<String> titles) {
        this.titles = titles;
    }

    public ArrayList<String> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<String> links) {
        this.links = links;
    }

    public ArrayList<Integer> getTotalPerProgram() {
        return totalPerProgram;
    }

    public void setTotalPerProgram(ArrayList<Integer> totalPerProgram) {
        this.totalPerProgram = totalPerProgram;
    }

    public ArrayList<Integer> getCompletePerWeek() {
        return completePerWeek;
    }

    public void setCompletePerWeek(ArrayList<Integer> completePerWeek) {
        this.completePerWeek = completePerWeek;
    }

    public ArrayList<Integer> getPerCompletion() {
        return perCompletion;
    }

    public void setPerCompletion(ArrayList<Integer> perCompletion) {
        this.perCompletion = perCompletion;
    }

    public ArrayList<Integer> getCanCompleteOnce() {
        return canCompleteOnce;
    }

    public void setCanCompleteOnce(ArrayList<Integer> canCompleteOnce) {
        this.canCompleteOnce = canCompleteOnce;
    }
}
