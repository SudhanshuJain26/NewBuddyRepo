package indwin.c3.shareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import com.facebook.internal.Utility;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by USER on 3/7/2016.
 */
public class CampaignTrackingService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle extras = intent.getExtras();

            String referrerString = extras.getString("referrer");
            // Next line uses my helper function to parse a query (eg "a=b&c=d") into key-value pairs
//        HashMap<String, String> getParams = Utility.getHashMapFromQuery(referrerString);
//        String source = getParams.get("utm_campaign");

            if (referrerString != null) {
                SharedPreferences preferences = context.getSharedPreferences("Referral", Context.MODE_PRIVATE);
                SharedPreferences.Editor preferencesEditor = preferences.edit();
                preferencesEditor.putString("referrer", referrerString);
                preferencesEditor.commit();
            }

            // Pass along to google
//        AnalyticsReceiver receiver = new AnalyticsReceiver();
//        receiver.onReceive(context, intent);
        }
        catch(Exception e)
        {}

    }

}
