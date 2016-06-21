package indwin.c3.shareapp;

import android.Manifest;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

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

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import io.intercom.android.sdk.Intercom;
import io.intercom.android.sdk.identity.Registration;

public class Otp extends AppCompatActivity {
    String name, email, college, fbid = "", phone, truth = "", code;
    BroadcastReceiver smsRecievedListener;
    int w = 0;
    int d = 1;
    private RelativeLayout rwarn;
    private ProgressBar spinner;
    EditText otp1, otp2, otp3, otp4;
    int check = 0, ot_check = 0;
    private int pT, pR, pL, pB;
    private String ref = "";
    private TextView timer, verify;
    SharedPreferences toks;
    String referral_code = "", creditLimit = "", formstatus = "", panoradhar = "", bankaccount = "", collegeid = "", verificationdate = "", rejectionReason = "";
    int l_otp = 0;

    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent in = getIntent();
        toks = getSharedPreferences("token", Context.MODE_PRIVATE);
        w = 1;
        try {
            ref = in.getStringExtra("Ref");
        } catch (Exception e) {
        }
        ot_check = in.getIntExtra("send", 0);
        if (ot_check == 3) {
            setContentView(R.layout.verifyotp);
            ImageView back = (ImageView) findViewById(R.id.backo11);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    onBackPressed();
                }
            });
            TextView t = (TextView) findViewById(R.id.buddyLogo);
            SharedPreferences get = getSharedPreferences("cred", Context.MODE_PRIVATE);
            t.setText("To change your password, please verify with OTP first.Your OTP will be sent to +91-" + get.getString("phone_number", ""));
        } else

            setContentView(R.layout.otp);
        timer = (TextView) findViewById(R.id.textLook);

        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                timer.setText(String.valueOf(millisUntilFinished / 1000));
                timer.setEnabled(false);
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
                timer.setText("Resend Otp");
                timer.setEnabled(true);
                //mTextField.setText("done!");

            }

        }.start();
        timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new resendotp().execute();
                //   Toast.makeText(Otp.this,"Cool",Toast.LENGTH_LONG).show();
            }
        });
        //countDownTimer = new MalibuCountDownTimer(30000, 1000);

        spinner = (ProgressBar) findViewById(R.id.progressBar1);
        otp1 = (EditText) findViewById(R.id.otp1);
        otp1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    otp1.setHint(R.string.hint1);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    otp1.setHint("Entet OTP");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        pL = otp1.getPaddingLeft();
        pT = otp1.getPaddingTop();
        pR = otp1.getPaddingRight();
        pB = otp1.getPaddingBottom();


        otp2 = (EditText) findViewById(R.id.otp2);
        otp3 = (EditText) findViewById(R.id.otp3);
        otp4 = (EditText) findViewById(R.id.otp4);
        //        otp1.addTextChangedListener;'
        rwarn = (RelativeLayout) findViewById(R.id.error);
        otp1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                otp1.setBackgroundResource(R.drawable.texted);
                otp1.setPadding(pL, pT, pR, pB);
                if (otp1.getText().toString().length() == 1)
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
                if (otp2.getText().toString().length() == 1)
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
                if (otp3.getText().toString().length() == 1)
                    otp4.requestFocus();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        TextView message = (TextView) findViewById(R.id.buddyLogo);
        if (ot_check == 0) {

            phone = in.getStringExtra("Phone");
            message.setText("We've sent an SMS containing an OTP to your Phone Number +91-" + phone);
        } else if (ot_check == 3) {

            phone = in.getStringExtra("phone_number");
            message.setText("We've sent an SMS containing an OTP to your Phone Number +91-" + phone);
        } else {
            phone = in.getStringExtra("Phone_otp");
            message.setText("We've sent an SMS containing an OTP to your Phone Number +91-" + phone);
        }


        smsRecievedListener = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub


                code = intent.getStringExtra(SMSreceiver.CODE);
                System.out.println("got the code information");
                if (code.length() == 4) {
                    try {

                        otp1.setText(String.valueOf(code));
                        otp2.setText(String.valueOf(code.charAt(1)));
                        otp3.setText(String.valueOf(code.charAt(2)));
                        otp4.setText(String.valueOf(code.charAt(3)));
                        SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                        w = sharedpreferences.getInt("shareflow", 1);

                        if (((w == 0) && (d == 1)) || (ot_check == 3)) {
                            Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                            Long oldtime = toks.getLong("expires", 0);
                            //        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
                            if (time + 5 < toks.getLong("expires", 0))
                                new verifyOtp().execute("");
                            else
                                new AuthTokc().execute();
                        }
                    } catch (Exception e) {
                        System.out.println(e.toString() + "mera");
                    }
                } else {
                    ActivityCompat.requestPermissions(Otp.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);

                }
                //Toast.makeText(Otp.this,code,Toast.LENGTH_LONG).show();
            }
        };
        verify = (TextView) findViewById(R.id.ver);
        verify.setEnabled(false);
        final TextWatcher mTextEditorWatcher1 = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                rwarn.setVisibility(View.GONE);
                //This sets a textview to the current length
                //  textview.setText(String.valueOf(s.length());
                if (s.length() == 4) {
                    verify.setEnabled(true);
                    if (ot_check == 3)
                        verify.setTextColor(Color.parseColor("#664A4A4A"));
                    else
                        verify.setTextColor(Color.parseColor("#ffffff"));
                } else

                {
                    if (ot_check == 3)
                        verify.setTextColor(Color.parseColor("#664A4A4A"));
                    else
                        verify.setTextColor(Color.parseColor("#66ffffff"));
                }

            }

            public void afterTextChanged(Editable s) {
            }
        };
        otp1.addTextChangedListener(mTextEditorWatcher1);

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verify.setEnabled(false);
                verify.setTextColor(Color.parseColor("#ffffff"));

                if ((otp1.getText().toString().length() == 4))// && (otp2.getText().toString().length() == 1) && (otp3.getText().toString().length() == 1) && (otp4.getText().toString().length() == 1)) {
                {
                    {
                        //                        code = otp1.getText().toString() + otp2.getText().toString() + otp3.getText().toString() + otp4.getText().toString();
                        code = otp1.getText().toString();
                        rwarn.setVisibility(View.GONE);
                    }

                    // Toast.makeText(Otp.this,code,Toast.LENGTH_LONG).show();
                    if (code.trim().length() == 4) {
                        rwarn.setVisibility(View.GONE);
                        w = 0;

                        Long time = Calendar.getInstance().getTimeInMillis() / 1000;
                        Long oldtime = toks.getLong("expires", 0);
                        //        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
                        if (time + 5 < toks.getLong("expires", 0))
                            new verifyOtp().execute("");
                        else
                            new AuthTokc().execute();

                    }
                } else {
                    verify.setEnabled(true);
                    if (ot_check == 3)
                        verify.setTextColor(Color.parseColor("#664A4A4A"));
                    else
                        verify.setTextColor(Color.parseColor("#66ffffff"));
                    rwarn.setVisibility(View.VISIBLE);
                    otp1.setBackgroundResource(R.drawable.texted2);
                    otp1.setPadding(pL, pT, pR, pB);
                    TextView err = (TextView) findViewById(R.id.msg);
                    err.setText("Complete OTP!");
                    //   Toast.makeText(Otp.this, "Please enter your complete otp!", Toast.LENGTH_LONG).show();
                }

            }
        });
        try {
            Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
            Intercom.client().openGCMMessage(getIntent());
        } catch (Exception e) {
        }
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
        if (ot_check == 1) {
            Intent in = new Intent(Otp.this, Login_with_otp.class);
            finish();
            startActivity(in);
            overridePendingTransition(0, 0);
        }
        if (ot_check == 3) {
            //            Intent in = new Intent(Otp.this, Login_with_otp.class);
            finish();
            //            startActivity(in);
            //            overridePendingTransition(0, 0);
        } else {
            Intent in = new Intent(Otp.this, Inviteform.class);
            finish();
            startActivity(in);
            overridePendingTransition(0, 0);
        }
    }

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
                String url2 = "";


                HttpPost httppost;
                if (ot_check == 0) {
                    payload.put("otp", code);

                    payload.put("phone", phone);
                    url2 =BuildConfig.SERVER_URL + "api/login/verifyotp";
                    httppost = new HttpPost(url2);

                    String tok_sp = toks.getString("token_value", "");
                    httppost.setHeader("x-access-token", tok_sp);
                } else {
                    payload.put("otp", code);

                    payload.put("phone", phone);
                    url2 = BuildConfig.SERVER_URL + "api/auth/verifyotp";
                    httppost = new HttpPost(url2);
                    SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                    String tok_sp = toks.getString("token_value", "");
                    httppost.setHeader("x-access-token", tok_sp);

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
                    if (ot_check == 0) {
                        JSONObject resp = new JSONObject(responseString);
                        JSONObject data1 = new JSONObject(resp.getString("data"));
                        try {

                            referral_code = data1.getString("uniqueCode");

                            name = data1.getString("name");
                            email = data1.getString("email");
                        } catch (Exception e) {
                        }
                        try {
                            try {
                                creditLimit = data1.getString("creditLimit");
                            } catch (Exception e) {
                            }
                            try {
                                rejectionReason = data1.getString("rejectionReason");
                            } catch (Exception e) {
                            }
                            try {
                                fbid = data1.getString("fbConnected");
                            } catch (Exception e) {
                                fbid = "empty";
                            }
                            if (fbid.equals("") || (fbid.equals("false")))
                                fbid = "empty";
                            try {
                                formstatus = data1.getString("formStatus");
                            } catch (Exception e) {
                                formstatus = "empty";
                            }
                            if (formstatus.equals(""))
                                formstatus = "empty";
                            try {
                                panoradhar = data1.getString("addressProofs");
                            } catch (Exception e) {
                                panoradhar = "NA";
                            }
                            if (panoradhar.equals(""))
                                panoradhar = "NA";
                            String profileStatus = "";
                            try {
                                profileStatus = data1.getString("profileStatus");
                            } catch (Exception e) {
                                profileStatus = "";
                            }

                            try {
                                bankaccount = data1.getString("bankAccountNumber");
                            } catch (Exception e) {
                                bankaccount = "NA";
                            }
                            if (bankaccount.equals(""))
                                bankaccount = "NA";
                            try {
                                verificationdate = data1.getString("collegeIdVerificationDate");
                            } catch (Exception e) {
                                verificationdate = "NA";
                            }
                            if (verificationdate.equals(""))
                                verificationdate = "NA";
                            try {
                                collegeid = data1.getString("collegeIDs");
                            } catch (Exception e) {
                                collegeid = "NA";
                            }
                            if (collegeid.equals(""))
                                collegeid = "NA";
                            try {
                                SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editorP = userP.edit();


                                editorP.putString("profileStatus", profileStatus);
                                editorP.commit();
                            } catch (Exception e) {
                            }
                            try {
                                String dpid = data1.getString("fbUserId");
                                SharedPreferences sf = getSharedPreferences("proid", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor2 = sf.edit();
                                editor2.putString("dpid", dpid);

                                //  editor2.putString("password", password.getText().toString());
                                editor2.commit();
                            } catch (Exception e) {
                            }
                            //// TODO: 2/7/2016  add college id field and check


                        } catch (Exception e) {
                        }
                        if (resp.getString("msg").contains("error")) {

                            Log.e("MeshCommunication", "Server returned code "
                                    + response.getStatusLine().getStatusCode());
                            return resp.getString("msg");
                        } else {
                            truth = resp.getString("msg");
                            return "win";

                        }
                    } else if ((ot_check == 1) || (ot_check == 3))

                    {
                        JSONObject forget = new JSONObject(responseString);
                        //                            if(forget.getString("status"))
                        String status = "";
                        try {
                            status = forget.getString("status");
                        } catch (Exception e) {

                        }
                        if (status.equals("success"))
                            return "win";
                        else
                            return "fail";
                    } else {
                        return "fail";
                    }
                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }

        }

        protected void onPostExecute(String result) {
            if (ot_check != 3) {
                verify.setEnabled(true);

                verify.setTextColor(Color.parseColor("#ffffff"));
                d = 0;
                spinner.setVisibility(View.GONE);
            }
            if (ot_check == 3) {
                verify.setEnabled(true);

                verify.setTextColor(Color.parseColor("#664A4A4A"));
                d = 0;
                //                spinner.setVisibility(View.GONE);
            }
            //                    inotp.putExtra("Name", mName);
            //                    inotp.putExtra("Email",email.getText().toString());
            //                    inotp.putExtra("College",college.getText().toString());
            //                    inotp.putExtra("Phone",phone.getText().toot_String());

            if (result.equals("win")) {
                SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putInt("shareflow", 1);
                if (ot_check == 1) {
                    if (result.equals("win")) {
                        Intent in = new Intent(Otp.this, ResetPass.class);
                        finish();
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        //            String re=result;
                        //            Toast.makeText(Otp.this,"re",Toast.LENGTH_LONG).show();
                    }

                } else if (ot_check == 3) {
                    if (result.equals("win")) {
                        SharedPreferences tt = getSharedPreferences("cred", Context.MODE_PRIVATE);
                        SharedPreferences.Editor s = tt.edit();
                        s.putInt("add", 1);
                        s.commit();
                        Intent in = new Intent(Otp.this, Editaddress.class);
                        finish();
                        startActivity(in);
                        //overridePendingTransition(0, 0);
                        //            String re=result;
                        //            Toast.makeText(Otp.this,"re",Toast.LENGTH_LONG).show();
                    }
                } else if (ot_check == 0) {

                    editor.putString("tok", Inviteform.token);
                    editor.putString("number", phone);
                    editor.putString("rcode", referral_code);
                    editor.putInt("sign", 1);
                    editor.commit();
                    Intent in2 = new Intent(Otp.this, Share.class);
                    in2.putExtra("refer", referral_code);
                    in2.putExtra("name", name);
                    in2.putExtra("fbid", fbid);
                    in2.putExtra("email", email);
                    finish();
                    startActivity(in2);
                    try {
                        setUpPush();
                        //            Intercom.initialize((Application) getApplicationContext(), "android_sdk-a252775c0f9cdd6cd922b6420a558fd2eb3f89b0", "utga6z2r");
                        Intercom.client().registerIdentifiedUser(
                                new Registration().withUserId(phone));

                        //            Intercom.client().openGCMMessage(getIntent());
                    } catch (Exception e) {
                        System.out.println("Intercom four" + e.toString());
                    }
                    // You can send attributes of any name/value
                    Map userMap = new HashMap<>();
                    userMap.put("name", name);
                    userMap.put("email", email);
                    userMap.put("user_id", phone);
                    userMap.put("referralCode", ref);
                    userMap.put("uniqueCode", referral_code);
                    userMap.put("phone", phone);
                    userMap.put("logged_in", true);
                    userMap.put("phoneVerified", true);
                    try {
                        Intercom.client().updateUser(userMap);
                        Toast.makeText(getApplicationContext(),
                                "Successfully Signed Up",
                                Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        System.out.println("Intercom four" + e.toString());
                    }

                }

            } else {

                rwarn.setVisibility(View.VISIBLE);
                otp1.setBackgroundResource(R.drawable.texted2);
                otp1.setPadding(pL, pT, pR, pB);
                SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                //    editor.putString("rcode", referral_code);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("number", "");
                editor.putString("code", "");

                editor.commit();
                //    Toast.makeText(getApplicationContext(),"Please try again!"
                //        ,
                //        Toast.LENGTH_LONG).show();
            }


        }
    }

    private void setUpPush() {
        //make sure we have google play services available,
        //without it we can't receive push notifications
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegistrationIntentService.class);
            startService(intent);
        }
    }

    public boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        return resultCode == ConnectionResult.SUCCESS;
    }

    private class resendotp extends
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

                payload.put("phone", phone);
                //                payload.put("password", pass);
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                String url_otp = BuildConfig.SERVER_URL + "api/login/resendotp?phone=" + phone;
                HttpPost httppost = new HttpPost(url_otp);
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp = toks.getString("token_value", "");
                httppost.setHeader("Authorization", "Basic YnVkZHlhcGlhZG1pbjptZW1vbmdvc2gx");
                httppost.setHeader("x-access-token", tok_sp);
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


                System.out.println("Error" + result);

            } else {

                new CountDownTimer(30000, 1000) {

                    public void onTick(long millisUntilFinished) {
                        timer.setText(String.valueOf(millisUntilFinished / 1000));
                        timer.setEnabled(false);
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        timer.setText("Resend Otp");
                        timer.setEnabled(true);
                        //mTextField.setText("done!");

                    }

                }.start();
            }
        }
    }


    public class AuthTokc extends
                          AsyncTask<String, Void, String> {

        private String apiN = "";

        //        Context context;
        //        AuthTok(Context context) {
        //            this.context = context;
        //        }
        //    Splash obj=new Splash();
        @Override
        protected String doInBackground(String... params) {
            JSONObject payload = new JSONObject();
            //            String urldisplay = params[0];
            //            apiN=urldisplay;
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
                String urll = BuildConfig.SERVER_URL + "authenticate";
                HttpPost httppost = new HttpPost(urll);
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
                        String token1 = "";


                        SharedPreferences.Editor editorP = toks.edit();
                        token1 = resp.getString("token");
                        editorP.putString("token_value", token1);
                        editorP.putLong("expires", resp.getLong("expiresAt"));
                        editorP.commit();
                        return "win";

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror111", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            if (result.equals("win")) {

                //   Toast.makeText(Otp.this, "yey", Toast.LENGTH_SHORT).show();
                //            next.new FacebookAuth.fblogin().execute();
                new verifyOtp().execute("");
                // new fblogin().execute();
                //            next.fblogin().execute();
                //   new forgotpass().execute();


            }
        }
    }

}

