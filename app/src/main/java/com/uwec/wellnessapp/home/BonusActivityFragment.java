package com.uwec.wellnessapp.home;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.uwec.wellnessapp.R;
import com.uwec.wellnessapp.data.BonusData;
import com.uwec.wellnessapp.data.LoggingHelper;
import com.uwec.wellnessapp.statics.Statics;

/**
 * Created by Noah Butler on 2/16/2015.
 *
 *
 */
public class BonusActivityFragment extends Fragment {

    /** link for more info on the bonus activities */
    private static String bonusActivityLinkString = "http://www.uwec.edu/Recreation/fitness/Stepitup/bonusactivities.htm";

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.bonus_activity_fragment_default, container, false);

        ListView listView = (ListView)rootView.findViewById(R.id.bonus_activity_list);
        BonusActivityViewItem[] placeHolder = new BonusActivityViewItem[6];
        BonusActivityListAdapter listAdapter = new BonusActivityListAdapter(getActivity().getBaseContext(), placeHolder);

        listView.setAdapter(listAdapter);

        return rootView;
    }

    public class BonusActivityViewItem {

        public BonusActivityViewItem() {

        }

    }

    /**
     * custom list adapter for the list in the bonus point fragment
     */
    public class BonusActivityListAdapter extends ArrayAdapter<BonusActivityViewItem> {

        private Context context;

        /** these hold the titles for the current bonus activity */
        private TextView bonusActivityTitleEven, bonusActivityTitleOdd;
        /** these are the buttons that link the user to the webpage for the bonus activities */
        private Button moreInfoEven, moreInfoOdd;

        /** display the current points of the bonus acitivity */
        private TextView bonusPointCurrentAmountEven, bonusPointCurrentAmountOdd;

        /** button to check off if the did the bonus activity */
        private ImageButton addBonusPointButtonEven, addBonusPointButtonOdd;

        public BonusActivityListAdapter(Context context, BonusActivityViewItem[] bonusActivityData) {
            super(context, R.layout.bonus_activity_list_item, bonusActivityData);
            this.context = context;
        }

        private void initiateRow(View rowView, int indexEven, int indexOdd) {

            /* create reference to the static bonus data */
            BonusData bonusData = Statics.globalBonusData;

            /* setup titles */
            bonusActivityTitleEven = (TextView) rowView.findViewById(R.id.bonus_activity_title_even);
            bonusActivityTitleEven.setText(bonusData.getTitles().get(indexEven));

            /* button for more info on the corresponding bonus activity */
            moreInfoEven = (Button) rowView.findViewById(R.id.more_info_button_even);
            moreInfoEven.setOnClickListener(new MoreInfoButtonListener());

            /* number that shows how many points the user has in that bonus activity */
            bonusPointCurrentAmountEven = (TextView) rowView.findViewById(R.id.bonus_point_current_amount_even);

            /* button to add a point to the corresponding bonus activity */
            addBonusPointButtonEven = (ImageButton) rowView.findViewById(R.id.overlay_button_add_point_even);
            addBonusPointButtonEven.setOnClickListener(new AddButtonListener(indexEven, bonusPointCurrentAmountEven));


            if (indexOdd > 0) { //make sure we are not on the last one
                bonusActivityTitleOdd = (TextView) rowView.findViewById(R.id.bonus_activity_title_odd);
                bonusActivityTitleOdd.setText(bonusData.getTitles().get(indexOdd));

                moreInfoOdd = (Button) rowView.findViewById(R.id.more_info_button_odd);
                moreInfoOdd.setOnClickListener(new MoreInfoButtonListener());

                bonusPointCurrentAmountOdd = (TextView) rowView.findViewById(R.id.bonus_point_current_amount_odd);

                addBonusPointButtonOdd = (ImageButton) rowView.findViewById(R.id.overlay_button_add_point_odd);
                addBonusPointButtonOdd.setOnClickListener(new AddButtonListener(indexOdd, bonusPointCurrentAmountOdd));
            }
        }

        private void initiateText(int indexEven, int indexOdd) {
            /* check if current row's left (even) bonus activity is completed or not */
            if (Statics.getUsersCurrentWeekData().getBonusPoints()[indexEven] < Statics.globalBonusData.getCompletePerWeek().get((indexEven))) {
                bonusPointCurrentAmountEven.setText("Earned: " + Statics.getUsersCurrentWeekData().getBonusPoints()[indexEven] + "/" + Statics.globalBonusData.getCompletePerWeek().get((indexEven)));
            } else {
                bonusPointCurrentAmountEven.setText("Completed!");
            }

            /* check if current row's right (odd) bonus activity is completed or not */
            if (Statics.getUsersCurrentWeekData().getBonusPoints()[indexOdd] < Statics.globalBonusData.getCompletePerWeek().get((indexOdd))) {
                bonusPointCurrentAmountOdd.setText("Earned: " + Statics.getUsersCurrentWeekData().getBonusPoints()[indexOdd] + "/" + Statics.globalBonusData.getCompletePerWeek().get(indexOdd));
            } else {
                bonusPointCurrentAmountOdd.setText("Completed!");
            }
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView;
            if (convertView != null) { //view can be recycled
                rowView = convertView;
            } else { // convertView is null, let's just create a new view to return. initiate
                rowView = inflater.inflate(R.layout.bonus_activity_list_item, parent, false);
            }

            /* there are two view sets, split by index */
            int indexEven = position * 2;
            int indexOdd = (position * 2) + 1;

            /* call our set up methods */
            if(position < 5) {
                initiateRow(rowView, indexEven, indexOdd);
                initiateText(indexEven, indexOdd);
            }else {
                initiateRow(rowView, indexEven, 0);
                initiateText(indexEven, 0);
            }


            return rowView;
        }

        /**
         * Custom listener class for the more info button
         */
        public class MoreInfoButtonListener implements View.OnClickListener {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setData(Uri.parse(bonusActivityLinkString));
                BonusActivityFragment.this.getActivity().startActivity(intent);
            }
        }

        /**
         * Custom listener class for the check off buttons
         */
        public class AddButtonListener implements View.OnClickListener {

            private int index;
            private TextView pointsView;

            public AddButtonListener(int index, TextView pointsView) {
                this.index = index;
                this.pointsView = pointsView;
            }

            @Override
            public void onClick(View v) {
                /* check if we can update */
                if (canUpdate()) {
                    /* update points */
                    updatePoints();
                    /* update text */
                    updateText();
                }

                LoggingHelper loggingHelper = new LoggingHelper(getActivity().getBaseContext(), getActivity(), 2);
                loggingHelper.logPoints();
            }

            private boolean canUpdate() {
                boolean r;
                if (index >= 5) { //can only be done once per program
                    r = !Statics.globalUserData.getOneTimeBonusPoints().get(index);
                } else { // can be done once per week
                    r = (Statics.getUsersCurrentWeekData().getBonusPoints()[index] < Statics.globalBonusData.getPerCompletion().get(index));
                }
                /* finished, return results */
                return r;
            }

            private void updatePoints() {
                if (index >= 5) {
                    Statics.globalUserData.getOneTimeBonusPoints().put(index, true);
                }
                Statics.getUsersCurrentWeekData().getBonusPoints()[index] += Statics.globalBonusData.getPerCompletion().get(index);
            }

            private void updateText() {
                pointsView.setTextSize(14);
                pointsView.setText("Completed!");
                pointsView.invalidate();
            }
        }
    }
}
