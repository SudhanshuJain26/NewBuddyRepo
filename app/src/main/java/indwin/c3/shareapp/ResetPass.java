package indwin.c3.shareapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ResetPass extends AppCompatActivity {
private EditText newpass,reenter;
    private TextView setpass;
    private String pass="";
    private int check1=0,check2=0;
    private RelativeLayout error;
    private TextView msg;
    private int pT,pR,pL,pB;
    SharedPreferences userP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.forgotpass);
        userP = getSharedPreferences("token", Context.MODE_PRIVATE);
        ImageView bac=(ImageView)findViewById(R.id.backo);
        bac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent in=new Intent(ResetPass.this,Login_with_otp.class);
                finish();

                startActivity(in);
                overridePendingTransition(0, 0);
            }
        });
        error=(RelativeLayout)findViewById(R.id.error);
        msg=(TextView)findViewById(R.id.msg);

         newpass=(EditText)findViewById(R.id.newpass);
        reenter=(EditText)findViewById(R.id.reenter);
          setpass=(TextView)findViewById(R.id.setpass);
        setpass.setEnabled(false);
        newpass.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    newpass.setHint(R.string.h8);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    newpass.setHint("New Password");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });
        reenter.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    reenter.setHint(R.string.h9);
                    //  Toast.makeText(getApplicationContext(), "got the focus", Toast.LENGTH_LONG).show();
                } else {
                    reenter.setHint("Re-enter new Password");
                    // Toast.makeText(getApplicationContext(), "lost the focus", Toast.LENGTH_LONG).show();
                }
            }
        });

        pL=setpass.getPaddingLeft();
         pT = setpass.getPaddingTop();
         pR = setpass.getPaddingRight();
         pB = setpass.getPaddingBottom();


        final TextWatcher mTextEditorWatcher1 = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //This sets a textview to the current length
                newpass.setBackgroundResource(R.drawable.texted);
                newpass.setPadding(pL, pT, pR, pB);
                reenter.setBackgroundResource(R.drawable.texted);
                reenter.setPadding(pL, pT, pR, pB);
                //  textview.setText(String.valueOf(s.length());
                error.setVisibility(View.INVISIBLE);
                if(s.length()>0)
                    check1=1;
                else
                    check1=0;
                if((check1==1)&&(check2==1))
                {
                    setpass.setEnabled(true);
                    setpass.setTextColor(Color.parseColor("#ffffff"));}
                else
                    setpass.setTextColor(Color.parseColor("#66ffffff"));

            }

            public void afterTextChanged(Editable s) {
            }
        };
        newpass.addTextChangedListener(mTextEditorWatcher1);
        final TextWatcher mTextEditorWatcher2 = new TextWatcher() {

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                newpass.setBackgroundResource(R.drawable.texted);
                newpass.setPadding(pL, pT, pR, pB);
                reenter.setBackgroundResource(R.drawable.texted);
                reenter.setPadding(pL, pT, pR, pB);
                //This sets a textview to the current length
                error.setVisibility(View.INVISIBLE);
                //  textview.setText(String.valueOf(s.length());
                if(s.length()>0)
                    check2=1;
                else
                    check2=0;
                if((check1==1)&&(check2==1))
                {        setpass.setEnabled(true);
                    setpass.setTextColor(Color.parseColor("#ffffff"));}
                else
                    setpass.setTextColor(Color.parseColor("#66ffffff"));

            }

            public void afterTextChanged(Editable s) {
            }
        };
        reenter.addTextChangedListener(mTextEditorWatcher2);


        setpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setpass.setEnabled(false);
                setpass.setTextColor(Color.parseColor("#ffffff"));
