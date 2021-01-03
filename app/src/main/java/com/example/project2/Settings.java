package com.example.project2;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.PreferenceManager;
import java.util.Locale;

public class Settings extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    @Override
    protected void onCreate(Bundle savedInstanceState)  {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isLight = sharedPreferences.getBoolean(getString(R.string.theme_key), true);
            if(isLight){
                setTheme(R.style.lightMode);
            }else {
                setTheme(R.style.darkMode);
            }
            String language = sharedPreferences.getString(getString(R.string.language_key), getString(R.string.pref_language_value_english));

        setLocal(language);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, new SettingsFragment()).commit();


        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
    }

    private void setLocal(String lang) {
        Locale locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration con = new Configuration();
        con.locale = locale;
        getBaseContext().getResources().updateConfiguration(con, getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

        if(key.equals(getString(R.string.language_key))){
            String language= sharedPreferences.getString(key, "en");
            setLocal(language);
        }
        else if(key.equals(getString(R.string.theme_key))){
            boolean isLight = sharedPreferences.getBoolean(key, true);
            if(isLight){
                setTheme(R.style.lightMode);
            }else {
                setTheme(R.style.darkMode);
            }
        }
        recreate();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}