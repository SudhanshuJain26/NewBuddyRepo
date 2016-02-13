package indwin.c3.shareapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
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

public class Otp extends AppCompatActivity {
String name,email,college,phone,truth="",code;BroadcastReceiver smsRecievedListener;
    int w=0;int d=1;
private ProgressBar spinner;
    EditText otp1,otp2,otp3,otp4;
    int check=0,ot_check=0;
    private String ref="";
    String referral_code="",creditLimit="",formstatus="",panoradhar="",bankaccount="",collegeid="",verificationdate="",rejectionReason="";
    int l_otp=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        w=1;
        try{
            ref=in.getStringExtra("Ref");
        }
        catch (Exception e){}
        ot_check=in.getIntExtra("send",0);
        setContentView(R.layout.otp);
        spinner=(ProgressBar)findViewById(R.id.progressBar1);
         otp1=(EditText)findViewById(R.id.otp1);
      otp2=(EditText)findViewById(R.id.otp2);
         otp3=(EditText)findViewById(R.id.otp3);
        otp4=(EditText)findViewById(R.id.otp4);
//        otp1.addTextChangedListener;'
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(otp1.getText().toString().length()==1)
                    otp2.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(otp2.getText().toString().length()==1)
                    otp3.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        otp3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(otp3.getText().toString().length()==1)
                    otp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        if(ot_check==0)
        phone=in.getStringExtra("Phone");
        else
        phone=in.getStringExtra("Phone_otp");
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
    SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
     w =sharedpreferences.getInt("shareflow",1);
    if((w==0)&&(d==1))
    new verifyOtp().execute("");}
catch (Exception e){System.out.println(e.toString()+"mera");}}
{

                }
                //Toast.makeText(Otp.this,code,Toast.LENGTH_LONG).show();
            }
        };
        TextView verify=(TextView)findViewById(R.id.ver);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


