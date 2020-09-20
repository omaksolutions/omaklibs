package com.omak.omakhelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import org.json.JSONException;
import org.json.JSONObject;

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

    /*Getting Key From This Helper Class*/
    public static String getPrefString(String key, String variable, Context context) {
        String variableString = preferences.getString(variable, "");

        // Return if the preferences does not have requested variable stored
        if (variableString.isEmpty()) return null;

        try {
            JSONObject variableObj = new JSONObject(variableString);
            if (!key.isEmpty()) {
                String keyString = variableObj.has(key) ? variableObj.getString(key) : "";
                return keyString;
            }
        } catch (final JSONException e) {
            HelperFunctions.theLogger("JSONException", "Error for preference key: " + key + " & variable: " + variable + " with error: " + e.getMessage());
            return null;
        }
        return null;
    }

    public static boolean getPreference(Context context, String key) {
        return preferences.getBoolean(key, true);
    }

    public static String getPreference(Context context, String key, String defaultValue) {
        return preferences.getString(key, defaultValue);
    }

    public void setPreference(String Key, String value) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(Key, value);
        commitAndClear(editor);
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