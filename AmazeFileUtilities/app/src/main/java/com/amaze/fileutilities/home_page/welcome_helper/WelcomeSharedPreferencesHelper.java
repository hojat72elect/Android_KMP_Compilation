package com.amaze.fileutilities.home_page.welcome_helper;

import android.content.Context;
import android.content.SharedPreferences;


public class WelcomeSharedPreferencesHelper {

    private static final String KEY_SHARED_PREFS = "com.stephentuso.welcome";
    private static final String KEY_HAS_RUN = "welcome_screen_has_run";

    private static SharedPreferences getSharedPrefs(Context context) {
        return context.getSharedPreferences(KEY_SHARED_PREFS, Context.MODE_PRIVATE);
    }

    public static boolean welcomeScreenCompleted(Context context, String welcomeScreenKey) {
        return getCompletedFromPreferences(getSharedPrefs(context), welcomeScreenKey);
    }

    public static void storeWelcomeCompleted(Context context, String welcomeScreenKey) {
        getSharedPrefs(context).edit().putBoolean(getKey(welcomeScreenKey), true).apply();
    }

    private static boolean getCompletedFromPreferences(SharedPreferences preferences, String welcomeScreenKey) {
        return preferences.getBoolean(getKey(welcomeScreenKey), false);
    }

    private static String getKey(String welcomeKey) {
        return KEY_HAS_RUN + welcomeKey;
    }

}
