package at.technikumwien.mc2020.ui.settings.custom.preference.seekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.preference.DialogPreference;

import at.technikumwien.mc2020.R;

public class RangeSeekBarPreference extends DialogPreference {

    private int mMinValue;

    private int mMaxValue;

    private boolean isInitialValueSet = false;

    public RangeSeekBarPreference(Context context){
        super(context);
    }

    public RangeSeekBarPreference(Context context, AttributeSet attrs,
                                  int defStyle) {
        super(context, attrs, defStyle);
    }

    public RangeSeekBarPreference(Context context, AttributeSet attrs) {
        super(context);
        setDialogLayoutResource(R.layout.custom_range_seekbar_preference);

        setKey(context.getResources().getString(R.string.pref_imdb_rating_key));
        setTitle(context.getResources().getString(R.string.pref_imdb_rating_label));
        setDialogTitle(context.getResources().getString(R.string.imdb_rating_dialog_title));
        setPositiveButtonText(context.getResources().getString(R.string.ok_Button));
        setNegativeButtonText(context.getResources().getString(R.string.cancel_Button));
    }

    public boolean isInitialValueSet() {
        return isInitialValueSet;
    }

    public int getmMinValue() {
        return mMinValue;
    }

    public void setmMinValue(int mMinValue) {

        if(this.mMinValue != mMinValue) {
            this.mMinValue = mMinValue;

            persistValue();
        }
    }

    public int getmMaxValue() {
        return mMaxValue;
    }

    public void setmMaxValue(int mMaxValue) {

        if(this.mMaxValue != mMaxValue) {
            this.mMaxValue = mMaxValue;

            persistValue();
        }
    }

    private void persistValue(){
        persistString(this.mMinValue + "-" + this.mMaxValue);
        setSummary(getCustomSummary());
    }

    private void setPersistedValue(){
        String persistedValues = getPersistedString(this.mMinValue + "-" + this.mMaxValue);
        String[] values = persistedValues.split("-");
        this.mMinValue = Integer.valueOf(values[0]);
        this.mMaxValue = Integer.valueOf(values[1]);
    }

    private String getCustomSummary(){
        return getmMinValue() + "-" + getmMaxValue();
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {

        return a.getInt(index, 0);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        setPersistedValue();
        setSummary(getCustomSummary());
        isInitialValueSet = true;
    }
}
