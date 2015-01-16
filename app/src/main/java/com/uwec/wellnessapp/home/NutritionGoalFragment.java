package com.uwec.wellnessapp.home;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by butlernc on 1/15/2015.
 */
public class NutritionGoalFragment extends Fragment {

    View rootView;

    TextView weekDisplayNutrition;
    TextView nutritionGoal;
    TextView ngDescTextView;

    Button linksButton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.nutrition_goal_fragment, container, false);
        getActivity().getActionBar().setTitle("Step It Up");

        ngDescTextView = (TextView) rootView.findViewById(R.id.nutrition_goal_desc);
        ngDescTextView.setText(Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber() - 1).getNutrition_goal_description());

        weekDisplayNutrition = (TextView) rootView.findViewById(R.id.week_display_nutrition);
        weekDisplayNutrition.setText("Week " + Statics.sessionData.getWeekNumber() + ": Nutrition Goal");

        nutritionGoal = (TextView) rootView.findViewById(R.id.nutrition_goal);
        nutritionGoal.setText("Goal: " + Statics.globalWeekDataList.get(Statics.sessionData.getWeekNumber() - 1).getNutrition_goal());

        linksButton = (Button) rootView.findViewById(R.id.nutrition_goal_links_button);
        linksButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinksFragment linksFragment = new LinksFragment();
                Bundle args = new Bundle();
                args.putBoolean("from_physical_activity", false);
                linksFragment.setArguments(args);
                NutritionGoalFragment.this.getActivity().getFragmentManager().beginTransaction().replace(R.id.main_nav_fragment, linksFragment).commit();
            }
        });

        return rootView;
    }

}
