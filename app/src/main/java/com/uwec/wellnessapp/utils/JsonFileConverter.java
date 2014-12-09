package com.uwec.wellnessapp.utils;

import android.util.Log;

import com.uwec.wellnessapp.data.GoalData;
import com.uwec.wellnessapp.data.UserData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Noah Butler on 12/5/2014.
 *
 * Class used to create json object from the Userdata object
 * and vice versa.
 */
public class JsonFileConverter {

    private static String json_value_names[] = {"email", "first_name", "last_name", "password", "total_score"};

    public UserData convertUserDataJSON(JSONObject jsonObject) throws JSONException {
        UserData userData = new UserData();
        userData.setEmail(jsonObject.getString(json_value_names[0]));
        userData.setFirst_name(jsonObject.getString(json_value_names[1]));
        userData.setLast_name(jsonObject.getString(json_value_names[2]));
        userData.setPassword(jsonObject.getString(json_value_names[3]));
        userData.setTotal_score(jsonObject.getInt(json_value_names[4]));
        return userData;
    }

    public GoalData convertGoalDataJSON(JSONObject jsonObject) throws JSONException {
        GoalData goalData = new GoalData();
        /* TODO: create goal data object from json */

        return goalData;
    }

    public JSONObject convertToJSON(UserData userData) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(json_value_names[0], userData.getEmail());
        jsonObject.put(json_value_names[1], userData.getFirst_name());
        jsonObject.put(json_value_names[2], userData.getLast_name());
        jsonObject.put(json_value_names[3], userData.getPassword());
        jsonObject.put(json_value_names[4], userData.getTotal_score());

        Log.e("JSON", "" + jsonObject.toString());

        return jsonObject;
    }
}
