package com.omak.omakhelpers;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

public class PreferencesHelper {
    public static SharedPreferences preferences;
    private static String PREFS_NAME = "omaklib";
    Context context;

    /**
     *
     * @param context
     */
    public PreferencesHelper(Context context) {
        this.context = context;
    }

    public void setPrefName(String pref_name) {
        PREFS_NAME = pref_name;
        preferences = context.getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
    }

    public static boolean getPreference(Context context, String key) {
        return preferences.getBoolean(key, true);
    }

    public static String getPreference(Context context, String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public static void setPreference(Context context, String Key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Key, value);
        commitAndClear(editor);
    }

    public static void setPreference(Context context, String Key, Boolean value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(Key, value);
        commitAndClear(editor);
    }

    public static void setPreference(Context context, String Key, Integer value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(Key, value);
        commitAndClear(editor);
    }

    public static void commitAndClear(SharedPreferences.Editor editor) {
        editor.commit();
    }

}