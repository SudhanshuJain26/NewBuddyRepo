package indwin.c3.shareapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.activities.VerifyOTPActivity;

/**
 * Created by shubhang on 13/04/16.
 */
public class SendOTP extends
        AsyncTask<String, Void, String> {

    Context context;
    int retryCount = 0;
    String phone;
    boolean resend = false;

    public SendOTP(Context context, String phone, boolean resend) {
        this.context = context;
        this.phone = phone;
        this.resend = resend;
    }

    @Override
    protected String doInBackground(String... data) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = context.getResources().getString(R.string.server) + "api/auth/sendotp?phone=" + phone;
            HttpPost postReq = new HttpPost(url);
            postReq.setHeader("Accept", "application/json");
            postReq.setHeader("Content-type", "application/json");
            SharedPreferences toks = context.getSharedPreferences("token", Context.MODE_PRIVATE);
            String tok_sp = toks.getString("token_value", "");
            postReq.setHeader("x-access-token", tok_sp);
            HttpResponse httpresponse = client.execute(postReq);
            String responseText = EntityUtils.toString(httpresponse.getEntity());
            JSONObject json = new JSONObject(responseText);
            if (json.get("status").toString().contains("success")) {
                if (json.get("msg").toString().contains("OTP sent")) {
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
        if (retryCount < 3) {
            retryCount++;
            if(result.equals("success")){
                if(resend){
                    VerifyOTPActivity.time.start();
                }
            }
            else if (result.equals("authFailed")) {
                new FetchNewToken(context).execute();
                this.execute();
            } else if (result.equals("fail"))
                this.execute();
        } else
            retryCount = 0;
    }
}