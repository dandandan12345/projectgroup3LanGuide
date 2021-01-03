package com.example.project2;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.example.project2.R;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences_setting, rootKey);


        SharedPreferences sharedPreferences = getPreferenceManager().getSharedPreferences();
        // find listPreference to set summary for it
        ListPreference listPreference =findPreference(getString(R.string.language_key));
        if(listPreference!=null){
            String value = sharedPreferences.getString(listPreference.getKey(), "");
            setPreferenceSummary(listPreference, value);
        }
    }
    // set preference summary for listPreference(language)
    // we need to set label text in summary not the value because the value will not be translated
    private void setPreferenceSummary(Preference preference, String value){

        if(preference instanceof ListPreference){

            ListPreference listPreference = (ListPreference)preference;
            int prefIndex = listPreference.findIndexOfValue(value);
            if(prefIndex>=0){

                listPreference.setSummary(listPreference.getEntries()[prefIndex]);
            }
        }
    }
    // this method is called when the preferences change
    // we need to update summary
    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);
        if(null !=preference){
            if(preference instanceof ListPreference){
                String value = sharedPreferences.getString(preference.getKey(),"");
                setPreferenceSummary(preference,value);
            }

        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // register onPreferenceChaneListener
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // unregister onPreferenceChaneListener
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

}