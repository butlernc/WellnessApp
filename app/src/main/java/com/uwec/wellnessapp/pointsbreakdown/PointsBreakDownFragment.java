package com.uwec.wellnessapp.pointsbreakdown;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.Series;
import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.LoggingHelper;
import com.uwec.wellnessapp.data.WeeklyUserData;
import com.uwec.wellnessapp.statics.Statics;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Noah Butler on 1/20/2015.
 */
public class PointsBreakDownFragment extends Fragment {

    View rootView;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.fitness_nutrtion_breakdown_fragment, container, false);
        getActivity().getActionBar().setTitle("Points Breakdown");

        /* calculate the points so we can display them */
        LoggingHelper loggingHelper = new LoggingHelper(getActivity().getBaseContext(), getActivity(), 0);

        /* grab the text view and set the points */
        TextView fitnessPointsTally = (TextView) rootView.findViewById(R.id.fitness_points_tally);
        fitnessPointsTally.setText(String.valueOf(loggingHelper.tallyAllFitness()));

        TextView nutritionPointsTally = (TextView) rootView.findViewById(R.id.nutrition_points_tally);
        nutritionPointsTally.setText(String.valueOf(loggingHelper.tallyAllNutrition()));

        TextView bonusPointsTally = (TextView) rootView.findViewById(R.id.bonus_points_tally);
        bonusPointsTally.setText(String.valueOf(loggingHelper.tallyAllBonusPoints()));

        /* grab the fitness graph view and set the title*/
        LineChart fitness_graph = (LineChart)rootView.findViewById(R.id.fitness_graph);
        fitness_graph.setDrawYValues(true);

        /* grab the nutrition grah view and set the title */
        LineChart nutrition_graph = (LineChart)rootView.findViewById(R.id.nutrition_graph);
        nutrition_graph.setDrawYValues(true);

        /* create the data point arrays for our graphs */
        ArrayList<Entry> dataFitnessPoints = new ArrayList<>();
        ArrayList<Entry> dataNutritionPoints = new ArrayList<>();
        /* used to label the x axis */
        ArrayList<String> xLabels = new ArrayList<>();
        /* gather data */
        if(Statics.sessionData.getWeekNumber() > 3) {
            for (int i = 0; i < Statics.sessionData.getWeekNumber(); i++) {
                int weekFitnessSnapShot = 0;
                WeeklyUserData currentWeekData = Statics.globalUserData.getWeeklyData().get(i);

                for (int j = 0; j < 5; j++) {
                    if (currentWeekData.getPhysicalGoalCheckOffs().get(j)) {
                        weekFitnessSnapShot += 2;
                    }
                }
                Log.e("WEEKLY", "Fitness: " + weekFitnessSnapShot);

                int weekNutritionSnapShot = 0;
                for (int j = 0; j < Statics.globalWeekDataList.get(i).getNg_days_per_week(); j++) {
                    if (Statics.globalUserData.getWeeklyData().get(i).getNutritionGoalCheckOffs().get(j)) {
                        weekNutritionSnapShot++;
                    }
                }
                Log.e("WEEKLY", "Fitness: " + weekNutritionSnapShot);

                xLabels.add("Week " + (i + 1) + ".");
                dataFitnessPoints.add(new Entry(weekFitnessSnapShot, (i + 1)));
                dataNutritionPoints.add(new Entry(weekNutritionSnapShot, (i + 1)));
            }

            Log.e("SIZE", "xlabels: " + xLabels.size() + " fitness points:" + dataFitnessPoints.size() + "nutrition data: " + dataNutritionPoints.size());

            /* Add our data to wrapper classes and then add it to the graph view */
            LineDataSet fitnessDataSet = new LineDataSet(dataFitnessPoints, "Fitness Points");
            fitnessDataSet.setColor(getResources().getColor(R.color.wallet_highlighted_text_holo_light));

            ArrayList<LineDataSet> fitnessDataSetWrapper = new ArrayList<>();
            fitnessDataSetWrapper.add(fitnessDataSet);

            LineData fitnessDataParent = new LineData(xLabels, fitnessDataSetWrapper);
            fitness_graph.setData(fitnessDataParent);

            /* same for the nutrition graph */
            LineDataSet nutritionDataSet = new LineDataSet(dataFitnessPoints, "Nutrition Points");
            nutritionDataSet.setColor(getResources().getColor(R.color.wallet_highlighted_text_holo_light));

            ArrayList<LineDataSet> nutritionDataSetWrapper = new ArrayList<>();
            nutritionDataSetWrapper.add(nutritionDataSet);

            LineData nutritionDataParent = new LineData(xLabels, nutritionDataSetWrapper);
            nutrition_graph.setData(nutritionDataParent);
        }

        return rootView;
    }
}
