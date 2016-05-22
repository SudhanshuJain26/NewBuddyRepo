package indwin.c3.shareapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import indwin.c3.shareapp.R;
import indwin.c3.shareapp.fragments.ProfileFormStep1Fragment1;
import indwin.c3.shareapp.models.UserModel;

/**
 * Created by shubhang on 26/04/16.
 */
public class FetchLatestUserDetails extends
                                    AsyncTask<String, Void, String> {

    Context context;
    int retryCount = 0;
    String phone;
    private UserModel userModel;

    public FetchLatestUserDetails(Context context, String phone,UserModel userModel) {
        this.context = context;
        this.phone = phone;
        this.userModel = userModel;
    }

    @Override
    protected void onPreExecute() {
        ProfileFormStep1Fragment1.verifyEmail.setText("...");
    }

    @Override
    protected String doInBackground(String... data) {
        try {
            HttpClient client = new DefaultHttpClient();
            String url = context.getResources().getString(R.string.server) + "api/user/form?phone=" + phone;
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
                if (json.get("msg").toString().contains("successfully")) {
                    if (json.opt("data") != null) {
                        JSONObject data1 = json.getJSONObject("data");
                        if (data1.opt("emailVerified") != null)
                            if (data1.getBoolean("emailVerified")) {
                                return "success";
                            }
                    }
                    return "notVerified";
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
                    ProfileFormStep1Fragment1.verifyEmail.setText("Verified!");
                    ProfileFormStep1Fragment1.verifyEmail.setTextColor(Color.GRAY);
                    ProfileFormStep1Fragment1.verifyEmail.setClickable(false);
                    ProfileFormStep1Fragment1.verifyEmail.setEnabled(false);
                    ProfileFormStep1Fragment1.completeEmail.setVisibility(View.VISIBLE);
                    ProfileFormStep1Fragment1.incompleteEmail.setVisibility(View.GONE);
                    AppUtils.saveUserObject(context, userModel);

                } else if (result.equals("notVerified")) {
                    ProfileFormStep1Fragment1.verifyEmail.setText("Check");
                    Toast.makeText(context, "Please try again", Toast.LENGTH_SHORT).show();
                } else if (result.equals("authFailed")) {
                    new FetchNewToken(context).execute();
                    new FetchLatestUserDetails(context, phone,userModel).execute();
                } else if (result.equals("fail"))
                    new FetchLatestUserDetails(context, phone,userModel).execute();
            } else
                retryCount = 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}