if((otp1.getText().toString().length()==1)&&(otp2.getText().toString().length()==1)&&(otp3.getText().toString().length()==1)&&(otp4.getText().toString().length()==1))
{ code=otp1.getText().toString()+otp2.getText().toString()+otp3.getText().toString()+otp4.getText().toString();

       // Toast.makeText(Otp.this,code,Toast.LENGTH_LONG).show();
        if(code.length()==4){

            w=0;
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
    @Override
    public void onBackPressed() {
       if(ot_check==1){
           Intent in= new Intent(Otp.this,Login_with_otp.class);
        finish();
        startActivity(in);
           overridePendingTransition(0, 0);
    }

    else
       { Intent in= new Intent(Otp.this,Inviteform.class);
           finish();
           startActivity(in);
           overridePendingTransition(0, 0);}}
    private class verifyOtp extends
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

                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String url2="";


                HttpPost httppost ;
                if(ot_check==0) {
                    payload.put("otp",code);

                    payload.put("phone", phone);
                    url2 = getApplicationContext().getString(R.string.server)+"api/login/verifyotp";
                    httppost = new HttpPost(url2);
                    httppost.setHeader("x-access-token", Inviteform.token);
                }
                else{
                    payload.put("otp",code);

                    payload.put("phone", phone);
                    url2=getApplicationContext().getString(R.string.server)+"api/login/verifyotp";
                    httppost = new HttpPost(url2);
                    httppost.setHeader("x-access-token", Login_with_otp.token);

                    }

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
                    JSONObject data1 = new JSONObject(resp.getString("data"));
                    try{

referral_code=data1.getString("uniqueCode");

                    name=data1.getString("name");
                    email=data1.getString("email");}
                    catch (Exception e){}
                    try{
                        try {
                            creditLimit = data1.getString("creditLimit");
                        }
                        catch(Exception e){}
                        try {
                            rejectionReason = data1.getString("rejectionReason");
                        }
                        catch(Exception e){}
                        try{
                            formstatus = data1.getString("formStatus");}
                        catch(Exception e){
                            formstatus="empty";
                        }
                        if(formstatus.equals(""))
                            formstatus="empty";
                        try{
                            panoradhar = data1.getString("panOrAadhar");}
                        catch(Exception e){
                            panoradhar="NA";
                        }
                        if(panoradhar.equals(""))
                            panoradhar="NA";
                        try{
                            bankaccount = data1.getString("bankAccountNumber");}
                        catch(Exception e){
                            bankaccount="NA";
                        }
                        if(bankaccount.equals(""))
                            bankaccount="NA";
                        try{
                            verificationdate = data1.getString("collegeIdVerificationDate");}
                        catch(Exception e){
                            verificationdate="NA";
                        }
                        if(verificationdate.equals(""))
                            verificationdate="NA";
                        try{
                            collegeid = data1.getString("collegeId");}
                        catch(Exception e){
                            collegeid="NA";
                        }
                        if(collegeid.equals(""))
                            collegeid="NA";
                        //// TODO: 2/7/2016  add college id field and check


                    }
                    catch(Exception e){}
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
            d=0;
spinner.setVisibility(View.GONE);
//                    inotp.putExtra("Name", mName);
//                    inotp.putExtra("Email",email.getText().toString());
//                    inotp.putExtra("College",college.getText().toString());
//                    inotp.putExtra("Phone",phone.getText().toot_String());

if(result.equals("win")) {
    SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putInt("shareflow",1);
    if(ot_check==1) {
if(w==0)
    Toast.makeText(getApplicationContext(),
            "Successfully Logged In",
            Toast.LENGTH_LONG).show();
        Map userMap = new HashMap<>();

        userMap.put("user_id", phone);

        userMap.put("phoneVerified", true);
        Intercom.client().updateUser(userMap);

        editor.putString("number", phone);
        editor.putString("rcode", referral_code);
        editor.putString("tok", Login_with_otp.token);
        editor.putString("code", code);
        editor.commit();
        if ((formstatus.equals("saved"))&&(w==0)) {
w=1;
            Intent in = new Intent(Otp.this, Formempty.class);
            // Intent in = new Intent(MainActivity.this, Inviteform.class);
            in.putExtra("Name", name);
            in.putExtra("Email", email);
            in.putExtra("Form", formstatus);
            in.putExtra("UniC", referral_code);
            finish();
            startActivity(in);
            overridePendingTransition(0,0);

        }
        else
        if ((formstatus.equals("declined"))&&(w==0)) {
w=1;
            Intent in = new Intent(Otp.this, Formempty.class);
            finish();
            // Intent in = new Intent(MainActivity.this, Inviteform.class);
            in.putExtra("Name", name);
            in.putExtra("Rej",rejectionReason);
            in.putExtra("Email", email);
            in.putExtra("Form", formstatus);
            in.putExtra("UniC", referral_code);
            startActivity(in);
            overridePendingTransition(0, 0);
        }
        else if ((formstatus.equals("submitted"))&&(w==0)) {
            //                 Intent in = new Intent(MainActivity.this, Landing.class);
            Intent in = new Intent(Otp.this, Formstatus.class);
w=1;
            if((panoradhar.equals("NA"))||(bankaccount.equals("NA"))||(collegeid.equals("NA")))
            {
                in.putExtra("screen_no",1);
            }
            else if((!panoradhar.equals("NA"))&&(!bankaccount.equals("NA"))&&(!collegeid.equals("NA"))&&(verificationdate.equals("NA")))
            {
                in.putExtra("screen_no",2);
            }
            else if((!panoradhar.equals("NA"))&&(!bankaccount.equals("NA"))&&(!verificationdate.equals("NA"))&&(!collegeid.equals("NA")))
            {
                in.putExtra("screen_no",3);
                in.putExtra("VeriDate",verificationdate);
            }
            in.putExtra("Name", name);
            in.putExtra("Email", email);
            in.putExtra("Form", formstatus);
            in.putExtra("UniC", referral_code);
            finish();
            startActivity(in);
            overridePendingTransition(0, 0);
        }
        if ((formstatus.equals("approved"))&&(w==0)) {
//w=1;
            Intent in = new Intent(Otp.this, Approved.class);
            // Intent in = new Intent(MainActivity.this, Inviteform.class);
            in.putExtra("Name", name);
            in.putExtra("Email", email);//  in.putExtra("Credits",creditLimit);
            in.putExtra("Form", formstatus);
            in.putExtra("Credits", creditLimit);
            in.putExtra("UniC", referral_code);
            finish();
            w=1;
            startActivity(in);
            overridePendingTransition(0, 0);
        } else if ((formstatus.equals("empty"))&&(w==0)) {
//                    Intent in = new Intent(MainActivity.this, Inviteform    .class);
            Intent in = new Intent(Otp.this, Formempty.class);
            in.putExtra("Name", name);
            w=1;
            in.putExtra("Email", email);
            in.putExtra("Form", formstatus);
            in.putExtra("UniC", referral_code);
            finish();
            startActivity(in);
            overridePendingTransition(0,0);
        }

    }
    else if(ot_check==0){

        editor.putString("tok",Inviteform.token);
            editor.putString("number", phone);
        editor.putString("rcode", referral_code);
        editor.putInt("sign", 1);
        editor.commit();
    Intent in2 = new Intent(Otp.this, Share.class);
        in2.putExtra("refer",referral_code);
        in2.putExtra("name",name);
        in2.putExtra("email",email);
        finish();
        startActivity(in2);
        // You can send attributes of any name/value
        Map userMap = new HashMap<>();
        userMap.put("name", name);
        userMap.put("email", email);
        userMap.put("user_id", phone);
        userMap.put("referralCode",ref);
        userMap.put("uniqueCode", referral_code);
        userMap.put("phone", phone);
        userMap.put("phoneVerified", true);
        Intercom.client().updateUser(userMap);
        Toast.makeText(getApplicationContext(),
            "Successfully Signed Up",
            Toast.LENGTH_LONG).show();}

                         }
else
{
    SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedpreferences.edit();
    editor.putString("number", "");
    editor.putString("code","");

    editor.commit();
    Toast.makeText(getApplicationContext(),"Please try again!"
        ,
        Toast.LENGTH_LONG).show();}



        }
    }
}
