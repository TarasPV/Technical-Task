package com.example.technicaltask.Tools;

import android.content.Context;
import android.content.SharedPreferences;

public class Utils {

    public static void putPrefsByKey(Context context, String key, boolean data) {
        SharedPreferences.Editor ed = context.getSharedPreferences(Constants.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).edit();
        ed.putBoolean(key, data);
        ed.apply();
    }

    public static boolean getPrefsBoolByKey(Context context, String key, boolean defaultVal) {
        return context.getSharedPreferences(Constants.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getBoolean(key, defaultVal);
    }
}
