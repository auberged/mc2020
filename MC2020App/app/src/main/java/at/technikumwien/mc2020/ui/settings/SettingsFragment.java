package at.technikumwien.mc2020.ui.settings;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.preference.ListPreference;
import androidx.preference.MultiSelectListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Set;

import at.technikumwien.mc2020.R;
import at.technikumwien.mc2020.ui.settings.custom.preference.number.picker.NumberPickerPreference;
import at.technikumwien.mc2020.ui.settings.custom.preference.number.picker.NumberPickerPreferenceDialogFragment;
import at.technikumwien.mc2020.ui.settings.custom.preference.seekbar.RangeSeekBarPreference;
import at.technikumwien.mc2020.ui.settings.custom.preference.seekbar.RangeSeekBarPreferenceDialogFragment;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    private static String TAG = SettingsFragment.class.getSimpleName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.pref_filter_criteria);

        SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
        PreferenceScreen prefScreen = getPreferenceScreen();
        int count = prefScreen.getPreferenceCount();

        setGenreDependingOfType(sharedPreferences);
        for(int i = 0; i < count; i++){
            Preference p = prefScreen.getPreference(i);
            // Summary for genre preference is set in setGenreDependingOfType()
            if(p instanceof ListPreference && !p.getKey().equals(R.string.pref_tv_genre_key) && !p.getKey().equals(R.string.pref_movie_genre_key)){
                String value = sharedPreferences.getString(p.getKey(), "");
                setPreferenceSummary(p, value);
            }
            if(p instanceof  MultiSelectListPreference){
                Set<String> selectedValues = sharedPreferences.getStringSet(p.getKey(), null);
                String countSelectedValues = selectedValues != null ? String.valueOf(selectedValues.size()) : "0";
                setPreferenceSummary(p, countSelectedValues);
            }
        }

        final Preference logoutPreference = findPreference(getString(R.string.pref_logout_key));
        logoutPreference.setEnabled(FirebaseAuth.getInstance().getCurrentUser() != null ? true : false);

        logoutPreference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                AlertDialog dialog = new AlertDialog.Builder(getContext())
                        .setTitle(R.string.logout_dialog_title)
                        .setMessage(R.string.logout_dialog_message)
                        .setPositiveButton(R.string.logout_dialog_ok, new DialogInterface.OnClickListener(){

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AuthUI.getInstance()
                                        .signOut(getContext())
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            public void onComplete(@NonNull Task<Void> task) {
                                                logoutPreference.setEnabled(false);
                                                Toast toast = Toast.makeText(getContext(), R.string.logout_dialog_success, Toast.LENGTH_LONG);
                                                toast.show();
                                            }
                                        });
                            }
                        })
                        .setNegativeButton(R.string.logout_dialog_cancel, null)
                        .show();

                return false;
            }
        });



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
                setSummaryOfMulitSelectListPreference(key, sharedPreferences, (MultiSelectListPreference) preference);
            }
        }

        // If type has been changed genre list has to be updated
        if(key.equals(getContext().getResources().getString(R.string.pref_type_key))){
            setGenreDependingOfType(sharedPreferences);
        }
    }

    private void setSummaryOfMulitSelectListPreference(String key, SharedPreferences sharedPreferences, MultiSelectListPreference preference){
        Set<String> selectedValues = sharedPreferences.getStringSet(key, null);
        if(selectedValues != null) {
            preference.setValues(selectedValues);
        }

        String countSelectedValues = selectedValues != null ? String.valueOf(selectedValues.size()) : "";
        setPreferenceSummary(preference, countSelectedValues);
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
        } else if(preference instanceof RangeSeekBarPreference){
            dialogFragment = RangeSeekBarPreferenceDialogFragment.newInstance(preference.getKey());
        }

        if(dialogFragment != null){
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(this.getFragmentManager(), TAG);
        } else{
            super.onDisplayPreferenceDialog(preference);
        }
    }

    private void setGenreDependingOfType(SharedPreferences pref){
        String type = pref.getString(getContext().getResources().getString(R.string.pref_type_key), getContext().getResources().getString(R.string.pref_type_movies_value));
        ListPreference preference = (ListPreference) findPreference(getContext().getResources().getString(R.string.pref_tv_genre_key));
        if(preference == null){
            preference = (ListPreference) findPreference(getContext().getResources().getString(R.string.pref_movie_genre_key));
        }

        String key = "";
        if(type.equals(getContext().getResources().getString(R.string.pref_type_movies_value))){
            key = getString(R.string.pref_movie_genre_key);
            preference.setKey(key);
            preference.setEntries(R.array.pref_movie_genre_option_labels);
            preference.setEntryValues(R.array.pref_movie_genre_option_values);
            String value = pref.getString(key, getString(R.string.pref_movie_genre_default_value));
            preference.setValue(value);
            setPreferenceSummary(preference, value);
        } else if(type.equals(getContext().getResources().getString(R.string.pref_type_series_value))){
            key = getString(R.string.pref_tv_genre_key);
            preference.setKey(key);
            preference.setEntries(R.array.pref_tv_genre_option_labels);
            preference.setEntryValues(R.array.pref_tv_genre_option_values);
            String value = pref.getString(key, getString(R.string.pref_tv_genre_default_value));
            preference.setValue(value);
            setPreferenceSummary(preference, value);
        }
    }
}
