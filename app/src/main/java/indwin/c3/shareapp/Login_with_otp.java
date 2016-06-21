package indwin.c3.shareapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

public class Login_with_otp extends AppCompatActivity {
    String url = "";
    String url_otp="";
    private ProgressBar spinner;
    private TextView loginotp;
    String userid="";
    static String token="";
    private RelativeLayout error;
    private TextView msg;
    EditText phone;
    private int pT,pR,pL,pB;
    private int checknu=0;
    SharedPreferences userP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_with_otp);
        userP = getSharedPreferences("token", Context.MODE_PRIVATE);
        ImageView bac=(ImageView)findViewById(R.id.backo);
        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in=new Intent(Login_with_otp.this,MainActivity.class);
                finish();
                startActivity(in);
                overridePendingTransition(0, 0);
            }
        });
        error=(RelativeLayout)findViewById(R.id.error);
        msg=(TextView)findViewById(R.id.msg);
        spinner=(ProgressBar)findViewById(R.id.progressBar1);
        url=BuildConfig.SERVER_URL+"authenticate";
        url_otp=BuildConfig.SERVER_URL+"api/login/sendotp";
        phone=(EditText)findViewById(R.id.phone_number);
        pL = phone.getPaddingLeft();
        phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    phone.setHint(R.string.h2);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    phone.setHint("Phone Number");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        pT = phone.getPaddingTop();
        pR = phone.getPaddingRight();
        pB = phone.getPaddingBottom();
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
        loginotp=(TextView)findViewById(R.id.Login);
        loginotp.setEnabled(false);
        loginotp.setTextColor(Color.parseColor("#66ffffff"));


        final TextWatcher mTextEditorWatcher1 = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                phone.setBackgroundResource(R.drawable.texted);
                phone.setPadding(pL, pT, pR, pB);
                error.setVisibility(View.INVISIBLE);

                //  textview.setText(String.valueOf(s.length());
                if(s.length()==10)
                    checknu=1;
                else
                    checknu=0;
                if((checknu==1))
                {
                    loginotp.setTextColor(Color.parseColor("#ffffff"));
                    loginotp.setEnabled(true);}
                else{
                    loginotp.setTextColor(Color.parseColor("#66ffffff"));
                    loginotp.setEnabled(false);}
            }

            public void afterTextChanged(Editable s) {
            }
        };
        phone.addTextChangedListener(mTextEditorWatcher1);
        int res=ContextCompat.checkSelfPermission(Login_with_otp.this, Manifest.permission.RECEIVE_SMS);
        if(res== PackageManager.PERMISSION_GRANTED){loginotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginotp.setEnabled(false);
                loginotp.setTextColor(Color.parseColor("#ffffff"));
                SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
                SharedPreferences.Editor edc=cred.edit();
                edc.putString("phone_number",phone.getText().toString());
                edc.commit();
                userid=phone.getText().toString();
                SharedPreferences userP = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = userP.edit();
                editor.putString("name", userid);

                editor.commit();

                SharedPreferences sharedpreferences = getSharedPreferences("buddyotp", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor1 = sharedpreferences.edit();
                editor1.putInt("shareflow", 0);
                editor1.commit();
                if(userid.length()==10){
//                new login_otp().execute(url);
                    Long time= Calendar.getInstance().getTimeInMillis()/1000;
                    Long oldtime=userP.getLong("expires",0);
//        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
                    if(time+5<userP.getLong("expires",0))
                        new login_otp().execute(url);
                    else
                        new AuthTokc().execute();}
                else

                {
                    loginotp.setEnabled(true);
                    phone.setBackgroundResource(R.drawable.texted2);
                    phone.setPadding(pL, pT, pR, pB);
                    error.setVisibility(View.VISIBLE);
                    msg.setText( "Please enter the correct phone number!");
                    loginotp.setTextColor(Color.parseColor("#66ffffff"));
//                    Toast.makeText(getApplicationContext(),
//                            "Please enter the correct phone number!",
//                            Toast.LENGTH_LONG).show();
                }

            }
        });}
        else
        {
            ActivityCompat.requestPermissions(Login_with_otp.this, new String[]{Manifest.permission.RECEIVE_SMS}, 1);
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    loginotp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loginotp.setEnabled(false);
                            loginotp.setTextColor(Color.parseColor("#ffffff"));
                            SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edc=cred.edit();
                            edc.putString("phone_number",phone.getText().toString());
                            edc.commit();
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
                                //  new login_otp().execute(url);
                                new AuthTokc().execute();
                            else
                            {loginotp.setEnabled(true);
                                phone.setBackgroundResource(R.drawable.texted2);
                                phone.setPadding(pL, pT, pR, pB);
                                error.setVisibility(View.VISIBLE);
                                msg.setText( "Please enter the correct phone number!");
                                loginotp.setTextColor(Color.parseColor("#66ffffff"));
//                                Toast.makeText(getApplicationContext(),
//                                        "Please enter the correct phone number!",
//                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });
                }
                else{
                    loginotp.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            loginotp.setEnabled(false);
                            loginotp.setTextColor(Color.parseColor("#ffffff"));
                            SharedPreferences cred = getSharedPreferences("cred", Context.MODE_PRIVATE);
                            SharedPreferences.Editor edc=cred.edit();
                            edc.putString("phone_number",phone.getText().toString());
                            edc.commit();
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
                            {

                                Long time= Calendar.getInstance().getTimeInMillis()/1000;
                                Long oldtime=userP.getLong("expires",0);
//        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
                                if(time+5<userP.getLong("expires",0))
                                    new login_otp().execute(url);
                                else
                                    new AuthTokc().execute();
                                //
                            }
                            else
                            {loginotp.setEnabled(true);
                                loginotp.setTextColor(Color.parseColor("#66ffffff"));
//                                Toast.makeText(getApplicationContext(),
//                                        "Please enter the correct phone number!",
//                                        Toast.LENGTH_LONG).show();
                                error.setVisibility(View.VISIBLE);
                                phone.setBackgroundResource(R.drawable.texted2);
                                phone.setPadding(pL, pT, pR, pB);
                                msg.setText( "Please enter the correct phone number!");
                            }

                        }
                    });
                }
                break;}}
    public void onBackPressed() {
        Intent in=new Intent(Login_with_otp.this,MainActivity.class);
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
                url_otp=BuildConfig.SERVER_URL+"api/auth/sendotp?phone="+userid;
                HttpPost httppost = new HttpPost(url_otp);
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp=toks.getString("token_value","");
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
            loginotp.setEnabled(true);

            if (!result.contains("win")) {
                loginotp.setTextColor(Color.parseColor("#66ffffff"));

                // populateUserDetails();
                error.setVisibility(View.VISIBLE);
                msg.setText(result);
                phone.setBackgroundResource(R.drawable.texted2);
                phone.setPadding(pL, pT, pR, pB);
                Toast.makeText(getApplicationContext(),
                        result,
                        Toast.LENGTH_LONG).show();


                System.out.println("Error" + result);

            } else {



                Intent otp=new Intent(Login_with_otp.this,Otp.class);
                otp.putExtra("send",1);
                otp.putExtra("Phone_otp", userid);
                finish();
                startActivity(otp);
                overridePendingTransition(0, 0);

                //  getAllSms();


            }

        }
    }

    public  class AuthTokc extends
            AsyncTask<String, Void, String> {

        private String apiN="";
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
                String urll=BuildConfig.SERVER_URL + "authenticate";
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
                        String token1="";

                        SharedPreferences userP = getSharedPreferences("token", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editorP = userP.edit();
                        token1 = resp.getString("token");
                        editorP.putString("token_value",token1);
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
            if(result.equals("win")){

                //  Toast.makeText(Login_with_otp.this, "yey", Toast.LENGTH_SHORT).show();
//            next.new FacebookAuth.fblogin().execute();
                // new fblogin().execute();
//            next.fblogin().execute();
//                new forgotpass().execute();
                new login_otp().execute(url);

            }}}
}
