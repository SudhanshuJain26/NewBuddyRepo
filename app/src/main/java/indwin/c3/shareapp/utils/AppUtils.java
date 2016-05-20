package indwin.c3.shareapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.HashMap;

import indwin.c3.shareapp.models.UserModel;
import io.intercom.com.google.gson.Gson;

/**
 * Created by rock on 5/10/16.
 */
public class AppUtils {

    static HashMap<String, HashMap<String, String>> image=new HashMap<>();
    static HashMap<String, HashMap<String, String>> mrp1;
    static HashMap<String, HashMap<String, String>> fkid1;
    static HashMap<String, HashMap<String, String>> title;
    static HashMap<String, HashMap<String, String>> sellers;
    static HashMap<String, HashMap<String, String>> selling;
    static HashMap<String, HashMap<String, String>> category;
    static HashMap<String, HashMap<String, String>> subCategory;
    static HashMap<String, HashMap<String, String>> brand;
    public static final String APP_NAME = "buddy";
    public static final String USER_OBJECT = "UserObject";
    public static final String IMAGE = "image";
    public static final String POSITION = "position";
    public static final String SOURCE = "source";
    public static final String HEADING = "heading";
    public static final String TnC_URL = "https://hellobuddy.in/termsApp";
    public static int TIMEOUT_MILLISECS = 30000;

    public static boolean isNotEmpty(String checkString) {
        if (checkString != null && !checkString.trim().isEmpty()) {
            return true;
        }
        return false;


    }


    public static HttpResponse connectToServerGet(String url, String x_access_token, String basicAuth) {

        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams
                .setConnectionTimeout(httpParameters, 30000);

        HttpClient client = new DefaultHttpClient(httpParameters);
        //                String url2="http://54.255.147.43:80/api/user/form?phone="+sh_otp.getString("number","");

        HttpGet httppost = new HttpGet(url);


        httppost.setHeader("x-access-token", x_access_token);


        httppost.setHeader("Content-Type", "application/json");


        try {
            HttpResponse response = client.execute(httppost);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }


    public static HttpResponse connectToServerPost(String url, String json, String x_access_token) {
        HttpParams httpParameters = new BasicHttpParams();

        HttpConnectionParams
                .setConnectionTimeout(httpParameters, 30000);

        HttpClient client = new DefaultHttpClient(httpParameters);

        HttpPost httppost = new HttpPost(url);
        httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
        httppost.setHeader("Content-Type", "application/json");
        if (isNotEmpty(x_access_token)) {

            httppost.setHeader("x-access-token", x_access_token);
        }

        try {
            if (isNotEmpty(json)) {
                StringEntity entity = new StringEntity(json);
                httppost.setEntity(entity);
            }

            HttpResponse response = client.execute(httppost);
            return response;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
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
