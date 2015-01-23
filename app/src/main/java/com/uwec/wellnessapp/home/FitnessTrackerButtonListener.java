package com.uwec.wellnessapp.home;

import android.content.Context;
import android.view.View;
import android.widget.Button;

import com.uwec.wellnessapp.data.LoggingHelper;
import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by Noah Butler on 1/20/2015.
 *
 * Used as the listener for the buttons that user will use to check off weekly/daily goals
 */
public class FitnessTrackerButtonListener implements View.OnClickListener {

    private Context context;
    private int index;
    private boolean isFitness;
    private Button currentButton;

    /* we create a new FitnessTrackerButtonListener object for each button in the goal's fragment
     * so that correct booleans are switched in the weeklyData object of the user. */
    public FitnessTrackerButtonListener(Context context, int index, boolean isFitness, Button currentButton) {
        this.context       = context;
        this.index         = index;
        this.isFitness     = isFitness;
        this.currentButton = currentButton;
    }

    @Override
    public void onClick(View view) {

        if(isFitness) {// We're in the physical goal's fragment, hitting a completion button.
            if(!Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().get(index)) {
                Statics.getUsersCurrentWeekData().getPhysicalGoalCheckOffs().add(index, true);
                currentButton.setText("Completed!");
            }

            /* save the user data now that they have recorded a completion */
            LoggingHelper loggingHelper = new LoggingHelper(context, isFitness);
            loggingHelper.start();

        } else {// We're in the nutrition goal's fragment, hitting a completion button.
            if(!Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().get(index)) {
                Statics.getUsersCurrentWeekData().getNutritionGoalCheckOffs().add(index, true);
                currentButton.setText("Completed!");
            }

            /* save the user data now that they have recorded a completion */
            LoggingHelper loggingHelper = new LoggingHelper(context, isFitness);
            loggingHelper.start();
        }
    }
}
