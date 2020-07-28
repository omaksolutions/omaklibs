package com.omak.omakhelpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.realm.RealmObject;

public class HelperFunctions {

    private static final String PREFS_NAME = "preferenceName";

    /*
        Get individual key from an object
        Returns:
            Null when object is null OR object is not a JSONObject
            String - whey key is available
            emptyString - when key does not exist
     */

    public static String getUserFullName(Context context) {
        SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String variableString = preferences.getString("currentUser", "");

        // Return if the preferences does not have requested variable stored
        if (variableString.isEmpty()) return null;

        try {
            JSONObject variableObj = new JSONObject(variableString);
            String first_name = variableObj.has("first_name") ? variableObj.getString("first_name") : "";
            String last_name = variableObj.has("last_name") ? variableObj.getString("last_name") : "";
            return first_name + " " + last_name;

        } catch (final JSONException e) {
            HelperFunctions.theLogger("JSONException", "Json parsing error: " + e.getMessage());
        }

        return null;
    }

    /*Getting Key From This Helper Class*/
    public static String getPrefString(String key, String variable, Context context) {
        SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
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
            HelperFunctions.theLogger("JSONException", "Json parsing error: " + e.getMessage());
            return null;
        }
        return null;
    }

    public static String getUser_id(Context context) {
        SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String variableString = preferences.getString("currentUser", "");

        // Return if the preferences does not have requested variable stored
        if (variableString.isEmpty()) return null;

        try {
            JSONObject variableObj = new JSONObject(variableString);
            String User_id = variableObj.has("id") ? variableObj.getString("id") : "";
            //String last_name = variableObj.has("last_name") ? variableObj.getString("last_name") : "";
            return User_id;

        } catch (final JSONException e) {
            HelperFunctions.theLogger("JSONException", "Json parsing error: " + e.getMessage());
        }

        return null;
    }


    /*Not USed Delete this */
    public static Boolean getPrefBoolean(String key, String variable, Context context) {
        SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String variableString = preferences.getString(variable, "");

        // Return if the preferences does not have requested variable stored
        if (variableString.isEmpty()) return false;

        try {
            JSONObject variableObj = new JSONObject(variableString);
            if (!key.isEmpty()) {
                Boolean keyString = variableObj.has(key) && variableObj.getBoolean(key);
                return keyString;
            }
        } catch (final JSONException e) {
            HelperFunctions.theLogger("JSONException", "Json parsing error: " + e.getMessage());
            return false;
        }
        return false;
    }

    public static JSONObject getPreferences(String key, String variable, Context context) {
        SharedPreferences preferences = android.preference.PreferenceManager.getDefaultSharedPreferences(context);
        String variableString = preferences.getString(variable, "");
        HelperFunctions.theLogger("getPreferences", "Current object is - " + variableString);
        if (variableString.isEmpty()) return null;

        try {
            JSONObject variableObj = new JSONObject(variableString);

            if (!key.isEmpty()) {
                String keyString = variableObj.getString(key);
                //HelperFunctions.theLogger("Test myData", "" + data);
                JSONObject keyObj = new JSONObject(keyString);
                return keyObj;
            }

            return variableObj;

        } catch (final JSONException e) {
            HelperFunctions.theLogger("JSONException", "Json parsing error: " + e.getMessage());
            return null;
        }
    }

    /*Method For Print Log */
    public static void toaster(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void theLogger(String tag, String message) {
        Log.e(tag, message);
    }

    public static String getObjectString(JSONObject obj, String key) {
        try {
            return obj.getString(key);
        } catch (final JSONException e) {
            HelperFunctions.theLogger("JSONException", "Json parsing error: " + e.getMessage());
            return null;
        }
    }

    public static Boolean getObjectString(JSONObject obj, String key, Boolean defaultvalue) {
        try {
            return obj.getBoolean(key);
        } catch (final JSONException e) {
            HelperFunctions.theLogger("JSONException", "Json parsing error: " + e.getMessage());
            return defaultvalue;
        }
    }

    /*Get Device Name Here*/
    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            HelperFunctions.theLogger("My Device Name ", "IS" + model);
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    public static JSONObject addApiPair(String key, String value) {
        JSONObject keyValuePair = new JSONObject();
        try {
            keyValuePair.put("key", key);
            keyValuePair.put("value", value);
        } catch (JSONException e) {

        }
        return keyValuePair;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;

        StringBuilder phrase = new StringBuilder();
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c));
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase.append(c);
        }

        return phrase.toString();
    }

    public static <T extends Object> void printTask(String tag, T task) {
        theLogger("Task", tag + ": " + new Gson().toJson(task));
    }


    public static String upperCaseLetter(String str) {
        StringBuffer capBuffer = new StringBuffer();
        if (str.isEmpty() || str.equals("") || str == null) {
            return " ";
        }
        Matcher capMatcher = Pattern.compile("([a-z])([a-z]*)", Pattern.CASE_INSENSITIVE).matcher(str);
        while (capMatcher.find()) {
            capMatcher.appendReplacement(capBuffer, capMatcher.group(1).toUpperCase() + capMatcher.group(2).toLowerCase());
        }

        return capMatcher.appendTail(capBuffer).toString();

    }

    public static String lowerCaseLetter(String str) {
        if (!str.isEmpty()) {
            return str.toLowerCase();

        }
        return str;
    }



    /**
     * helper method for set array adapter for spinner
     * @param context
     * @param options
     * @return
     */
    public static SpinnerAdapter setArrayAdapterHelperMethod(Context context, String[] options) {
        ArrayAdapter arrayAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, options);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return arrayAdapter;
    }

    /**
     * check that value of field is empty or null
     * @param str
     * @return
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
