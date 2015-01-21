package com.uwec.wellnessapp.home;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.uwec.wellnessapp.data.LoggingHelper;
import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by butlernc on 1/20/2015.
 */
public class FitnessTrackerButtonListener implements View.OnClickListener {

    private Context context;
    private int index;
    private boolean isFitness;
    private Button currentButton;

    public FitnessTrackerButtonListener(Context context, int index, boolean isFitness, Button currentButton) {
        this.context       = context;
        this.index         = index;
        this.isFitness     = isFitness;
        this.currentButton = currentButton;
    }

    @Override
    public void onClick(View view) {

        if(isFitness) {// we're in the physical goals fragment, hitting buttons
            if(!Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().get(index)) {
                Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().add(index, true);
                currentButton.setText("Completed!");
            }

            LoggingHelper loggingHelper = new LoggingHelper(context, isFitness);
            loggingHelper.start();
        } else {
            if(!Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().get(index)) {
                Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().add(index, true);
                currentButton.setText("Completed!");
            }

            LoggingHelper loggingHelper = new LoggingHelper(context, isFitness);
            loggingHelper.start();
        }
    }
}
