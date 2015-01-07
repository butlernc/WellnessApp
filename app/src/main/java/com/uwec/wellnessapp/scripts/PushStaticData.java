package com.uwec.wellnessapp.scripts;

import com.uwec.wellnessapp.utils.JsonFileConverter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Noah Butler on 12/23/2014.
 *
 * push static data such as goal data etc. to our server
 */
public class PushStaticData {

    private static String week_data_json_value_names[] = {"week", "physical_activity", "physical_activity_description", "pa_days_per_week", "nutrition_goal", "nutrition_goal_description", "ng_days_per_week", "supporting_evidence"};

    /** creates a string for a jsonObject to be written out to a file */
    public static String weekStartDays() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("WEEK_ONE", 1);
            jsonObject.put("WEEK_TWO", 8);
            jsonObject.put("WEEK_THREE", 15);
            jsonObject.put("WEEK_FOUR", 22);
            jsonObject.put("WEEK_FIVE", 1);
            jsonObject.put("WEEK_SIX", 8);

            jsonObject.put("MONTH_ONE", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return (jsonObject.toString());
    }

    public static String weeklyData(int week) {
        JSONObject jsonObject = new JSONObject();
        String pa_title = "Do 30 minutes of cardio on at least 3 days this week";
        String ng_title = "Drink 6-8 eight ounce cups of water per day (48-64 ounces total)";
        String pa = "Dr. Myers and the AHA point out that some of the benefits of regular cardiovascular exercise include reduction in body weight, blood pressure, bad cholesterol and increases in good cholesterol and insulin sensitivity.  Every adult should participate in 30 minutes or more of moderate activity on most days of the week.  Moderate activities include brisk walking, recreational activities such as cycling or swimming, house work or yard work.  Getting at least 30 minutes of exercise doesn’t require a gym membership or fancy equipment.  Get outside, grab your friends, and just get moving.  Check out what the University Recreation and Sport Facilities department has to offer for fun activities.  An added bonus in this fitness and nutrition challenge is the opportunity to learn how to find and identify credible health resources. For example, here is a published research article noting the benefits of cardiovascular exercise.  The reader can quickly see that the author is a PhD in cardiology and is associated with the American Heart Association.  Anybody can read this entire article or use the “navigate this article” on the right hand side to quickly take you to key sections like “what are the benefits of exercise” and how much is enough”.  http://circ.ahajournals.org/content/107/1/e2.full";
        String ng = "The CDC provides a wealth of great information on nutrition and water intake.  http://www.cdc.gov/nutrition/everyone/basics/water.html points out that we get some fluids through foods such as broth, soups, vegetables and fruits, but most water needs are met through drinking fluids.  Water helps our bodies regulate temperature, protect and cushion our joints, spinal cord, and other tissues, and also helps us get rid of wastes.  We need to drink water every day, even if we aren’t exercising.  Of course, with vigorous activity it is important to drink even more.  The Mayo Clinic and Institute of Medicine recommend an adequate intake for men is about 3 liters (13 cups) and for women 2.2 liters (about 9 cups) of total beverages http://www.mayoclinic.org/healthy-living/nutrition-and-healthy-eating/in-depth/water/art-20044256";
        try{
            jsonObject.put(week_data_json_value_names[0], week);
            jsonObject.put(week_data_json_value_names[1], pa_title);
            jsonObject.put(week_data_json_value_names[2], pa);
            jsonObject.put(week_data_json_value_names[3], 3);
            jsonObject.put(week_data_json_value_names[4], ng_title);
            jsonObject.put(week_data_json_value_names[5], ng);
            jsonObject.put(week_data_json_value_names[6], 7);
            jsonObject.put(week_data_json_value_names[7], "");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return (jsonObject.toString());
    }

}
