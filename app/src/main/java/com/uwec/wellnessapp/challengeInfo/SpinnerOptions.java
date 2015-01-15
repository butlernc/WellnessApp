package com.uwec.wellnessapp.challengeInfo;


/**
 * Created by Noah Butler on 1/14/2015.
 */
public class SpinnerOptions{

    public String name;
    public int ID;

    public SpinnerOptions(String name, int ID) {
        this.name = name;
        this.ID   = ID;
    }

    public String toString() {
        return name;
    }


}
