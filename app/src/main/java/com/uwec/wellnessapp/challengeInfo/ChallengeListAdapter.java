package com.uwec.wellnessapp.challengeInfo;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.uwec.wellnessapp.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Noah Butler on 1/14/2015.
 * Used as the list adapter for the ChallengeInfoFragment
 */
public class ChallengeListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private Activity activity;
    private List<String> listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> listDataChild;

    public ChallengeListAdapter(Context context, Activity activity, List<String> listDataHeader,
                                 HashMap<String, List<String>> listChildData) {
        this.context = context;
        this.activity = activity;
        this.listDataHeader = listDataHeader;
        this.listDataChild = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    /**
     * Returns a single child item of the list. Adds a listener to it so that when clicked on
     * the proper goal information is displayed on the next fragment.
     * @param groupPosition
     * @param childPosition
     * @param isLastChild
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {

        final String childText = (String) getChild(groupPosition, childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.challenge_info_child_list, null);
        }

        Button button = (Button) convertView.findViewById(R.id.challenge_info_child);
        button.setText(childText);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChallengeInfoSubFragment challengeInfoFragment = new ChallengeInfoSubFragment();
                Bundle args = new Bundle();

                if(childPosition == 0) {
                    args.putBoolean("info_type", true);
                } else {
                    args.putBoolean("info_type", false);
                }

                args.putInt("id", (groupPosition));

                challengeInfoFragment.setArguments(args);
                activity.getFragmentManager().beginTransaction().replace(R.id.main_nav_fragment, challengeInfoFragment).commit();
                Log.d("LIST", "should be a new fragment showing");
            }
        });

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.listDataChild.get(this.listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.listDataHeader.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    /**
     * Displays the list of weeks in the challengeInfo fragment.
     * @param groupPosition
     * @param isExpanded
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.challenge_info_header_layout, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.challenge_info_header_text);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

}
