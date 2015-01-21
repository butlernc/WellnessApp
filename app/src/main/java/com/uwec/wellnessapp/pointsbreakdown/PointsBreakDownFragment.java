package com.uwec.wellnessapp.pointsbreakdown;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.LoggingHelper;

import org.w3c.dom.Text;

/**
 * Created by Noah Butler on 1/20/2015.
 */
public class PointsBreakDownFragment extends Fragment {

    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fitness_nutrtion_breakdown_fragment, container, false);
        getActivity().getActionBar().setTitle("Points Breakdown");

        LoggingHelper loggingHelper = new LoggingHelper(getActivity().getBaseContext(), false);

        TextView fitnessPointsTally = (TextView) rootView.findViewById(R.id.fitness_points_tally);
        fitnessPointsTally.setText(String.valueOf(loggingHelper.tallyAllFitness()));

        TextView nutritionPointsTally = (TextView) rootView.findViewById(R.id.nutrition_points_tally);
        nutritionPointsTally.setText(String.valueOf(loggingHelper.tallyAllNutrition()));

        return rootView;
    }
}