//                setpass.setTextColor(Integer.parseInt("#ffffff"));
                if((newpass.getText().toString().trim().equals(reenter.getText().toString().trim()))&&(newpass.getText().toString().trim().length()!=0))
                {

                    pass=newpass.getText().toString().trim();
                    Long time= Calendar.getInstance().getTimeInMillis()/1000;
                    Long oldtime=userP.getLong("expires",0);
//        Toast.makeText(FacebookAuth.this, String.valueOf(oldtime), Toast.LENGTH_SHORT).show();
                    if(time+5<userP.getLong("expires",0))
                        new forgotpass().execute();
                    else
                   // new forgotpass().execute();

                    new AuthTokc().execute();
                }
                else
                    if((newpass.getText().toString().trim().length()==0)){
                        error.setVisibility(View.VISIBLE);
                        msg.setText("Please enter a correct password");
                        setpass.setEnabled(true);
                        newpass.setBackgroundResource(R.drawable.texted2);
                        newpass.setPadding(pL, pT, pR, pB);
//                        reenter.setBackgroundResource(R.drawable.texted);
//                        reenter.setPadding(pL, pT, pR, pB);
                     //   Toast.makeText(ResetPass.this,"Please enter a correct password",Toast.LENGTH_LONG).show();
                        setpass.setTextColor(Color.parseColor("#66ffffff"));
                    }

                else
                {
                    newpass.setBackgroundResource(R.drawable.texted2);
                    newpass.setPadding(pL, pT, pR, pB);
                    setpass.setTextColor(Color.parseColor("#66ffffff"));
                    error.setVisibility(View.VISIBLE);
                    msg.setText("The passwords entered don't match!");
                    setpass.setEnabled(true);
                   // Toast.makeText(ResetPass.this,"The passwords entered don't match!",Toast.LENGTH_LONG).show();
                }
            }
        });
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

           //     Toast.makeText(ResetPass.this, "yey", Toast.LENGTH_SHORT).show();
//            next.new FacebookAuth.fblogin().execute();
                // new fblogin().execute();
//            next.fblogin().execute();
                new forgotpass().execute();


            }}}

    private class forgotpass extends
            AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
//            spinner.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... data) {

            // String urldisplay = data[0];
            //   HashMap<String, String> details = data[0];
            JSONObject payload = new JSONObject();
            try {
                // userid=12&productid=23&action=add
                // TYPE: POST
                SharedPreferences ss = getSharedPreferences("buddyin", Context.MODE_PRIVATE);
                String userid=ss.getString("name", "sad");
                if(!userid.equals("sad"))
                {
                payload.put("phone",userid);
                payload.put("password",pass);}
                // payload.put("action", details.get("action"));

                HttpParams httpParameters = new BasicHttpParams();

                HttpConnectionParams
                        .setConnectionTimeout(httpParameters, 30000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                //api/login/sendotp
                String url2 = BuildConfig.SERVER_URL+"api/auth/password/forgot";
                HttpPost httppost = new HttpPost(url2);
                SharedPreferences toks = getSharedPreferences("token", Context.MODE_PRIVATE);
                String tok_sp=toks.getString("token_value","");
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
                        String truth;
                        truth=resp.getString("status");
                        return truth;

                    }

                }

            } catch (Exception e) {
                Log.e("mesherror", e.getMessage());
                return "fail";

            }
        }

        protected void onPostExecute(String result) {
            setpass.setEnabled(true);

        if(result.equals("success"))
        {setpass.setTextColor(Color.parseColor("#66ffffff"));
            Toast.makeText(ResetPass.this,"Password changed successfully!",Toast.LENGTH_LONG).show();
            Intent in=new Intent(ResetPass.this,Successfulreset.class);
            startActivity(in);
            overridePendingTransition(0,0);
            finish();
        }
            else
        {setpass.setTextColor(Color.parseColor("#66ffffff"));
            Toast.makeText(ResetPass.this,"Something's Wrong! Please try again!",Toast.LENGTH_LONG).show();

        }
        }}

    @Override
    public void onBackPressed()
    {

        {

            Intent in=new Intent(ResetPass.this,Login_with_otp.class);
            finish();

            startActivity(in);
            overridePendingTransition(0, 0);

        }
        // code here to show dialoguper.onBackPressed();  // optional depending on your needs
    }
        }
