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
import at.technikumwien.mc2020.ui.main.MainActivity;
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

        final Preference logoutPreference = findPreference(getString(R.string.pref_logout_key));
        logoutPreference.setEnabled(FirebaseAuth.getInstance().getCurrentUser() != null ? true : false);

        DialogInterface.OnClickListener btnClick = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

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
}
