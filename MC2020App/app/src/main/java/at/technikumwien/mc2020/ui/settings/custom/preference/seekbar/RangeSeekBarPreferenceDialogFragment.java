package at.technikumwien.mc2020.ui.settings.custom.preference.seekbar;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import at.technikumwien.mc2020.R;

public class RangeSeekBarPreferenceDialogFragment extends PreferenceDialogFragmentCompat implements DialogPreference.TargetFragment {

    private RangeSeekBar rangeSeekBar;
    private RangeSeekBarPreference pref;

    private static final int MIN_VALUE = 0;
    private static final int MAX_VALUE = 10;
    private int selectedMinValue;
    private int selectedMaxValue;

    public static RangeSeekBarPreferenceDialogFragment newInstance(String key){
        final RangeSeekBarPreferenceDialogFragment fragment = new RangeSeekBarPreferenceDialogFragment();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        rangeSeekBar = (RangeSeekBar) view.findViewById(R.id.custom_range_seekbar);
        initializeSeekBar();

        rangeSeekBar.setOnRangeSeekBarChangeListener(new RangeSeekBar.OnRangeSeekBarChangeListener() {
            @Override
            public void onRangeSeekBarValuesChanged(RangeSeekBar bar, Object minValue, Object maxValue) {
                selectedMinValue = (int)bar.getSelectedMinValue();
                selectedMaxValue = (int)bar.getSelectedMaxValue();
            }
        });
    }

    private void initializeSeekBar(){
        pref = (RangeSeekBarPreference) getPreference();
        rangeSeekBar.setRangeValues(MIN_VALUE, MAX_VALUE);
        rangeSeekBar.setTextAboveThumbsColor(Color.GRAY);

        int minValue = pref.getmMinValue();
        int maxValue = pref.getmMaxValue();

        // If minValue is smaller than MIN_Value or user has not set the imdb rating yet minValue is set to MIN_VALUE.
        if(minValue < MIN_VALUE || !pref.isInitialValueSet()){
            rangeSeekBar.setSelectedMinValue(MIN_VALUE);
            selectedMinValue = MIN_VALUE;
        } else{
            rangeSeekBar.setSelectedMinValue(minValue);
            selectedMinValue = minValue;
        }

        // If maxValue is greater than MAX_VALUE or user has not set the imdb rating yet maxValue is set to MAX_VALUE.
        if(maxValue > MAX_VALUE || !pref.isInitialValueSet()){
            rangeSeekBar.setSelectedMaxValue(MAX_VALUE);
            selectedMaxValue = MAX_VALUE;
        } else{
            rangeSeekBar.setSelectedMaxValue(maxValue);
            selectedMaxValue = maxValue;
        }

    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            pref.setmMinValue(selectedMinValue);
            pref.setmMaxValue(selectedMaxValue);
        }
    }

    @Nullable
    @Override
    public Preference findPreference(@NonNull CharSequence key) {
        return getPreference();
    }
}
