package com.example.buddy.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class Otp extends AppCompatActivity {
String name,email,college,phone,truth="",code;BroadcastReceiver smsRecievedListener;
    EditText otp1,otp2,otp3,otp4;
    int check=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        setContentView(R.layout.otp);
         otp1=(EditText)findViewById(R.id.otp1);
      otp2=(EditText)findViewById(R.id.otp2);
         otp3=(EditText)findViewById(R.id.otp3);
        otp4=(EditText)findViewById(R.id.otp4);
        phone=in.getStringExtra("Phone");
        smsRecievedListener = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                code = intent.getStringExtra(SMSreceiver.CODE);
                System.out.println("got the code information");
                if(code.length()==4){
try{
                otp1.setText(String.valueOf(code.charAt(0)));
                otp2.setText(String.valueOf(code.charAt(1)));
                otp3.setText(String.valueOf(code.charAt(2)));
                otp4.setText(String.valueOf(code.charAt(3)));
    new verifyOtp().execute("");}
catch (Exception e){System.out.println(e.toString()+"mera");}}
{

                }
                Toast.makeText(Otp.this,code,Toast.LENGTH_LONG).show();
            }
        };
        TextView verify=(TextView)findViewById(R.id.ver);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


if((otp1.getText().toString().length()==1)&&(otp2.getText().toString().length()==1)&&(otp3.getText().toString().length()==1)&&(otp4.getText().toString().length()==1))
{ code=otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString();

        Toast.makeText(Otp.this,code,Toast.LENGTH_LONG).show();
        if(code.length()==4){

                    new verifyOtp().execute(" ");
                }
           }
        else
        { Toast.makeText(Otp.this,"Please enter your complete otp!",Toast.LENGTH_LONG).show();}

            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter(
                SMSreceiver.INTENT_INFORM_MESSAGE_RECIEVED);
        registerReceiver(smsRecievedListener, filter);
        LocalBroadcastManager.getInstance(getApplicationContext())
                .registerReceiver(smsRecievedListener, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        try {
            unregisterReceiver(smsRecievedListener);
        } catch (Exception e) {
            Log.e("digo", e.getMessage());
        }
    }
    private class verifyOtp extends
            AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                payload.put("otp",code);
            ;
                payload.put("phone",phone);
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String url2 = "http://54.255.147.43:3000/api/login/verifysignupotp";
                HttpPost httppost = new HttpPost(url2);

                httppost.setHeader("x-access-token", Inviteform.token);
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

                    if (resp.getString("msg").contains("error")) {

                        Log.e("MeshCommunication", "Server returned code "
                                + response.getStatusLine().getStatusCode());
                        return resp.getString("msg");
                    } else {
                        truth=resp.getString("msg");
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {

//                    inotp.putExtra("Name", mName);
//                    inotp.putExtra("Email",email.getText().toString());
//                    inotp.putExtra("College",college.getText().toString());
//                    inotp.putExtra("Phone",phone.getText().toString());

if(result.equals("win")) {
    Intent in2 = new Intent(Otp.this, Share.class);
    finish();
    startActivity(in2);
    Toast.makeText(getApplicationContext(),
            truth,
            Toast.LENGTH_LONG).show();
}
else
{Toast.makeText(getApplicationContext(),"Please try again!"
        ,
        Toast.LENGTH_LONG).show();}



        }
    }
}
