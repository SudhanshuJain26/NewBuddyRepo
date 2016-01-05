package com.example.buddy.myapplication;

import android.app.Application;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.util.regex.Matcher;

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

import java.util.List;
import java.util.regex.Pattern;

import io.intercom.android.sdk.Intercom;

public class Inviteform extends AppCompatActivity {
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private WebView form;
    private Pattern pattern;
    private Matcher matcher;
    int an=0,ae=0,ac=0,ap=0;
    Intent intform;  EditText name,email,phone;
    AutoCompleteTextView college;
    String mName,mEmail,mCollege,mPhone,truth="";static String token="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inviteform);

        name=(EditText)findViewById(R.id.name);

        email=(EditText)findViewById(R.id.email);
        college=(AutoCompleteTextView)findViewById(R.id.college);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, colleges);
        phone=(EditText)findViewById(R.id.phone);
        college.setAdapter(adapter);

        try {
            toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);

        }
        catch(Exception e){System.out.println(e.toString()+"digo");}
        TextView invite=(TextView)findViewById(R.id.invite);
                invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try { mName=name.getText().toString();
                     String EMAIL_PATTERN =
                            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";




                    mEmail=email.getText().toString();
                    pattern = Pattern.compile(EMAIL_PATTERN);
                    matcher = pattern.matcher(mEmail);
                    Boolean checkemail=matcher.matches();
                    mCollege=college.getText().toString();
                    mPhone=phone.getText().toString();
                    if(mName.length()!=0)
                        an=1;
                    if((mEmail.length()!=0)&&(checkemail))
                        ae=1;
                    if(mPhone.length()!=0)
                        ap=1;
                        ac=1;
                    if((an==1)&&(ae==1)&&(ap==1)&&(ac==1))
                    new ItemsByKeyword().execute("");
                    else
                        if(!checkemail)
                            Toast.makeText(Inviteform.this,"Please enter a valid email-id!",Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(Inviteform.this,"Please complete the details",Toast.LENGTH_LONG).show();


//
                }
                catch(Exception e){
                    Toast.makeText(Inviteform.this,e.toString(),Toast.LENGTH_LONG).show();}



                //                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                      startActivity(intent);
            }
        });
    }
    private static final String[] colleges=new String[]{};
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
    private class ItemsByKeyword extends
            AsyncTask<String, Void, String> {
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

                HttpPost httppost = new HttpPost("http://54.255.147.43:3000/authenticate");
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
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {

            if (result.contains("fail")) {

                // populateUserDetails();

                Toast.makeText(getApplicationContext(),
                        "Something's Wrong! Please try again!",
                        Toast.LENGTH_LONG).show();

            } else {

                new sendOtp().execute();

            }

        }
    }
    private class sendOtp extends
            AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                payload.put("name",mName);
                payload.put("email", mEmail);
               // payload.put("college", mCollege);
                payload.put("phone", mPhone);
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //api/login/sendotp
                String url2 = "http://54.255.147.43:3000/api/login/signup";
                HttpPost httppost = new HttpPost(url2);

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
                        return resp.getString("status");
                    } else {
                        truth=resp.getString("status");
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {if(result.equals("win")){
            if(truth.equals("success")){
            Intent inotp=new Intent(Inviteform.this,Otp.class);
//                    inotp.putExtra("Name", mName);
//                    inotp.putExtra("Email",email.getText().toString());
//                    inotp.putExtra("College",college.getText().toString());
                    inotp.putExtra("Phone",phone.getText().toString());


            startActivity(inotp);

            Toast.makeText(getApplicationContext(),
                    truth,
                    Toast.LENGTH_LONG).show();}
        else{
                Intent inotp=new Intent(Inviteform.this,Share.class);
//                    inotp.putExtra("Name", mName);
//                    inotp.putExtra("Email",email.getText().toString());
//                    inotp.putExtra("College",college.getText().toString());
            inotp.putExtra("Phone",phone.getText().toString());


            startActivity(inotp);

            Toast.makeText(getApplicationContext(),
                    truth,
                    Toast.LENGTH_LONG).show();
        }





        }
    }

}}
