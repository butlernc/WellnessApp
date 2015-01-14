package com.uwec.wellnessapp.challengeInfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.uwec.wellnessapp.R;

/**
 * Created by butlernc on 1/14/2015.
 */
public class ChallengeListSpinnerAdapter extends ArrayAdapter<String> {

    Context context;
    String[] strings;

    public ChallengeListSpinnerAdapter(Context context, int textViewResourceId, String[] strings) {
        super(context, textViewResourceId, strings);
        this.context = context;
        this.strings = strings;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        notifyDataSetChanged();
        return getCustomView(position, convertView, parent);
    }

    public void setDefaultText(String defaultText) {
        //this.firstElement = objects[0];
        //objects[0] = defaultText;
    }

    public View getCustomView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.challenge_info_spinner_fragment, parent, false);
        TextView physical_label = (TextView) row.findViewById(R.id.challenge_info_spinner_row_physical);
        TextView nutrition_label = (TextView) row.findViewById(R.id.challenge_info_spinner_row_nutrition);
        physical_label.setText(strings[0]);
        nutrition_label.setText(strings[1]);

        return row;
    }

}

