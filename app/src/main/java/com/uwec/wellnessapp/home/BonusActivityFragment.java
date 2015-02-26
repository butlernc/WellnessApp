package com.uwec.wellnessapp.home;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.BonusData;
import com.uwec.wellnessapp.data.LoggingHelper;
import com.uwec.wellnessapp.statics.Statics;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Noah Butler on 2/16/2015.
 *
 *
 */
public class BonusActivityFragment extends Fragment {

    String bonusActivityLinkString = "enter link here";
    TextView bonusPointCurrentAmountEven;
    TextView bonusPointCurrentAmountOdd;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bonus_activity_fragment_default, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.bonus_activity_list);
        BonusActivityViewItem[] placeHolder = new BonusActivityViewItem[5];
        BonusActivityListAdapter listAdapter = new BonusActivityListAdapter(getActivity().getBaseContext(), getActivity(), placeHolder);

        listView.setAdapter(listAdapter);

        return rootView;
    }

    public class BonusActivityViewItem {

        public BonusActivityViewItem() {

        }

    }

    public class BonusActivityListAdapter extends ArrayAdapter<BonusActivityViewItem> {

        private Context context;
        private Activity activity;

        public BonusActivityListAdapter(Context context, Activity activity, BonusActivityViewItem[] bonusActivityData) {
            super(context, R.layout.bonus_activity_list_item, bonusActivityData);
            this.context = context;
            this.activity = activity;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View rowView = inflater.inflate(R.layout.bonus_activity_list_item, parent, false);

            final BonusData bonusData = Statics.globalBonusData;

            /* title of the bonus activity */
            TextView bonusActivityTitleEven = (TextView)rowView.findViewById(R.id.bonus_activity_title_even);
            TextView bonusActivityTitleOdd  = (TextView)rowView.findViewById(R.id.bonus_activity_title_odd);

            bonusActivityTitleEven.setText(bonusData.getTitles().get((position*2)));
            bonusActivityTitleOdd.setText(bonusData.getTitles().get((position*2)+1));

            /* button for more info on the corresponding bonus activity */
            Button moreInfoEven = (Button)rowView.findViewById(R.id.more_info_button_even);
            Button moreInfoOdd = (Button)rowView.findViewById(R.id.more_info_button_odd);

            moreInfoEven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http://www.uwec.edu/Recreation/activities/wellness/StepItUp/bonusactivities.htm"));
                    //intent.setData(Uri.parse(bonusData.getLinks().get((position*2))));
                    activity.startActivity(intent);
                }
            });

            moreInfoOdd.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_BROWSABLE);
                    intent.setData(Uri.parse("http://www.uwec.edu/Recreation/activities/wellness/StepItUp/bonusactivities.htm"));
                    //intent.setData(Uri.parse(bonusData.getLinks().get((position*2)+1)));
                    activity.startActivity(intent);
                }
            });

            /* number that shows how many points the user has in that bonus activity */
            bonusPointCurrentAmountEven = (TextView)rowView.findViewById(R.id.bonus_point_current_amount_even);
            bonusPointCurrentAmountOdd = (TextView)rowView.findViewById(R.id.bonus_point_current_amount_odd);

            /* check if current row's left (even) bonus activity is completed or not */
            if(Statics.getUsersCurrentWeekData().getBonusPoints()[position*2] < Statics.globalBonusData.getCompletePerWeek().get((position*2))) {
                bonusPointCurrentAmountEven.setText("Earned: " + Statics.getUsersCurrentWeekData().getBonusPoints()[position * 2] + "/" + Statics.globalBonusData.getCompletePerWeek().get((position * 2)));
            }else {
                bonusPointCurrentAmountEven.setText("Completed!");
            }

            /* check if current row's right (odd) bonus activity is completed or not */
            if(Statics.getUsersCurrentWeekData().getBonusPoints()[position*2] < Statics.globalBonusData.getCompletePerWeek().get((position*2))) {
                bonusPointCurrentAmountOdd.setText("Earned: " + Statics.getUsersCurrentWeekData().getBonusPoints()[(position * 2) + 1] + "/" + Statics.globalBonusData.getCompletePerWeek().get(((position * 2) + 1)));
            }else {
                bonusPointCurrentAmountOdd.setText("Completed!");
            }

            /* button to add a point to the corresponding bonus activity */
            ImageButton addBonusPointButtonEven = (ImageButton)rowView.findViewById(R.id.overlay_button_add_point_even);
            ImageButton addBonusPointButtonOdd = (ImageButton)rowView.findViewById(R.id.overlay_button_add_point_odd);

            addBonusPointButtonEven.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = position*2;
                    check(index);
                    Log.d("Clicked", "Clicked Position: " + position + " Clicked index: " + index);

                    updateText(rowView, position, index);

                }
            });

            addBonusPointButtonOdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int index = (position*2) + 1;
                    check(index);
                    Log.d("Clicked", "Clicked Position: " + position + " Clicked index: " + index);
                    updateText(rowView, position, index);

                }
            });

            return rowView;
        }


        public void check(int index) {
            int currentStat = Statics.getUsersCurrentWeekData().getBonusPoints()[index];
            boolean canOnlyOnce = false;
            /* check if this bonus activity can only be checked off once */
            for(int i = 0; i < Statics.globalBonusData.getCanCompleteOnce().size(); i++) {
                if(Statics.globalBonusData.getCanCompleteOnce().get(i) == index) { // we have one that can only be checked off once
                    if(!Statics.globalUserData.getOneTimeBonusPoints().get(index)) { //hasn't been checked off yet
                        Statics.globalUserData.getOneTimeBonusPoints().put(index, true);
                        Statics.getUsersCurrentWeekData().getBonusPoints()[index] = Statics.globalBonusData.getPerCompletion().get(index);
                    }
                    canOnlyOnce = true;
                    break;
                }
            }

            if(!canOnlyOnce && currentStat < Statics.globalBonusData.getCompletePerWeek().get(index)) {
                LoggingHelper loggingHelper = new LoggingHelper(getActivity().getBaseContext(), getActivity(), 2);
                loggingHelper.logPoints();
                Statics.getUsersCurrentWeekData().getBonusPoints()[index] = (currentStat + Statics.globalBonusData.getPerCompletion().get(index));
            }
        }

        /**
         * Used to check if the current bonus activity should be displayed as completed or
         * the score it currently has.
         * @param index
         */
        public void updateText(View rowView, int position, int index) {

            boolean canOnlyOnce = false;
            /* update text for bonus activities that can only be completed once */
            for(int i = 0; i < Statics.globalBonusData.getCanCompleteOnce().size(); i++) {
                if(Statics.globalBonusData.getCanCompleteOnce().get(i) == index) { // we have one that can only be checked off once
                    if (Statics.globalUserData.getOneTimeBonusPoints().get(index)) { //has been check off, show as completed

                    }
                }
            }

            /* update text for even*/
            if(!canOnlyOnce && Statics.getUsersCurrentWeekData().getBonusPoints()[position*2] < Statics.globalBonusData.getCompletePerWeek().get((position*2))) {

                bonusPointCurrentAmountEven.setText("Earned: " + Statics.getUsersCurrentWeekData().getBonusPoints()[position * 2] + "/" + Statics.globalBonusData.getCompletePerWeek().get((position * 2)));
            }else {
                bonusPointCurrentAmountEven.setText("Completed!");
            }

            /* update text for odd */
            if(!canOnlyOnce && Statics.getUsersCurrentWeekData().getBonusPoints()[(position*2)+1] < Statics.globalBonusData.getCompletePerWeek().get((position*2)+1)) {

                bonusPointCurrentAmountOdd.setText("Earned: " + Statics.getUsersCurrentWeekData().getBonusPoints()[(position * 2) + 1] + "/" + Statics.globalBonusData.getCompletePerWeek().get(((position * 2) + 1)));
            }else {
                bonusPointCurrentAmountOdd.setText("Completed!");
            }

            bonusPointCurrentAmountEven.invalidate();
            bonusPointCurrentAmountOdd.invalidate();

        }

    }

    private void updateScoreEven(String score) {

    }

}
