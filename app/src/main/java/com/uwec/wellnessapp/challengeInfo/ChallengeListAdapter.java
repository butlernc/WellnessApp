package com.uwec.wellnessapp.challengeInfo;

import android.content.ClipData.Item;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.uwec.wellnessapp.R;

import java.util.List;

/**
 * Created by butlernc on 1/14/2015.
 */
public class ChallengeListAdapter extends ArrayAdapter<Spinner> {

    public ChallengeListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ChallengeListAdapter(Context context, int resource, List<Spinner> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(getContext());
        }

        Spinner p = getItem(position);
        if(p != null) {
            String[] labels = {"Physical Activity", "Nutrition Goal"};
            p.setAdapter(new ChallengeListSpinnerAdapter(getContext(), R.layout.challenge_info_spinner_row, labels));
        }
        /* TODO: fix this */
        return convertView;
    }

}
