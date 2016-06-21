package indwin.c3.shareapp.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import indwin.c3.shareapp.BuildConfig;
import indwin.c3.shareapp.activities.ChangePasswordActivity;

/**
 * Created by shubhang on 14/04/16.
 */
public class VerifyOTP extends
        AsyncTask<String, Void, String> {

    Context context;
    int retryCount = 0;
    String phone;
    String otp;

    public VerifyOTP(Context context, String phone, String otp) {
        this.context = context;
        this.phone = phone;
        this.otp = otp;
    }

    @Override
    protected String doInBackground(String... data) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = BuildConfig.SERVER_URL + "api/auth/verifyotp?phone=" + phone;
            HttpPost postReq = new HttpPost(url);
            JSONObject jsonobj = new JSONObject();
            jsonobj.put("phone",phone);
            jsonobj.put("otp", otp);
            StringEntity se = new StringEntity(jsonobj.toString());
            postReq.setHeader("Accept", "application/json");
            postReq.setHeader("Content-type", "application/json");
            SharedPreferences toks = context.getSharedPreferences("token", Context.MODE_PRIVATE);
            String tok_sp = toks.getString("token_value", "");
            postReq.setHeader("x-access-token", tok_sp);
            postReq.setEntity(se);
            HttpResponse httpresponse = client.execute(postReq);
            String responseText = EntityUtils.toString(httpresponse.getEntity());
            JSONObject json = new JSONObject(responseText);
            if (json.get("status").toString().contains("success")) {
                if (json.get("msg").toString().contains("OTP verified")) {
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
                Intent intent = new Intent(context,ChangePasswordActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("source","VerifyOTPActivity");
                context.startActivity(intent);
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