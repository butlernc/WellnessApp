package com.uwec.wellnessapp.utils;

import android.util.Log;

import com.uwec.wellnessapp.data.UserData;
import com.uwec.wellnessapp.data.WeeklyUserData;
import com.uwec.wellnessapp.statics.Statics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Noah Butler on 2/23/2015.
 */
public class UserJsonFileConverter {

    private static String weeklyData_json_value_names[] = {
            "pa_points",//0
            "ng_points",//1
            "pa_amount",//2
            "ng_amount",//3
            "pa_checkOff",//4
            "pa_checkOffArray",//5
            "ng_checkOff",//6
            "ng_checkOffArray",//7
            "total_points",//8
            "weekly_points",//9
            "bonus_points"//10
    };
    private static String json_value_names[]            = {"email", "first_name", "last_name", "password", "total_score", "weekly_data", "once"};

    UserData userData;

    public UserJsonFileConverter() {
        userData = new UserData();
            /* create the weekly data objects for this in userData. */
        ArrayList<WeeklyUserData> weeklyUserData = new ArrayList<>();
        for(int i = 0; i < Statics.NUMBER_OF_WEEKS; i++) {
            weeklyUserData.add(new WeeklyUserData(i));
        }

        userData.setWeeklyData(weeklyUserData);
    }

    /**
     * These next methods are used to convert the user's data object into the different
     * JSON objects. Each one get written out to its own file.
     *
     * @param userData
     * @return
     * @throws org.json.JSONException
     */

    public JSONObject convertUserPAPointsToJSON(UserData userData) throws JSONException {
        JSONObject allPaPointsJSON = new JSONObject();
        for(int i = 0; i < Statics.NUMBER_OF_WEEKS; i++) {
            WeeklyUserData single_weeklyData = userData.getWeeklyData().get(i);
            JSONObject pa_checkOffs_jsonObject = new JSONObject();

            for (int j = 0; j < single_weeklyData.getPhysicalGoalCheckOffs().size(); j++) {
                pa_checkOffs_jsonObject.put(weeklyData_json_value_names[4] + j, single_weeklyData.getPhysicalGoalCheckOffs().get(j));
            }

            allPaPointsJSON.put((weeklyData_json_value_names[4] + i), pa_checkOffs_jsonObject);
            allPaPointsJSON.put(weeklyData_json_value_names[0] + i, single_weeklyData.getPhysicalGoalPoints());
            allPaPointsJSON.put(weeklyData_json_value_names[2] + i, single_weeklyData.getPhysicalGoalCheckOffAmount());

                /* stored here, didn't feel like having another file just for these two values per week */
            allPaPointsJSON.put(weeklyData_json_value_names[8] + i, single_weeklyData.getSnapShotTotalScore());
            Log.d("SNAP", "WEEK " + i + ": " + single_weeklyData.getSnapShotWeekScore());
            allPaPointsJSON.put(weeklyData_json_value_names[9] + i, single_weeklyData.getSnapShotWeekScore());
            Log.d("SNAP", "WEEK " + i + ": " + single_weeklyData.getSnapShotTotalScore());
        }

        return allPaPointsJSON;
    }

    public JSONObject convertUserNGPointsToJSON(UserData userData) throws JSONException{
        JSONObject allNgPointsJSON = new JSONObject();
        for(int i = 0; i < Statics.NUMBER_OF_WEEKS; i++) {
            WeeklyUserData single_weeklyData = userData.getWeeklyData().get(i);
            JSONObject ng_checkOffs_jsonObject = new JSONObject();

            for (int j = 0; j < single_weeklyData.getNutritionGoalCheckOffs().size(); j++) {
                ng_checkOffs_jsonObject.put(weeklyData_json_value_names[6] + j, single_weeklyData.getNutritionGoalCheckOffs().get(j));
            }

            allNgPointsJSON.put((weeklyData_json_value_names[6] + i), ng_checkOffs_jsonObject);
            allNgPointsJSON.put(weeklyData_json_value_names[1] + i, single_weeklyData.getNutritionGoalPoints());
            allNgPointsJSON.put(weeklyData_json_value_names[3] + i, single_weeklyData.getNutritionGoalCheckOffAmount());
        }

        return allNgPointsJSON;
    }

    public JSONObject convertUserBonusPointsToJSON(UserData userData) throws JSONException {
        JSONObject allBonusPointsJSON = new JSONObject();
        for(int i = 0; i < Statics.NUMBER_OF_WEEKS; i++) {
            WeeklyUserData single_weeklyData = userData.getWeeklyData().get(i);
            JSONObject bonusPoints_jsonObject = new JSONObject();

            for (int j = 0; j < single_weeklyData.getBonusPoints().length; j++) {
                bonusPoints_jsonObject.put(weeklyData_json_value_names[10] + j, single_weeklyData.getBonusPoints()[j]);
            }

            allBonusPointsJSON.put((weeklyData_json_value_names[10] + i), bonusPoints_jsonObject);
        }

        return allBonusPointsJSON;
    }

