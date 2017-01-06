package com.escuelapp.utility;

import android.content.Context;
import android.preference.PreferenceManager;

import com.google.firebase.auth.FirebaseAuth;

/**
 * Created by alan on 10/1/16.
 */
public class Utility {

    public static void saveUserUid(Context context, String userUid) {
        if (userUid != null) {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putString(Constants.USER_UID, userUid).commit();
        }
    }

    public static String getUserUid(Context context) {
        String userUid = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.USER_UID, null);
        if (userUid == null) {
            userUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            saveUserUid(context, userUid);
        }
        return userUid;
    }

    public static void deleteUserUid(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(Constants.USER_UID).commit();
    }

    public static void saveUserName(Context context, String userName) {
        if (userName != null) {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putString(Constants.USER_NAME, userName).commit();
        }
    }

    public static String getUserName(Context context) {
        String userName = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.USER_NAME, null);
        if (userName == null) {
            userName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            saveUserName(context, userName);
        }
        return userName;
    }

    public static void deleteUserName(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(Constants.USER_NAME).commit();
    }

    public static void saveUserEmail(Context context, String userEmail) {
        if (userEmail != null) {
            PreferenceManager.getDefaultSharedPreferences(context).edit()
                    .putString(Constants.USER_EMAIL, userEmail).commit();
        }
    }

    public static String getUserEmail(Context context) {
        String userEmail = PreferenceManager.getDefaultSharedPreferences(context).getString(Constants.USER_EMAIL, null);
        if (userEmail == null) {
            userEmail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            saveUserEmail(context, userEmail);
        }
        return userEmail;
    }

    public static void deleteUserEmail(Context context) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().remove(Constants.USER_EMAIL).commit();
    }
}
