package at.technikumwien.mc2020.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import java.util.Set;

import at.technikumwien.mc2020.R;
import at.technikumwien.mc2020.ui.settings.custom.preference.NumberPickerPreference;
import at.technikumwien.mc2020.ui.settings.custom.preference.NumberPickerPreferenceDialogFragment;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_filter_criteria);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        for(int i = 0; i < count; i++){
            Preference p = prefScreen.getPreference(i);
            if(p instanceof ListPreference){
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
            if(p instanceof  MultiSelectListPreference){
                Set<String> selectedValues = sharedPreferences.getStringSet(p.getKey(), null);
                String countSelectedValues = selectedValues != null ? String.valueOf(selectedValues.size()) : "0";
                setPreferenceSummary(p, countSelectedValues);
            }
        }
    }

    private void setPreferenceSummary(Preference preference, String value){
        if(preference instanceof ListPreference){
            ListPreference listPreference = (ListPreference) preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if(prefIndex >= 0){
                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
        if(preference instanceof MultiSelectListPreference){
            MultiSelectListPreference multiSelectListPreference = (MultiSelectListPreference) preference;
            multiSelectListPreference.setSummary(value + " " + getString(R.string.genreCountSelectedMessage));
        }

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if( preference != null){
            if(preference instanceof ListPreference){
                String value = sharedPreferences.getString(preference.getKey(), "");
                setPreferenceSummary(preference, value);
            }
            if(preference instanceof  MultiSelectListPreference){
                Set<String> selectedValues = sharedPreferences.getStringSet(preference.getKey(), null);
                String countSelectedValues = selectedValues != null ? String.valueOf(selectedValues.size()) : "";
                setPreferenceSummary(preference, countSelectedValues);
            }
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment dialogFragment = null;
        if(preference instanceof NumberPickerPreference){
            dialogFragment = NumberPickerPreferenceDialogFragment.newInstance(preference.getKey());
        }

        if(dialogFragment != null){
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), TAG);
        } else{
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
