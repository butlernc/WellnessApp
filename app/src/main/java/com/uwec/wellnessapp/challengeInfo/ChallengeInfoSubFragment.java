package com.uwec.wellnessapp.challengeInfo;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.WeekData;
import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by Noah Butler on 1/14/2015.
 *
 * Used to display the challengeInfo selected from the challengeInfo expandable list
 * Generic class that is used for both Fitness and Nutrition Goal. Just depends on what the users
 * selects. The needed info from the challengeInfo expandable list is then set to this fragment via
 * an argument bundle.
 */
public class ChallengeInfoSubFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.challenge_info_sub_fragment, container, false);
        /* get the data that tells us what we should display */
        Bundle args = getArguments();

        /* set by what week the user selected in the expandable list */
        int selected_week = args.getInt("id");
        /* set to true if the user selected Physical Activity or false if was Nutrition Goal */
        boolean selected_physical = args.getBoolean("info_type");

        /* get the correct week data from the selected week */
        WeekData currentSelectedWeekData = Statics.globalWeekDataList.get(selected_week);

        /* populate the given text view with info from selected week and goal */
        TextView challenge_info_title = (TextView) rootView.findViewById(R.id.challenge_info_title);
        TextView challenge_info_goal_text = (TextView) rootView.findViewById(R.id.challenge_info_goal_text);
        TextView challenge_info_desc = (TextView) rootView.findViewById(R.id.challenge_info_desc);

        if(selected_physical) {
            challenge_info_title.setText("Week " + currentSelectedWeekData.getWeek() + ": Physical Activity");
            challenge_info_goal_text.setText("Goal: " + currentSelectedWeekData.getPhysical_activity());
            challenge_info_desc.setText(currentSelectedWeekData.getPhysical_activity_description());
        }else{
            challenge_info_title.setText("Week " + currentSelectedWeekData.getWeek() + ": Nutrition Goal" );
            challenge_info_goal_text.setText("Goal: " + currentSelectedWeekData.getNutrition_goal());
            challenge_info_desc.setText(currentSelectedWeekData.getNutrition_goal_description());
        }

        return rootView;
    }
}
