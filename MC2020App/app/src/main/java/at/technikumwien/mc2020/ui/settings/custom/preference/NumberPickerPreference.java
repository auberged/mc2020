package at.technikumwien.mc2020.ui.settings.custom.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.NumberPicker;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;
import androidx.preference.PreferenceDialogFragmentCompat;

import at.technikumwien.mc2020.R;

public class NumberPickerPreference extends DialogPreference {

    private static final int DEFAULT_YEAR = 2020;

    private int mPickedNumber;

    public NumberPickerPreference(Context context){
        super(context);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs,
                                    int defStyle) {
        super(context, attrs, defStyle);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        super(context);
        setDialogLayoutResource(R.layout.custom_number_picker_preference);

        setKey(context.getResources().getString(R.string.pref_release_year_key));
        setTitle(context.getResources().getString(R.string.pref_release_year_label));
        setDialogTitle(context.getResources().getString(R.string.dialog_title));
        setPositiveButtonText(context.getResources().getString(R.string.ok_Button));
        setNegativeButtonText(context.getResources().getString(R.string.cancel_Button));
    }


    public int getmPickedNumber() {
        return mPickedNumber;
    }

    public void setmPickedNumber(int mPickedNumber) {

        if(this.mPickedNumber != mPickedNumber) {
            this.mPickedNumber = mPickedNumber;

            persistInt(mPickedNumber);
            setSummary(String.valueOf(mPickedNumber));
        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        setmPickedNumber(getPersistedInt(mPickedNumber));
        setSummary(String.valueOf(mPickedNumber));
    }
}
