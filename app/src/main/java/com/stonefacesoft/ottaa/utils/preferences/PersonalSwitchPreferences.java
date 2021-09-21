package com.stonefacesoft.ottaa.utils.preferences;

import android.content.Context;
import android.os.Build;
import android.preference.SwitchPreference;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;


import com.stonefacesoft.ottaa.R;

public class PersonalSwitchPreferences extends SwitchPreference {

    public PersonalSwitchPreferences(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public PersonalSwitchPreferences(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public PersonalSwitchPreferences(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PersonalSwitchPreferences(Context context) {
        super(context);
    }


    @Override
    protected void onBindView(View view) {
        super.onBindView(view);
        Switch theSwitch = findSwitchInChildviews((ViewGroup) view);
        if (theSwitch != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                theSwitch.setThumbTintList(Utils.colorToStateList(getContext().getResources().getColor(R.color.NaranjaOTTAA),
                        getContext().getResources().getColor(R.color.Gray)));
                theSwitch.setTrackTintList(Utils.colorToStateList(getContext().getResources().getColor(R.color.NaranjaOTTAA),
                        getContext().getResources().getColor(R.color.DarkGray)));

            }
        }
    }

    private Switch findSwitchInChildviews(ViewGroup view) {
        for (int i = 0; i < view.getChildCount(); i++) {
            View thisChildview = view.getChildAt(i);
            if (thisChildview instanceof Switch) {
                return (Switch) thisChildview;
            } else if (thisChildview instanceof ViewGroup) {
                Switch theSwitch = findSwitchInChildviews((ViewGroup) thisChildview);
                if (theSwitch != null) return theSwitch;
            }
        }
        return null;
    }
}
