package com.uwec.wellnessapp.challengeInfo;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.HashMap;
import java.util.List;

/**
 * Created by butlernc on 1/14/2015.
 */
public class ChallengeListAdapter extends ArrayAdapter<SpinnerOptions> {

    HashMap<SpinnerOptions, Integer> mIdMap = new HashMap<>();

    public ChallengeListAdapter(Context context, int resource, List<SpinnerOptions> items) {
        super(context, resource, items);
        for (int i = 0; i < items.size(); ++i) {
            //TODO: add to spinner here?
            mIdMap.put(items.get(i), i);
        }
    }

    @Override
    public long getItemId(int position) {
        SpinnerOptions item = getItem(position);
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

}
