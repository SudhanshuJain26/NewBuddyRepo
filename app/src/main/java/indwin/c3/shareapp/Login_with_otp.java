package indwin.c3.shareapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class Login_with_otp extends AppCompatActivity {
    String url = "";
    String url_otp="";
private ProgressBar spinner;
    String userid="";
    static String token="";
    EditText phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_with_otp);
        spinner=(ProgressBar)findViewById(R.id.progressBar1);
url=getApplicationContext().getString(R.string.server)+"authenticate";
        url_otp=getApplicationContext().getString(R.string.server)+"api/login/sendotp";
        phone=(EditText)findViewById(R.id.phone_number);
        try {
            //Intercom.client().reset();
        }
        catch (Exception e)
        {System.out.println(e.toString()+"int logotp");}

        TextView login_pass=(TextView)findViewById(R.id.Login_pass);
        login_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent in =new Intent(Login_with_otp.this,MainActivity.class);
                finish();
                startActivity(in);
                overridePendingTransition(0, 0);
            }
        });
        TextView loginotp=(TextView)findViewById(R.id.Login);
        loginotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userid=phone.getText().toString();
                SharedPreferences userP = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userP.edit();
                editor.putString("name", userid);

                editor.commit();

                SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedpreferences.edit();
                editor1.putInt("shareflow", 0);
                editor1.commit();
                if(userid.length()==10)
                new ItemsByKeyword().execute(url);
                else

                    Toast.makeText(getApplicationContext(),
                            "Please enter the correct phone number!",
                            Toast.LENGTH_LONG).show();

            }
        });


    }
    public void onBackPressed() {
        Intent in=new Intent(Login_with_otp.this,Landing.class);
        finish();
        startActivity(in);
        overridePendingTransition(0, 0);

//        Intent in =new Intent(Login_with_otp.this,MainActivity.class);
//        finish();
//        startActivity(in);
    }
    private class ItemsByKeyword extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... params) {

            // String urldisplay = data[0];
            // HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                //      payload.put("userid", details.get("userid"));
                // payload.put("productid", details.get("productid"));
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);

                HttpPost httppost = new HttpPost(url);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");

                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("fail")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {
                        token = resp.getString("token");
                        SharedPreferences userP = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = userP.edit();
                        editor.putString("tok", token);

                        editor.commit();
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            spinner.setVisibility(View.GONE);

            if (result.contains("fail")) {

                // populateUserDetails();

                Toast.makeText(getApplicationContext(),
                        "Something's Wrong! Please try again!",
                        Toast.LENGTH_LONG).show();

            } else {

                new login_otp().execute(url_otp);

            }

        }
    }
    private class login_otp extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST

                payload.put("userid", userid);
//                payload.put("password", pass);
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);

                HttpPost httppost = new HttpPost(url_otp);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
                httppost.setHeader("x-access-token", token);
                httppost.setHeader("Content-Type", "application/json");


                StringEntity entity = new StringEntity(payload.toString());

                httppost.setEntity(entity);
                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);

                    if (resp.getString("status").contains("error")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return resp.getString("msg");
                    } else {

                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            spinner.setVisibility(View.GONE);

            if (!result.contains("win")) {

                // populateUserDetails();

                    Toast.makeText(getApplicationContext(),
                            result,
                            Toast.LENGTH_LONG).show();


System.out.println("Error"+result);

            } else {
                Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                Intercom.client().registerIdentifiedUser(
                        new Registration().withUserId(phone.getText().toString()));


                Map userMap = new HashMap<>();


                userMap.put("user_id", phone.getText().toString());

                Intercom.client().updateUser(userMap);
                //getAllSms();

                Intent otp=new Intent(Login_with_otp.this,Otp.class);
                otp.putExtra("send",1);
                otp.putExtra("Phone_otp", userid);
finish();
                startActivity(otp);
                overridePendingTransition(0,0);
                //  getAllSms();


            }

        }
    }
}
