package indwin.c3.shareapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.AccountSettingsActivity;

/**
 * Created by shubhang on 18/04/16.
 */
public class VerifyEmail extends
        AsyncTask<String, Void, String> {

    Context context;
    int retryCount = 0;
    String phone;
    String email;

    public VerifyEmail(Context context, String phone, String email) {
        this.context = context;
        this.phone = phone;
        this.email = email;
    }

    @Override
    protected String doInBackground(String... data) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = context.getResources().getString(R.string.server) + "api/auth/verify/email?phone=" + phone + "&email=" + email;
            HttpGet postReq = new HttpGet(url);
            postReq.setHeader("Accept", "application/json");
            postReq.setHeader("Content-type", "application/json");
            SharedPreferences toks = context.getSharedPreferences("token", Context.MODE_PRIVATE);
            String tok_sp = toks.getString("token_value", "");
            postReq.setHeader("x-access-token", tok_sp);
            HttpResponse httpresponse = client.execute(postReq);
            String responseText = EntityUtils.toString(httpresponse.getEntity());
            JSONObject json = new JSONObject(responseText);
            if (json.get("status").toString().contains("success")) {
                if (json.get("msg").toString().contains("sent")) {
                    return "success";
                }
            } else {
                if (json.get("status").toString().contains("error")) {
                    Log.d("Error", json.get("msg").toString());
                    return "fail";
                } else if (json.get("msg").toString().contains("Invalid Token")) {
                    return "authFailed";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "fail";
    }

    protected void onPostExecute(String result) {
        try {
            if (retryCount < 3) {
                retryCount++;
                if (result.equals("success")) {
                    Toast.makeText(context,context.getResources().getString(R.string.confirmation_email_link),Toast.LENGTH_SHORT).show();
                    AccountSettingsActivity.verifyEmail.setText("Check");
                } else if (result.equals("authFailed")) {
                    new FetchNewToken(context).execute();
                    new VerifyEmail(context, phone, email).execute();
                } else if (result.equals("fail"))
                    new VerifyEmail(context, phone, email).execute();
            } else
                retryCount = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}