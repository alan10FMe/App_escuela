package com.escuelapp.utility;

import android.content.Context;
import android.preference.PreferenceManager;

/**
 * Created by alan on 10/1/16.
 */
public class Utility {

    private static final String USER_UID = "user_uid";

    public static void saveUserUid(Context context, String userUid) {
        if (userUid != null) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putString(USER_UID, userUid).commit();
        }
    }

    public static String getUserUid(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(USER_UID, null);
    }

    public static void deleteUserUid(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(USER_UID).commit();
    }
}
