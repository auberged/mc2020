package at.technikumwien.mc2020.ui.settings.custom.preference.number.picker;

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceDialogFragmentCompat;

import java.util.Calendar;

import at.technikumwien.mc2020.R;

public class NumberPickerPreferenceDialogFragment extends PreferenceDialogFragmentCompat implements DialogPreference.TargetFragment {

    private NumberPicker numberPicker;
    private NumberPickerPreference pref;

    private static final int MIN_YEAR = 1950;
    private static final int MAX_YEAR = 2099;

    public static NumberPickerPreferenceDialogFragment newInstance(String key){
        final NumberPickerPreferenceDialogFragment fragment = new NumberPickerPreferenceDialogFragment();
        final Bundle bundle = new Bundle(1);
        bundle.putString(ARG_KEY, key);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        numberPicker = (NumberPicker)view.findViewById(R.id.custom_number_picker);
        numberPicker.setMinValue(MIN_YEAR);
        numberPicker.setMaxValue(MAX_YEAR);
        pref = (NumberPickerPreference) getPreference();
        int value = pref.getmPickedNumber();
        if(value < MIN_YEAR){
            value = Calendar.getInstance().get(Calendar.YEAR);;
        }
        numberPicker.setValue(value);
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if(positiveResult){
            pref.setmPickedNumber(numberPicker.getValue());
        }
    }

    @Nullable
    @Override
    public Preference findPreference(@NonNull CharSequence key) {
        return getPreference();
    }
}
