package indwin.c3.shareapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import indwin.c3.shareapp.models.UserModel;
import io.intercom.com.google.gson.Gson;

/**
 * Created by rock on 5/10/16.
 */
public class AppUtils {
    public static final String APP_NAME = "buddy";
    public static final String USER_OBJECT = "UserObject";
    public static final String IMAGE = "image";
    public static final String POSITION = "position";
    public static final String SOURCE = "source";
    public static final String HEADING = "heading";
    public static final String TnC_URL = "https://hellobuddy.in/termsApp";

    public static boolean isNotEmpty(String checkString) {
        if (checkString != null && !checkString.trim().isEmpty()) {
            return true;
        }
        return false;


    }

    public static UserModel getUserObject(Context context) {
        String json = getFromSharedPrefs(context, USER_OBJECT);
        return new Gson().fromJson(json, UserModel.class);
    }

    public static void saveUserObject(Context context, UserModel userModel) {
        String userJson = new Gson().toJson(userModel);
        saveInSharedPrefs(context, USER_OBJECT, userJson);
    }

    public static boolean isEmpty(String value) {
        return !isNotEmpty(value);
    }

    public static enum uploadStatus {
        OPEN {
            @Override
            public String toString() {

                return "open";
            }
        },
        PICKED {
            @Override
            public String toString() {

                return "picked";
            }
        }, UPLOADED {
            @Override
            public String toString() {

                return "uploaded";
            }
        }

    }

    public static boolean isOnline(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        return false;
    }


    public static String getFromSharedPrefs(Context context, String key) {
        SharedPreferences editor = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);
        return editor.getString(key, "");

    }

    public static void saveInSharedPrefs(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.commit();

    }


}