    public JSONObject convertUserInfoToJSON(UserData userData) throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(json_value_names[0], userData.getEmail());
        jsonObject.put(json_value_names[1], userData.getFirst_name());
        jsonObject.put(json_value_names[2], userData.getLast_name());
        jsonObject.put(json_value_names[3], userData.getPassword());
        jsonObject.put(json_value_names[4], userData.getTotal_score());
        jsonObject.put(json_value_names[6], oneTimeBonusPointsToJSON(userData.getOneTimeBonusPoints()));

        return jsonObject;
    }

    /**
     * Next methods are used for converting the user's data JSON objects into the weeklyData objects
     *
     * @param allPaPointsJSON
     */

    public void convertJSONToUserPAPoints(JSONObject allPaPointsJSON) throws JSONException{
        for(int i = 0; i < Statics.NUMBER_OF_WEEKS; i++) {
            WeeklyUserData weeklyUserData = userData.getWeeklyData().get(i);
            JSONObject pa_checkOffs_jsonObject = allPaPointsJSON.getJSONObject((weeklyData_json_value_names[4] + i));

            for(int j = 0; j < allPaPointsJSON.getInt(weeklyData_json_value_names[2] + i); j++) {
                weeklyUserData.getPhysicalGoalCheckOffs().set(j, pa_checkOffs_jsonObject.getBoolean(weeklyData_json_value_names[4] + j));
            }

            weeklyUserData.setPhysicalGoalPoints(allPaPointsJSON.getInt(weeklyData_json_value_names[0] + i));
            weeklyUserData.setPhysicalGoalCheckOffAmount(allPaPointsJSON.getInt(weeklyData_json_value_names[2] + i));

            weeklyUserData.setSnapShotTotalScore(allPaPointsJSON.getInt(weeklyData_json_value_names[8] + i));
            weeklyUserData.setSnapShotWeekScore(allPaPointsJSON.getInt(weeklyData_json_value_names[9] + i));
        }
    }

    public void convertJSONToUserNGPoints(JSONObject allNgPointsJSON) throws JSONException{
        for(int i = 0; i < Statics.NUMBER_OF_WEEKS; i++) {
            WeeklyUserData weeklyUserData = userData.getWeeklyData().get(i);
            JSONObject ng_checkOffs_jsonObject = allNgPointsJSON.getJSONObject((weeklyData_json_value_names[6] + i));
            for(int j = 0; j < allNgPointsJSON.getInt(weeklyData_json_value_names[3] + i); j++) {
                weeklyUserData.getNutritionGoalCheckOffs().set(j, ng_checkOffs_jsonObject.getBoolean(weeklyData_json_value_names[6] + j));
            }

            weeklyUserData.setNutritionGoalPoints(allNgPointsJSON.getInt(weeklyData_json_value_names[1] + i));
            weeklyUserData.setNutritionGoalCheckOffAmount(allNgPointsJSON.getInt(weeklyData_json_value_names[3] + i));
        }
    }

    public void convertJSONToUserBPoints(JSONObject allBonusPointsJSON) throws JSONException{
        for(int i = 0; i < Statics.NUMBER_OF_WEEKS; i++) {
            WeeklyUserData weeklyUserData = userData.getWeeklyData().get(i);
            JSONObject bp_checkOffs_jsonObject = allBonusPointsJSON.getJSONObject((weeklyData_json_value_names[10] + i));
            for(int j = 0; j < 12; j++) {
                weeklyUserData.getBonusPoints()[j] = bp_checkOffs_jsonObject.getInt(weeklyData_json_value_names[10] + j);
            }
        }
    }

    /**
     * converts the user's info (header file) from json to the userData object
     * @param userInfoJSON
     * @throws JSONException
     */

    public void convertJSONToUserInfo(JSONObject userInfoJSON) throws JSONException{
        userData.setEmail(userInfoJSON.getString(json_value_names[0]));
        userData.setFirst_name(userInfoJSON.getString(json_value_names[1]));
        userData.setLast_name(userInfoJSON.getString(json_value_names[2]));
        userData.setPassword(userInfoJSON.getString(json_value_names[3]));
        userData.setTotal_score(userInfoJSON.getInt(json_value_names[4]));
        userData.setOneTimeBonusPoints(jsonToOneTimeBonusPoints(userInfoJSON.getJSONObject(json_value_names[6])));
    }

    /**
     * Helper methods for the user's info (header file) converting methods. The oneTimeBonusPoints
     * object is used to keep track of the user's completion of certain bonus activities that can
     * only be completed once.
     * @param jsonObject
     * @return
     * @throws JSONException
     */
    private HashMap<Integer, Boolean> jsonToOneTimeBonusPoints(JSONObject jsonObject) throws JSONException {
        HashMap<Integer, Boolean> oneTimeBonusPoints = new HashMap<>();

        for(int i = 6; i < 12; i++) {
            oneTimeBonusPoints.put(i, jsonObject.getBoolean(json_value_names[6] + i));
        }

        return oneTimeBonusPoints;
    }

    private JSONObject oneTimeBonusPointsToJSON(HashMap<Integer, Boolean> oneTimeBonusPoints) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        for(int i = 6; i < 12; i++) {
            jsonObject.put(json_value_names[6] + i, oneTimeBonusPoints.get(i));
        }

        return jsonObject;
    }

}
